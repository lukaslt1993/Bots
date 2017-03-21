
package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.common.Stuff;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class OSRSStuffCheckBranch extends BranchTask {
    
    private final ZammyWineGrabber bot;
    private final PlayerInBankBranch playerInBankBranch;
    private final PlayerInSpotBranch playerInSpotBranch;
    
    public OSRSStuffCheckBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        playerInBankBranch = new PlayerInBankBranch(bot);
        playerInSpotBranch = new PlayerInSpotBranch(bot);
    }
    
    @Override
    public TreeTask successTask() {
        return playerInSpotBranch;
    }

    @Override
    public TreeTask failureTask() {
        return playerInBankBranch;
    }

    @Override
    public boolean validate() {
        return !Inventory.isFull() && Stuff.check(false);
    }
}
