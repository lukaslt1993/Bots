
package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.leafs.ClickAltar;
import com.runemate.warrior55.zammy.leafs.Run;

public class PlayerNearAltarBranch extends BranchTask {
    
    private final Coordinate altarCoord = new Coordinate (2948, 3475);
    private final Run runToAltar = new Run(altarCoord, false);
    private final ClickAltar clickAltar = new ClickAltar();
    
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
        return Players.getLocal().distanceTo(altarCoord) <= 4;
    }
}
