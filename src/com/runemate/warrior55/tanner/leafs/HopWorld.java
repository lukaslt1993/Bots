package com.runemate.warrior55.tanner.leafs;

import com.runemate.game.api.hybrid.local.Worlds;
import com.runemate.game.api.hybrid.local.hud.interfaces.WorldHop;
import com.runemate.game.api.script.framework.tree.LeafTask;

public class HopWorld extends LeafTask {

    private final int world;

    public HopWorld(int world) {
        this.world = world;
    }

    @Override
    public void execute() {
        if (Worlds.getCurrent() != world) {
            WorldHop.hopTo(world);
        }
    }
}
