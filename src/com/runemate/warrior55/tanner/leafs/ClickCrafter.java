
package com.runemate.warrior55.tanner.leafs;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.rs3.local.hud.interfaces.MakeXInterface;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;

public class ClickCrafter extends LeafTask {

    @Override
    public void execute() {
        LocatableEntityQueryResults <GameObject> crafters = GameObjects.newQuery().names("Portable crafter").results();
        
        if (!crafters.isEmpty()) {
            crafters.nearest().click();
            Execution.delayUntil(() -> MakeXInterface.isOpen(), 2500);
        }
    }
}
