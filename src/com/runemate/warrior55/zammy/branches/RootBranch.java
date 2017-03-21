package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.warrior55.zammy.leafs.EnterTheVoid;

public class RootBranch extends BranchTask {

    private final ZammyWineGrabber bot;
    private final FailedPicksBranch failedPicksBranch;
    private final EnterTheVoid enterTheVoid = new EnterTheVoid();

    public RootBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        failedPicksBranch = new FailedPicksBranch(bot);
    }

    @Override
    public TreeTask successTask() {
        return failedPicksBranch;
    }

    @Override
    public TreeTask failureTask() {
        return enterTheVoid;
    }

    @Override
    public boolean validate() {
        Player player = Players.getLocal();
        return player != null && player.isVisible();
    }
}
