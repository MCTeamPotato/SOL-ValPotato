package org.mcteampotato.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.mcteampotato.SOLValpotato;
import org.mcteampotato.attchment.FoodDataAttachment;
import org.mcteampotato.config.ValpotatoConfig;
import org.mcteampotato.fooddata.FoodData;
import org.mcteampotato.fooddata.effect.FoodEffectHandle;
import org.mcteampotato.fooddata.health.FoodHealHandle;
import org.mcteampotato.fooddata.health.FoodHealType;
import org.mcteampotato.network.SyncFoodDataPacket;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = SOLValpotato.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GameEvent {
    public static final String HURT_KEY = "SOL:HurtTime";

    @SubscribeEvent
    public static void tooltipsEvent(ItemTooltipEvent event) {
        List<Component> toolTips = event.getToolTip();
        ItemStack itemStack = event.getItemStack();
        FoodData.FoodInfo foodInfo = FoodData.getInfo(itemStack);

        if (foodInfo != null) {
            if (itemStack.is(Items.OMINOUS_BOTTLE)) return;
            if (itemStack.is(Items.ROTTEN_FLESH)) {
                toolTips.add(1, Component.translatable("tooltips.sol_valpotato.empty").withStyle(ChatFormatting.GREEN));
                return;
            }
            List<Component> list = new ArrayList<>();
            list.add(Component.literal("❤ %.1f ".formatted(((float) foodInfo.getHearts()) / 2)).append(Component.translatable("tooltips.sol_valpotato.heart")).withStyle(ChatFormatting.RED));
            FoodHealType healType = ValpotatoConfig.HEAL_TYPE.get();
            if (healType == FoodHealType.SATURATION) {
                list.add(Component.literal("✚ %.1f ".formatted(foodInfo.getRestore())).append(Component.translatable("tooltips.sol_valpotato.regen")).withStyle(ChatFormatting.GREEN));
            }
            list.add(Component.literal("⌚ %.1f ".formatted(((float) foodInfo.getDurationTicks()) / 1200)).append(Component.translatable("tooltips.sol_valpotato.minute")).withStyle(ChatFormatting.GOLD));
            toolTips.addAll(1, list);
        }
    }

    @SubscribeEvent
    public static void rightClick(PlayerInteractEvent.RightClickItem event) {
        LivingEntity livingEntity = event.getEntity();
        ItemStack stack = event.getItemStack();
        event.setCanceled(checkFood(livingEntity, stack));
    }

    @SubscribeEvent
    public static void useItem(LivingEntityUseItemEvent.Start event) {
        LivingEntity livingEntity = event.getEntity();
        ItemStack stack = event.getItem();
        event.setCanceled(checkFood(livingEntity, stack));
    }

    public static boolean checkFood(LivingEntity livingEntity, ItemStack stack) {
        Item item = stack.getItem();
        FoodData.FoodInfo info = FoodData.getInfo(stack);
        if (item instanceof PotionItem || stack.is(Items.ROTTEN_FLESH) || item instanceof OminousBottleItem)
            return false;
        if (info != null || livingEntity instanceof ServerPlayer serverPlayer && stack.getFoodProperties(serverPlayer) != null) {
            FoodDataAttachment foodData = livingEntity.getData(SOLValpotato.FOOD_DATA);
            return foodData.isFull() || foodData.isSame(item);
        }
        return false;
    }

    @SubscribeEvent
    public static void login(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            FoodDataAttachment foodData = serverPlayer.getData(SOLValpotato.FOOD_DATA);
            PacketDistributor.sendToPlayer(serverPlayer, new SyncFoodDataPacket(foodData.serialize(serverPlayer.level().registryAccess())));
        }
    }

    @SubscribeEvent
    public static void inDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            long gameTime = serverPlayer.level().getGameTime();
            serverPlayer.getPersistentData().putLong(HURT_KEY, gameTime);
        }
    }

    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        FoodDataAttachment foodData = player.getData(SOLValpotato.FOOD_DATA);
        if (player instanceof ServerPlayer serverPlayer) {
            foodData.tick(serverPlayer);
            FoodHealHandle.handle(serverPlayer);
            FoodEffectHandle.addAllFoodEffectInstance(serverPlayer);
        }
    }
}
