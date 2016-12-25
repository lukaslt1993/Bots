
package com.runemate.warrior55.tanner.leafs;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;

public class WithdrawPreset extends LeafTask {

    @Override
    public void execute() {
        Bank.loadPreset(1);
        Execution.delayUntil(() -> !Bank.isOpen(), 2500);
    }

}
