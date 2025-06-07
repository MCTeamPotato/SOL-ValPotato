package org.mcteampotato.attchment;


import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.mcteampotato.SOLValpotato;
import org.mcteampotato.config.ValpotatoConfig;
import org.mcteampotato.fooddata.FoodData;
import org.mcteampotato.fooddata.FoodInstance;
import org.mcteampotato.fooddata.IFoodSlots;
import org.mcteampotato.network.SyncFoodDataPacket;

import java.util.List;


public class FoodDataAttachment implements IFoodSlots, INBTSerializable<CompoundTag> {
    public static final ResourceLocation healthModifier = ResourceLocation.fromNamespaceAndPath(SOLValpotato.MOD_ID, "food_health_add");
    private final List<FoodInstance> slots = new ObjectArrayList<>();

    @Override
    public List<FoodInstance> getSlots() {
        return slots;
    }

    @Override
    public boolean addFood(FoodData.FoodInfo info, Player player) {
        if (isFull()) return false;
        slots.add(new FoodInstance(info, info.getDurationTicks()));
        if (player instanceof ServerPlayer serverPlayer) {
            setHealthModifier(serverPlayer);
            PacketDistributor.sendToPlayer(serverPlayer, new SyncFoodDataPacket(serialize(player.level().registryAccess())));
        }
        return true;
    }

    public int getTotalHealth() {
        int health = 0;
        for (FoodInstance foodInstance : slots) {
            health += foodInstance.getInfo().getHearts();
        }
        return health;
    }

    public void setHealthModifier(Player player) {
        AttributeInstance healthAttribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttribute.hasModifier(healthModifier)) {
            healthAttribute.removeModifier(healthModifier);
        }
        healthAttribute.addPermanentModifier(new AttributeModifier(healthModifier, getTotalHealth(), AttributeModifier.Operation.ADD_VALUE));
    }

    public boolean isSame(Item item) {
        for (FoodInstance foodInstance : slots) {
            return foodInstance.getInfo().getItemStack().is(item);
        }
        return false;
    }

    @Override
    public void tick(Player player) {
        for (FoodInstance foodInstance : slots) {
            foodInstance.decrement();
            if (foodInstance.getRemainingTicks() <= 0) {
                slots.remove(foodInstance);
                setHealthModifier(player);
                PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncFoodDataPacket(serialize(player.level().registryAccess())));
            }
        }

        if (!slots.isEmpty()) {
            for (FoodInstance foodInstance : slots) {
                int ticks = foodInstance.getRemainingTicks();
                if (ticks < 1200 && ticks % 20 != 0) continue;
                else if (ticks >= 1200 && ticks % 1200 != 0) continue;
                PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncFoodDataPacket(serialize(player.level().registryAccess())));
            }
        }
    }

    @Override
    public void clear(Player player) {
        slots.clear();
        if (player instanceof ServerPlayer serverPlayer) {
            AttributeInstance healthAttribute = serverPlayer.getAttribute(Attributes.MAX_HEALTH);
            if (healthAttribute.hasModifier(healthModifier)) {
                healthAttribute.removeModifier(healthModifier);
            }
            PacketDistributor.sendToPlayer(serverPlayer, new SyncFoodDataPacket(serialize(serverPlayer.level().registryAccess())));
        }
    }

    @Override
    public boolean isFull() {
        return slots.size() >= ValpotatoConfig.FOOD_SLOT.get();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        return serialize(provider);
    }

    public CompoundTag serialize(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (FoodInstance foodInstance : slots) {
            CompoundTag instTag = new CompoundTag();
            ItemStack itemStack = foodInstance.getInfo().getItemStack();
            if (itemStack.isEmpty()) continue;
            instTag.put("itemstack", itemStack.save(provider));
            instTag.putInt("remaining", foodInstance.getRemainingTicks());
            list.add(instTag);
        }
        tag.put("slots", list);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
        deserialize(nbt);
    }

    public void deserialize(CompoundTag nbt) {
        slots.clear();
        ListTag list = nbt.getList("slots", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag instTag = list.getCompound(i);
            ItemStack itemstack = ItemStack.CODEC.decode(NbtOps.INSTANCE, instTag.get("itemstack")).getOrThrow().getFirst();
            FoodData.FoodInfo info = FoodData.getInfo(itemstack);
            if (info != null) {
                int rem = instTag.getInt("remaining");
                slots.add(new FoodInstance(info, rem));
            }
        }
    }
}
