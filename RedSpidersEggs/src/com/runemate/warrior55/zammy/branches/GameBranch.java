package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class GameBranch extends BranchTask {

    private final ZammyWineGrabber bot;
    private final OSRSCheckRunesBranch osrsCheckRunesBranch;
    private final InCombatBranch inCombatBranch;

    public GameBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        osrsCheckRunesBranch = new OSRSCheckRunesBranch(bot);
        inCombatBranch = new InCombatBranch(bot);
    }

    @Override
    public TreeTask successTask() {
        return osrsCheckRunesBranch;
    }

    @Override
    public TreeTask failureTask() {
        return inCombatBranch;
    }

    @Override
    public boolean validate() {
        return Environment.isOSRS();
    }
}
