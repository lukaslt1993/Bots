
package com.runemate.warrior55.spawner.tasks;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.spawner.tasks.common.Constants;

public class RestoreTask extends Task {
    
    private final Validators VALIDATORS = new Validators();

    @Override
    public boolean validate() {
        return Players.getLocal() != null && VALIDATORS.isRestore();
    }

    @Override
    public void execute() {
        SpriteItem pot = Inventory.getItems(Constants.POTION_NAMES).random();

        if (pot != null) {
            pot.click();

        } else {
            Execution.delayUntil(() -> !validate(), 20000);
        }
    }
}
