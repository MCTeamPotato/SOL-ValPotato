package org.mcteampotato.fooddata.health;

import net.minecraft.server.level.ServerPlayer;
import org.mcteampotato.config.ValpotatoConfig;
import org.mcteampotato.event.GameEvent;

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
                    //TODO 把你奇妙的饱和度回复写了
//            float heal = 0;
//            if (hurtTime != 0 && gameTime - hurtTime > 10 * 20 && serverPlayer.tickCount % 5 == 0 && serverPlayer.getHealth() < serverPlayer.getMaxHealth()) {
//                for (FoodInstance foodInstance : foodInstanceList) {
//                    heal += foodInstance.getInfo().getRestore();
//                }
//                heal *= (float) Math.min(((System.currentTimeMillis() - hurtTime - 10 * 20) / 10 * 20), 1);
//                serverPlayer.heal(heal / 4);
//            }
//            if (player.tickCount % 600 == 0) {
//                Effect(serverPlayer);
//            }
                }
            }
        }
    }
}
