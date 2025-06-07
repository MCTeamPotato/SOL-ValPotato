package org.mcteampotato.fooddata.health;

import net.minecraft.server.level.ServerPlayer;
import org.mcteampotato.SOLValpotato;
import org.mcteampotato.config.ValpotatoConfig;
import org.mcteampotato.event.GameEvent;
import org.mcteampotato.fooddata.FoodInstance;

public class FoodHealHandle {
    public static void handle(ServerPlayer serverPlayer) {
        if (!ValpotatoConfig.ENABLE_HEAL.get()) return;
        FoodHealType healType = ValpotatoConfig.HEAL_TYPE.get();
        long hurtTime = serverPlayer.getPersistentData().getLong(GameEvent.HURT_KEY);
        long gameTime = serverPlayer.level().getGameTime();
        if (hurtTime != 0 && gameTime - hurtTime >= 200 && gameTime % 10 == 0 && serverPlayer.getHealth() < serverPlayer.getMaxHealth()) {
            switch (healType) {
                case LINEAR -> serverPlayer.heal(0.5f);
                case SATURATION -> {
                    float heal = 0;
                    for (FoodInstance foodInstance : serverPlayer.getData(SOLValpotato.FOOD_DATA).getSlots()) {
                        heal += foodInstance.getInfo().getRestore();
                    }
                    serverPlayer.heal(heal / 2);
                }
            }
        }
    }
}
