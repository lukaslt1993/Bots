
package com.runemate.warrior55.tanner.leafs;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;

public class WithdrawPreset extends LeafTask {

    @Override
    public void execute() {
        // TODO: add money check
        Bank.loadPreset(1, true);
        Execution.delayUntil(() -> !Bank.isOpen(), 2500);
        
        if (!Bank.isOpen() && Inventory.isEmpty()) {
            Environment.getBot().stop();
        }
    }

}
