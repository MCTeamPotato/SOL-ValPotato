package org.mcteampotato.fooddata;

public class FoodInstance {
    private final FoodData.FoodInfo info;
    private int remainingTicks;

    /**
     * @param info          与 JSON 中定义相对应的 FoodInfo 实例
     * @param remainingTicks 初始的持续时长（以游戏刻 tick 为单位）
     */
    public FoodInstance(FoodData.FoodInfo info, int remainingTicks) {
        this.info = info;
        this.remainingTicks = remainingTicks;
    }

    public FoodData.FoodInfo getInfo() {
        return info;
    }

    public int getRemainingTicks() {
        return remainingTicks;
    }

    /**
     * @return 向下取整的秒数
     */
    public int getRemainingSeconds() {
        return remainingTicks / 20;
    }

    public void decrement() {
        if (remainingTicks > 0) {
            remainingTicks--;
        }
    }

    public boolean isExpired() {
        return remainingTicks <= 0;
    }
}