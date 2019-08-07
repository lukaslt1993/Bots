package com.runemate.warrior55.tanner.leafs;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.tree.LeafTask;

public class OpenBank extends LeafTask {

    @Override
    public void execute() {
        if (Players.getLocal() != null && Players.getLocal().isVisible()) {
            if (!Banks.getLoaded().isEmpty()) {
                Bank.open();
            } else {
                Environment.getBot().stop();
            }
        }
    }
}
