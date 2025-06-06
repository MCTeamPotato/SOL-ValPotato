package org.mcteampotato.fooddata.effect;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import org.mcteampotato.SOLValpotato;
import org.mcteampotato.attchment.FoodDataAttachment;
import org.mcteampotato.config.ValpotatoConfig;
import org.mcteampotato.fooddata.FoodInstance;

import java.util.ArrayList;
import java.util.List;

public class FoodEffectHandle {
    public static void handle(ServerPlayer serverPlayer) {
        if (!ValpotatoConfig.ENABLE_EFFECT.get()) return;
        //TODO 还有神秘的药水效果
        FoodDataAttachment foodData = serverPlayer.getData(SOLValpotato.FOOD_DATA);
        List<FoodInstance> foodInstanceList = foodData.getSlots();
        for (MobEffectInstance effect : new ArrayList<>(serverPlayer.getActiveEffects())) {
            if (effect instanceof FoodEffectInstance) {
                serverPlayer.removeEffect(effect.getEffect());
            }
        }
        for (FoodInstance foodInstance : foodInstanceList) {
            FoodProperties foodProps = foodInstance.getInfo().getItemStack().getFoodProperties(null);
            if (foodProps != null) {
                for (FoodProperties.PossibleEffect possibleEffect : foodProps.effects()) {
                    MobEffectInstance foodEffect = possibleEffect.effect();
                    MobEffectInstance currentEffect = serverPlayer.getEffect(foodEffect.getEffect());
                    if (currentEffect == null || currentEffect.getAmplifier() < foodEffect.getAmplifier()) {
                        serverPlayer.addEffect(new FoodEffectInstance(
                                foodEffect.getEffect(),
                                -1,
                                foodEffect.getAmplifier()
                        ));
                    }
                }
            }
        }
    }

    static class FoodEffectInstance extends MobEffectInstance {
        public FoodEffectInstance(Holder<MobEffect> effect, int duration, int amplifier) {
            super(effect, duration, amplifier, false, false, false);
        }
    }
}
