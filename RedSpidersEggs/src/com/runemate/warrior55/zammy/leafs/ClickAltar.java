
package com.runemate.warrior55.zammy.leafs;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.framework.tree.LeafTask;

public class ClickAltar extends LeafTask {
    
    @Override
    public void execute() {
        GameObject altar = GameObjects.newQuery().names("Chaos altar").results().nearest();
        if (altar != null) {
            altar.click();
        }
    }
}
