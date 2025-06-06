package org.mcteampotato.fooddata.compat;

import net.minecraft.world.item.ItemStack;
import org.mcteampotato.fooddata.FoodData;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.registry.ModItems;

public class SomeAssemblyRequired {
    public static boolean isSandwich(ItemStack itemStack){
        return itemStack.is(ModItems.SANDWICH);
    }

    public static FoodData.FoodInfo tryResetFoodInfo(FoodData.FoodInfo defaultInfo, ItemStack itemStack) {
        if (isSandwich(itemStack)) {
            SandwichContents contents = SandwichContents.get(itemStack);
            int nutrition = contents.nutrition(null);
            float saturation = contents.saturation(null);
            if (nutrition <= 0) return defaultInfo;
            int durationTicks = (int) (10 * Math.log1p(nutrition) * 60 * 20);
//            int durationTicks = (nutrition) * 60 * 20; // DEBUG;
            float restore = (float) (0.1 + Math.log1p(saturation) / 3);
            return new FoodData.FoodInfo(itemStack, nutrition, saturation, nutrition, durationTicks, restore);
        }
        return defaultInfo;
    }
}
