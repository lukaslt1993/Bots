
package com.runemate.warrior55.zammy.leafs;

import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.framework.tree.LeafTask;

public class ClickAltar extends LeafTask {
    
    @Override
    public void execute() {
        GameObjects.newQuery().names("Chaos altar").results().nearest().click();
    }
}
