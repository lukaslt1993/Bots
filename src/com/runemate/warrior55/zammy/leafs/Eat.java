
package com.runemate.warrior55.zammy.leafs;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.tree.LeafTask;

public class Eat extends LeafTask {
    
    @Override
    public void execute () {
        Inventory.newQuery().actions("Eat").results().random().click();
    }
}
