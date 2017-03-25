
package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.leafs.Run;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class PlayerInSafeSpotBranch extends BranchTask {
    
    private final ZammyWineGrabber bot;
    private final Run runToSafeSpot;
    private final Run runFurther;
    
    public PlayerInSafeSpotBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        runToSafeSpot = new Run(new Coordinate[] {bot.getSafeSpotHalfwayCoord(), bot.getSafeSpotCoord()});
        runFurther = new Run(bot.getFurtherSafeSpotCoord(), false);
    }
    
    @Override
    public TreeTask successTask() {
        //runFurther = new Run(new Coordinate(2948, Players.getLocal().getPosition().getY() - 10), true);
        return runFurther;
    }

    @Override
    public TreeTask failureTask() {
        return runToSafeSpot;
    }

    @Override
    public boolean validate() {
        return bot.getPlayer().distanceTo(bot.getSafeSpotCoord()) <= 4;
    }
}
