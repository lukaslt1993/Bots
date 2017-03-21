package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class GameBranch extends BranchTask {

    private final ZammyWineGrabber bot;
    private final OSRSStuffCheckBranch osrsStuffCheckBranch;
    private final InCombatBranch inCombatBranch;

    public GameBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        osrsStuffCheckBranch = new OSRSStuffCheckBranch(bot);
        inCombatBranch = new InCombatBranch(bot);
    }

    @Override
    public TreeTask successTask() {
        return osrsStuffCheckBranch;
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
