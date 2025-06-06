package org.mcteampotato.attchment.compat;

import net.minecraft.world.item.ItemStack;
import org.mcteampotato.attchment.FoodData;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.registry.ModItems;

import java.util.Objects;

public class SomeAssemblyRequired {
    public static boolean isSandwich(ItemStack itemStack){
        return itemStack.is(ModItems.SANDWICH);
    }

    public static FoodData.FoodInfo tryResetFoodInfo(FoodData.FoodInfo defaultInfo, ItemStack itemStack) {
        if (isSandwich(itemStack)) {
            SandwichContents contents = SandwichContents.get(itemStack);
            int nutrition = contents.nutrition(null);
            if (nutrition <= 0) return defaultInfo;
            float saturation = contents.saturation(null);
            int durationTicks = (int) (10 * Math.log(nutrition + 1) * 60 * 20);
            float restore = (float) (0.1 + Math.log1p(Objects.requireNonNull(itemStack.getFoodProperties(null)).saturation()) / 3);
//            int durationTicks = (nutrition) * 60 * 20; // DEBUG;
            return new FoodData.FoodInfo(itemStack, nutrition, saturation, nutrition, durationTicks, restore);
        }
        return defaultInfo;
    }
}
