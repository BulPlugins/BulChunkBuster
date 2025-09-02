package com.alihaine.bulchunkbuster.file;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;

public class Config {
    Material materialBuster;
    String blockBusterName;
    List<String> blockBusterLore;

    int busterSpeed;
    int busterSize;

    boolean busterDown;
    boolean busterUp;

    boolean destroyBlockBuster;
    boolean dropDestroyedBlock;
    double chanceDropDestroyedBlock;

    List<String> blackListMaterials;
    List<String> blockListWorlds;

    int cooldown;

    private final HashMap messages = new HashMap();

    public Config() {

    }

}
