package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.warrior55.zammy.leafs.ThatWillDo;

public class RootBranch extends BranchTask {

    private final ZammyWineGrabber bot;
    private final HopWorldBranch hopWorldBranch;
    private final ThatWillDo thatWillDo = new ThatWillDo();
    private Player player;

    public RootBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        hopWorldBranch = new HopWorldBranch(bot);
    }

    @Override
    public TreeTask successTask() {
        bot.setPlayer(player);
        return hopWorldBranch;
    }

    @Override
    public TreeTask failureTask() {
        return thatWillDo;
    }

    @Override
    public boolean validate() {
        player = Players.getLocal();
        return player != null && player.isVisible();
    }
}
