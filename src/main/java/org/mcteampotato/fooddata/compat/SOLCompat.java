package org.mcteampotato.fooddata.compat;

import net.neoforged.fml.ModList;

public class SOLCompat {
    public static boolean isLoadSomeAssemblyRequired(){
        return ModList.get().isLoaded("someassemblyrequired");
    }
}
