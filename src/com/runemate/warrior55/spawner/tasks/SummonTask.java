
package com.runemate.warrior55.spawner.tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.spawner.main.EggSpawner;

public class SummonTask extends Task {
    
    private final Validators VALIDATORS = new Validators();
    
    private final EggSpawner BOT = (EggSpawner) Environment.getBot();
    
    @Override
    public boolean validate() {
        return Players.getLocal() != null && VALIDATORS.isSummon();
    }

    @Override
    public void execute() {
        SpriteItem pouch = Inventory.getItems(BOT.getPouchName()).first();
        
        if (pouch != null && pouch.click()) {
            Execution.delayWhile(() -> validate(), 5000);
        }     
    }    
}
