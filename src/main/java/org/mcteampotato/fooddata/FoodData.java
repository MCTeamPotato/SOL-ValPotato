package org.mcteampotato.fooddata;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.mcteampotato.fooddata.compat.SOLCompat;
import org.mcteampotato.fooddata.compat.SomeAssemblyRequired;

import java.util.ArrayList;
import java.util.Optional;

public class FoodData {
    public static final FoodProperties empty = new FoodProperties(0, 0, false, 0, Optional.empty(), new ArrayList<>());

    public static FoodInfo getInfo(ItemStack itemStack) {
        FoodProperties props = Optional.ofNullable(itemStack.getFoodProperties(null)).orElse(empty);
        int nutrition = props.nutrition();
        if (nutrition <= 0) return null;
        int durationTicks = (int) (10 * Math.log1p(nutrition) * 60 * 20);
//        int durationTicks = (nutrition) * 60 * 20; // DEBUG;
        float restore = (float) (0.1 + Math.log1p(props.saturation()) / 3);
        FoodInfo foodInfo = new FoodInfo(itemStack, nutrition, props.saturation(), nutrition, durationTicks, restore);
        if (SOLCompat.isLoadSomeAssemblyRequired()) {
            foodInfo = SomeAssemblyRequired.tryResetFoodInfo(foodInfo, itemStack);
        }
        return foodInfo;
    }


    public static class FoodInfo {
        private final ItemStack itemStack;
        private final int nutrition;
        private final float saturation;
        private final int hearts;
        private final int durationTicks;
        private final float restore;

        public FoodInfo(ItemStack itemStack, int nutrition, float saturation, int hearts, int durationTicks, float restore) {
            this.itemStack = itemStack;
            this.nutrition = nutrition;
            this.saturation = saturation;
            this.hearts = hearts;
            this.durationTicks = durationTicks;
            this.restore = restore;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public int getNutrition() {
            return nutrition;
        }

        public float getSaturation() {
            return saturation;
        }

        public int getHearts() {
            return hearts;
        }

        public int getDurationTicks() {
            return durationTicks;
        }

        public float getRestore() {
            return restore;
        }
    }
}
