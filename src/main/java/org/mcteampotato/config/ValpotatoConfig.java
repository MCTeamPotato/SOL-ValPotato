package org.mcteampotato.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.*;

public class ValpotatoConfig {
    public static final Builder BUILDER = new ModConfigSpec.Builder();
    public static final ConfigValue<Integer> FOOD_SLOT = BUILDER
            .comment("Number of food slots")
            .translation("config.sol_valpotato.food_slots")
            .define("foodSlots", 4);
    public static final ConfigValue<Boolean> DISABLE_VANILLA_FOOD = BUILDER
            .comment("Whether to disable vanilla's food system")
            .translation("config.sol_valpotato.disable_vanilla")
            .define("disableVanillaFoodSystem", true);
    public static final ConfigValue<Boolean> EFFECT_TYPE = BUILDER
            .comment("The effect after eat food")
            .translation("config.sol_valpotato.effect_type")
            .define("effectType", true);



}
