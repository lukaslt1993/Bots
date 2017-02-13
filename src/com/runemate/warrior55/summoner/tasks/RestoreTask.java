
package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.common.Constants;

public class RestoreTask extends Task {
    
    private final Validators validators;
    private final Summoner bot;
    
    public RestoreTask(Summoner s) {
        bot = s;
        validators = new Validators(bot);
    }

    @Override
    public boolean validate() {
        return Players.getLocal() != null && validators.isRestore();
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
