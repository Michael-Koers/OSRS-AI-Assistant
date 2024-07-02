package com.aiassistant.model;

import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldPoint;

public enum Location {

    GRAND_EXCHANGE(new WorldPoint(3164, 3489, 0), ItemID.COIN_POUCH),
    VARROCK(new WorldPoint(3212, 3428, 0), ItemID.VARROCK_TELEPORT),
    FALADOR(new WorldPoint(2975, 3340, 0), ItemID.FALADOR_TELEPORT);


    public final WorldPoint worldPoint;
    public final Integer itemID;

    Location(WorldPoint wp, int itemID) {
        this.worldPoint = wp;
        this.itemID = itemID;
    }
}
