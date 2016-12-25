
package com.runemate.warrior55.tanner.leafs;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.script.framework.tree.LeafTask;
import com.runemate.warrior55.tanner.common.Constants;

public class ClickHide extends LeafTask {

    @Override
    public void execute() {
        SpriteItemQueryResults hides = Inventory.getItems(Constants.PATTERN);
        
        if (!hides.isEmpty()) {
            hides.random().click();
        }
    }
}
