package org.mcteampotato.fooddata;

import net.minecraft.world.entity.player.Player;

import java.util.List;

public interface IFoodSlots {
    List<FoodInstance> getSlots();
    boolean addFood(FoodData.FoodInfo info, Player player);
    void tick(Player player);
    void clear(Player player);
    boolean isFull();
}