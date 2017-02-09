package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.common.Constants;
import com.runemate.warrior55.summoner.tasks.common.Utils;

public class SpawnTask extends Task {

    private final Summoner BOT = (Summoner) Environment.getBot();

    private final Validators VALIDATORS = new Validators();

    @Override
    public boolean validate() {
        return Players.getLocal() != null && VALIDATORS.isSpawn();
    }

    @Override
    public void execute() {
        
        ActionBar.Slot firstSlot = ActionBar.getFilledSlots().first();
        
        if (firstSlot != null) {
            firstSlot.activate(false);
        }
        
        if (!BOT.getPouchName().equals("Spirit cobra pouch")) {

            if (BOT.getNoDropCounter() > 10) {
                Utils.walk(Constants.BANK_COORD);
            }
            
        } else {
            SpriteItemQueryResults eggs = Inventory.newQuery().names("Egg").results();
            
            if (!eggs.isEmpty()) {
                int eggsCount = Inventory.getQuantity("Egg");
                
                if (eggs.random().click()) {
                    Execution.delay(400);
                }
            }
        }
    }
}
