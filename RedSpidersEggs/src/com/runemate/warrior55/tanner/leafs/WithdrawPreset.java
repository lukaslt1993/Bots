
package com.runemate.warrior55.tanner.leafs;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;
import com.runemate.warrior55.tanner.common.Constants;
import com.runemate.warrior55.tanner.main.PortableCrafter;

public class WithdrawPreset extends LeafTask {

    @Override
    public void execute() {
        // TODO: add money check
        PortableCrafter bot = (PortableCrafter) Environment.getBot();
        if (GameObjects.newQuery().names("Portable crafter").results().nearest() != null) {
            Bank.loadPreset(1, true);
        } else if (bot.canUseOwnCrafter() && hasCrafter()) {
            Bank.loadPreset(2, true);
        } else {
            bot.setCanUseOwnCrafter(false);
            Bank.loadPreset(1);
        }        
        Execution.delayUntil(() -> !Bank.isOpen(), 2500);
        
        if (!Bank.isOpen() && !Inventory.contains(Constants.PATTERN)) {
            Environment.getBot().stop();
        }
    }
    
    private boolean hasCrafter() {
        return Bank.contains("Portable crafter") || Inventory.contains("Portable crafter");
    }

}
