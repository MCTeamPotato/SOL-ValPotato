package org.mcteampotato.fooddata.effect;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import org.mcteampotato.SOLValpotato;
import org.mcteampotato.config.ValpotatoConfig;
import org.mcteampotato.fooddata.FoodData;
import org.mcteampotato.fooddata.FoodInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FoodEffectHandle {

    public static void addAllFoodEffectInstance(ServerPlayer serverPlayer) {
        if (!ValpotatoConfig.ENABLE_EFFECT.get()) return;
        if (serverPlayer.tickCount % ValpotatoConfig.EFFECT_INTERVAL_TICK.get() == 0) {
            List<FoodInstance> foodInstanceList = serverPlayer.getData(SOLValpotato.FOOD_DATA).getSlots();
            for (FoodInstance foodInstance : foodInstanceList) {
                FoodEffectHandle.addFoodEffectInstance(serverPlayer, foodInstance);
            }
        }
    }

    public static void addFoodEffectInstance(ServerPlayer serverPlayer, FoodInstance foodInstance) {
        FoodData.FoodInfo foodInfo = foodInstance.getInfo();
        if (foodInfo != null) {
            for (FoodProperties.PossibleEffect possibleEffect : Objects.requireNonNull(foodInfo.getItemStack().getFoodProperties(null)).effects()) {
                MobEffectInstance foodEffect = possibleEffect.effect();
                MobEffectInstance currentEffect = serverPlayer.getEffect(foodEffect.getEffect());
                if (foodEffect.getDuration() >= ValpotatoConfig.EFFECT_MINIMUM_TICK.get() && (currentEffect == null || currentEffect.getAmplifier() <= foodEffect.getAmplifier())) {
                    serverPlayer.addEffect(new FoodEffectInstance(
                            foodEffect.getEffect(),
                            499,
                            foodEffect.getAmplifier()
                    ));
                }
            }
        }
    }

    public static void removeAllFoodEffectInstance(ServerPlayer serverPlayer) {
        if (!ValpotatoConfig.ENABLE_EFFECT.get()) return;
        ArrayList<MobEffectInstance> mobEffectInstances = new ArrayList<>(serverPlayer.getActiveEffects());
        for (MobEffectInstance mobEffectInstance : mobEffectInstances) {
            if (mobEffectInstance instanceof FoodEffectInstance) {
                serverPlayer.removeEffect(mobEffectInstance.getEffect());
            }
        }
    }

    public static void removeFoodEffectInstance(ServerPlayer serverPlayer, FoodInstance foodInstance) {
        if (!ValpotatoConfig.ENABLE_EFFECT.get()) return;
        for (FoodProperties.PossibleEffect possibleEffect : Objects.requireNonNull(foodInstance.getInfo().getItemStack().getFoodProperties(null)).effects()) {
            if (possibleEffect.effect() instanceof FoodEffectInstance) {
                Holder<MobEffect> mobEffect = possibleEffect.effect().getEffect();
                if (serverPlayer.getEffect(mobEffect) != null) {
                    serverPlayer.removeEffect(mobEffect);
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
