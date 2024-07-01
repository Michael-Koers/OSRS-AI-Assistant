package com.aiassistant.model;

import lombok.Getter;
import net.runelite.http.api.worlds.WorldType;

@Getter
public class World {

    final int world;
    final int players;
    final String activity;
    final boolean f2p;

    public World(net.runelite.http.api.worlds.World world) {
        this.world = world.getId();
        this.players = world.getPlayers();
        this.activity = world.getActivity();
        this.f2p = !world.getTypes().contains(WorldType.MEMBERS);
    }
}
