package org.mcteampotato.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.mcteampotato.SOLValpotato;
import org.mcteampotato.attchment.FoodDataAttachment;
import org.mcteampotato.fooddata.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "eat", at = @At("HEAD"))
    public void eat(Level level, ItemStack food, FoodProperties foodProperties, CallbackInfoReturnable<ItemStack> cir) {
        Player player = (Player) (Object) this;
        ItemStack itemStack = food.copy();
        FoodData.FoodInfo info = FoodData.getInfo(itemStack);
        FoodDataAttachment foodData = player.getData(SOLValpotato.FOOD_DATA);
        if (info == null) {
            SOLValpotato.LOGGER.error("[FoodData Error] {} can find foodinfo", food.getItem().toString());
            return;
        }
        if (!player.isLocalPlayer()) {
            if (itemStack.is(Items.ROTTEN_FLESH)) {
                foodData.clear(player);
            } else {
                foodData.addFood(info, player);
            }
            //TODO 还有神秘的药水效果
//            if (player instanceof ServerPlayer serverPlayer) {
//                Effect(serverPlayer);
//            }
        }
    }

    @Inject(method = "canEat", at = @At("HEAD"), cancellable = true)
    public void alwaysEat(boolean canAlwaysEat, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
