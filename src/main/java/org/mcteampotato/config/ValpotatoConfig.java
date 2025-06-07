package org.mcteampotato.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec.EnumValue;
import org.mcteampotato.fooddata.health.FoodHealType;

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
    public static final ConfigValue<Boolean> ENABLE_EFFECT = BUILDER
            .comment("Grants all potion effects from the consumed food")
            .translation("config.sol_valpotato.enable_effect")
            .define("enableEffect", false);
    public static final ConfigValue<Integer> EFFECT_MINIMUM_TICK = BUILDER
            .comment("The minimum tick requirement for granting potion effects to food")
            .translation("config.sol_valpotato.effect_minimum_tick")
            .define("effectMinimumTick", 20);
    public static final ConfigValue<Integer> EFFECT_INTERVAL_TICK = BUILDER
            .comment("The interval for granting food with potion effects")
            .translation("config.sol_valpotato.effect_interval_tick")
            .define("effectIntervalTick", 400);
    public static final ConfigValue<Boolean> ENABLE_HEAL = BUILDER
            .comment("Enable Heal after the war")
            .translation("config.sol_valpotato.enable_heal")
            .define("enableHeal", true);
    public static final EnumValue<FoodHealType> HEAL_TYPE = BUILDER
            .comment("Heal type after the war")
            .translation("config.sol_valpotato.heal_type")
            .defineEnum("healType", FoodHealType.LINEAR);
}
