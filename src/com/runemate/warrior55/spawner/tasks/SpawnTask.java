
package com.runemate.warrior55.spawner.tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.spawner.main.EggSpawner;
import com.runemate.warrior55.spawner.tasks.common.Constants;
import com.runemate.warrior55.spawner.tasks.common.Utils;

public class SpawnTask extends Task {
    
    private final EggSpawner BOT = (EggSpawner) Environment.getBot();
    
    private final Validators VALIDATORS = new Validators();

    @Override
    public boolean validate() {
        return Players.getLocal() != null && VALIDATORS.isSpawn();
    }

    @Override
    public void execute() {
        if (BOT.getNoDropCounter() > 10) {
            Utils.walk(Constants.BANK_COORD);
        }
        
        Summoning.FamiliarOption.getLeftClick().select();
    }
}
