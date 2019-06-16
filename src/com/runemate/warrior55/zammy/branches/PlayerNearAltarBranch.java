
package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.leafs.ClickAltar;
import com.runemate.warrior55.zammy.leafs.Run;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class PlayerNearAltarBranch extends BranchTask {
    
    private final Coordinate altarCoord = new Coordinate (2948, 3475);
    private final Run runToAltar;
    private final ClickAltar clickAltar = new ClickAltar();
    private final ZammyWineGrabber bot;
    
    public PlayerNearAltarBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        runToAltar = new Run(altarCoord, false, bot);
    }
    
    @Override
    public TreeTask successTask() {
        return clickAltar;
    }

    @Override
    public TreeTask failureTask() {
        return runToAltar;
    }

    @Override
    public boolean validate() {
        return bot.getPlayer().distanceTo(altarCoord) <= 4;
    }
}
