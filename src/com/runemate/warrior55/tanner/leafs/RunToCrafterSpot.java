package com.runemate.warrior55.tanner.leafs;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;

public class RunToCrafterSpot extends LeafTask {

    private final Coordinate crafter;
    private Path path;

    public RunToCrafterSpot(Coordinate crafter) {
        this.crafter = crafter;
    }

    @Override
    public void execute() {
        if (path == null) {
            path = Traversal.getDefaultWeb().getPathBuilder().buildTo(crafter);
            if (path == null) {
                path = BresenhamPath.buildTo(crafter);
            }
        }
        if (path != null) {
            while (Players.getLocal().distanceTo(crafter) > 5) {
                Execution.delayUntil(() -> path.step(false), 15000);
            }
        }
    }
}
