package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.leafs.Run;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class PlayerInBankBranch extends BranchTask {

    private final ZammyWineGrabber bot;
    private final Coordinate bankCoord = new Coordinate(2947, 3368);
    private final BankOpenBranch bankOpenBranch;
    private final Run runToBank;

    public PlayerInBankBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        bankOpenBranch = new BankOpenBranch(bot);
        runToBank = new Run(bankCoord, false, bot);
    }

    @Override
    public TreeTask successTask() {
        return bankOpenBranch;
    }

    @Override
    public TreeTask failureTask() {
        return runToBank;
    }

    @Override
    public boolean validate() {
        return bot.getPlayer().distanceTo(bankCoord) <= 4;
    }
}
