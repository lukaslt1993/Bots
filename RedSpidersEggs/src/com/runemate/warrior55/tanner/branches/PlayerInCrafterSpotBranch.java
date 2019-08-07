
package com.runemate.warrior55.tanner.branches;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.tanner.leafs.RunToCrafterSpot;
import com.runemate.warrior55.tanner.leafs.HopWorld;

public class PlayerInCrafterSpotBranch extends BranchTask {

    private final HopWorld HOP_WORLD_LEAF;
    private final RunToCrafterSpot RUN_TO_CRAFTER_SPOT_LEAF;
    private final Coordinate crafter;
    
    public PlayerInCrafterSpotBranch(Coordinate crafter, int world) {
        this.crafter = crafter;
        HOP_WORLD_LEAF = new HopWorld(world);
        RUN_TO_CRAFTER_SPOT_LEAF = new RunToCrafterSpot(crafter);
    }

    @Override
    public TreeTask successTask() {
        return HOP_WORLD_LEAF;
    }

    @Override
    public TreeTask failureTask() {
        return RUN_TO_CRAFTER_SPOT_LEAF;
    }

    @Override
    public boolean validate() {
        return Players.getLocal().distanceTo(crafter) <= 5;
    }

}
