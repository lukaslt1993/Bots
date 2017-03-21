package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.leafs.Run;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class PlayerInSpotBranch extends BranchTask {

    private final TelegrabClickedBranch telegrabClickedBranch;
    private final Run runToSpot;
    private final ZammyWineGrabber bot;

    public PlayerInSpotBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        telegrabClickedBranch = new TelegrabClickedBranch(bot);
        if (Players.getLocal().distanceTo(bot.getSafeSpotCoord()) > 4) {
            runToSpot = new Run(bot.getSpotCoord(), false);
        } else {
            runToSpot = new Run(new Coordinate[] {bot.getSafeSpotHalfwayCoord(), bot.getSpotCoord()});
        }
    }

    @Override
    public TreeTask successTask() {
        return telegrabClickedBranch;
    }

    @Override
    public TreeTask failureTask() {
        return runToSpot;
    }

    @Override
    public boolean validate() {
        return Players.getLocal().distanceTo(bot.getSpotCoord()) <= 4;
    }
}
