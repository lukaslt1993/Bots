
package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.common.Stuff;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class RS3StuffCheckBranch extends BranchTask {
    
    private final ZammyWineGrabber bot;
    private final PlayerInBankBranch playerInBankBranch;
    private final EnoughHealthBranch enoughHealthBranch;
    
    public RS3StuffCheckBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        enoughHealthBranch = new EnoughHealthBranch(bot);
        playerInBankBranch = new PlayerInBankBranch(bot);
    }
    
    @Override
    public TreeTask successTask() {
        return enoughHealthBranch;
    }

    @Override
    public TreeTask failureTask() {
        return playerInBankBranch;
    }

    @Override
    public boolean validate() {
        return Stuff.check(false) && !(Players.getLocal().distanceTo(bot.getSpotCoord()) >= 35 && Inventory.newQuery().names(bot.getWineName()).results().size() > 0);
    }
}
