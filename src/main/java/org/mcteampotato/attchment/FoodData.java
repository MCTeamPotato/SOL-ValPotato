package org.mcteampotato.attchment;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mcteampotato.attchment.compat.SOLCompat;
import org.mcteampotato.attchment.compat.SomeAssemblyRequired;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class FoodData {
    public static final FoodProperties empty = new FoodProperties(0, 0, false, 0, Optional.empty(), new ArrayList<>());

    public static FoodInfo getInfo(ItemStack itemStack) {
        FoodProperties props = Optional.ofNullable(itemStack.getFoodProperties(null)).orElse(empty);
        int nutrition = props.nutrition();
        if (nutrition <= 0) return null;
        int durationTicks = (int) (10 * Math.log(nutrition+1) * 60 * 20);
        float restore = (float) (0.1 + Math.log1p(Objects.requireNonNull(itemStack.getFoodProperties(null)).saturation()) / 3);
        //        int durationTicks = (nutrition) * 60 * 20; // DEBUG;
        FoodInfo foodInfo = new FoodInfo(itemStack, nutrition, props.saturation(), nutrition, durationTicks,restore);
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
        private final int maxExtraHearts;
        private final float restore;

        public FoodInfo(ItemStack itemStack, int nutrition, float saturation, int hearts, int durationTicks, float restore) {
            this(itemStack, nutrition, saturation, hearts, durationTicks, restore, 0);
        }

        public FoodInfo(ItemStack itemStack, int nutrition, float saturation, int hearts, int durationTicks, float restore, int maxExtraHearts) {
            this.itemStack = itemStack;
            this.nutrition = nutrition;
            this.saturation = saturation;
            this.hearts = hearts;
            this.durationTicks = durationTicks;
            this.restore = restore;
            this.maxExtraHearts = maxExtraHearts;
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

        public int getMaxExtraHearts() {
            return maxExtraHearts;
        }

        public float getRestore() {
            return restore;
        }
    }
}
