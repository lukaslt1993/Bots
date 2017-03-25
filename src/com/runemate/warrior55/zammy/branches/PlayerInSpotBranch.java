package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.leafs.Run;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class PlayerInSpotBranch extends BranchTask {

    private final TelegrabClickedBranch telegrabClickedBranch;
    private Run runToSpot;
    private final ZammyWineGrabber bot;
    private Player player;

    public PlayerInSpotBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        telegrabClickedBranch = new TelegrabClickedBranch(bot);
    }

    @Override
    public TreeTask successTask() {
        Execution.delayUntil(() -> !player.isMoving());
        return telegrabClickedBranch;
    }

    @Override
    public TreeTask failureTask() {
        if (player.distanceTo(bot.getSafeSpotCoord()) > 4) {
            runToSpot = new Run(bot.getSpotCoord(), false);
        } else {
            runToSpot = new Run(new Coordinate[] {bot.getSafeSpotHalfwayCoord(), bot.getSpotCoord()});
        }
        return runToSpot;
    }

    @Override
    public boolean validate() {
        player = bot.getPlayer();
        return player.distanceTo(bot.getSpotCoord()) <= 4;
    }
}
