
package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.common.Constants;
import com.runemate.warrior55.summoner.tasks.common.Utils;

public class BankBarbarianTask extends Task {
    
    private final Validators validators;
    private final Summoner bot;
    
    public BankBarbarianTask(Summoner s) {
        bot = s;
        validators = new Validators(bot);
    }

    @Override
    public boolean validate() {
        return validators.isBankBarbarian();
    }

    @Override
    public void execute() {
        Player player = Players.getLocal();
        
        if (player.distanceTo(Constants.BARBARIAN_BANK_COORD) > 800) {
            ActionBar.Slot firstSlot = ActionBar.getFilledSlots().first();
            
            if (firstSlot != null && firstSlot.getComponent().interact("Teleport to Daemonheim")) {
                Execution.delayUntil(() -> player.distanceTo(Constants.BARBARIAN_BANK_COORD) < 200, 10000);
            }
            
        } else if (!Bank.isOpen()) {
            Npc banker = Npcs.newQuery().names("Fremennik banker").results().nearest();
            
            if (banker == null || player.distanceTo(banker) > 5 || !banker.click()) {
                Utils.walk(Constants.BARBARIAN_BANK_COORD);   
            }
            
            Execution.delayUntil(() -> !player.isMoving(), 1000, 8000);
            
        } else if (!Inventory.containsAnyOf("Spirit kyatt pouch") && Summoning.getMinutesRemaining() < 1) {
            SpriteItemQueryResults pouches = Bank.getItems("Spirit kyatt pouch");

            if (pouches.isEmpty()) {
                bot.showAndLogAlert("Out of pouches to summon spirit kyatt");
                bot.stop();

            } else {
                Utils.loadPresetAndWait(2);    
            }
            
        } else {
            Utils.loadPresetAndWait(1);
        }
    }  
}
