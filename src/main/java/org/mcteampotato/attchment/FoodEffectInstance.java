package org.mcteampotato.attchment;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class FoodEffectInstance extends MobEffectInstance {

    public FoodEffectInstance(Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon) {
        super(effect, duration, amplifier, ambient, visible, showIcon);
    }

}
