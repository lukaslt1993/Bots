
package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.leafs.HopWorld;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class HopWorldBranch extends BranchTask {

    private final ZammyWineGrabber bot;
    private final HopWorld hopWorld;
    private final GameBranch gameBranch;

    public HopWorldBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        hopWorld = new HopWorld(bot);
        gameBranch = new GameBranch(bot);
    }

    @Override
    public TreeTask successTask() {
        return hopWorld;
    }

    @Override
    public TreeTask failureTask() {
        return gameBranch;
    }

    @Override
    public boolean validate() {
        bot.setIsInCombat(!Npcs.newQuery().targeting(bot.getPlayer()).results().isEmpty());
        return bot.getFailedPicks() >= bot.getHopAfterFails() && !bot.isInCombat();
    }
}
