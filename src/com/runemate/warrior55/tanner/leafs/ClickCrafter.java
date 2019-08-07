package com.runemate.warrior55.tanner.leafs;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.rs3.local.hud.interfaces.MakeXInterface;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;

public class ClickCrafter extends LeafTask {

    @Override
    public void execute() {
        GameObject crafter = GameObjects.newQuery().names("Portable crafter").results().nearest();
        if (crafter.click() || crafter.interact("Tan Leather")) {
            Execution.delayUntil(() -> MakeXInterface.isOpen(), 2500);
        }
    }
}
