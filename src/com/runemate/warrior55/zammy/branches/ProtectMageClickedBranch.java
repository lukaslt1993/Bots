
package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.rs3.local.hud.Powers;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.leafs.ClickProtectMage;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class ProtectMageClickedBranch extends BranchTask {
    
    private final ClickProtectMage clickProtectMage;
    private final PlayerInSafeSpotBranch playerInSafeSpotBranch ;
    private final ZammyWineGrabber bot;
    
    public ProtectMageClickedBranch (ZammyWineGrabber zwg) {
        bot = zwg;
        clickProtectMage = new ClickProtectMage(bot);
        playerInSafeSpotBranch = new PlayerInSafeSpotBranch(bot);
    }
    
    @Override
    public TreeTask successTask() {
        return playerInSafeSpotBranch;
    }

    @Override
    public TreeTask failureTask() {
        return clickProtectMage;
    }

    @Override
    public boolean validate() {
        /*if (Environment.isOSRS()) {
            return Prayer.PROTECT_FROM_MAGIC.isActivated();
        } else {*/
            return Powers.Prayer.PROTECT_FROM_MAGIC.isActivated();
        //}
    }
}
