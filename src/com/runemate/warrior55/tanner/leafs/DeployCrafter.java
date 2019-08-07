
package com.runemate.warrior55.tanner.leafs;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;

public class DeployCrafter extends LeafTask {

    @Override
    public void execute() {
        if (Inventory.getItems("Portable crafter").first().click()) {
            Execution.delayUntil(() -> !ChatDialog.getOptions().isEmpty(), 5000);
            
            if (!ChatDialog.getOptions().isEmpty()) {
                Keyboard.typeKey('1');
                Execution.delay(2000);
            }
        }
    }
}
