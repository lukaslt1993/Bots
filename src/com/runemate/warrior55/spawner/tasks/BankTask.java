
package com.runemate.warrior55.spawner.tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.rs3.region.Familiars;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.warrior55.spawner.main.EggSpawner;
import com.runemate.warrior55.spawner.tasks.common.Constants;
import com.runemate.warrior55.spawner.tasks.common.Utils;

public class BankTask extends Task {
    
    private final EggSpawner BOT = (EggSpawner) Environment.getBot();
    
    private String scrollName = BOT.getScrollName();
    
    private String pouchName = BOT.getPouchName();

    private final String[] ITEM_TO_KEEP_NAMES = new String[]{"Summoning potion (4)", "Summoning potion (3)", "Summoning potion (2)", "Summoning potion (1)", scrollName};
    
    private final Validators VALIDATORS = new Validators();

    @Override
    public boolean validate() {
        return VALIDATORS.isBank();
    }

    @Override
    public void execute() {
        if (Bank.isOpen() || openBank()) {
            setFields();
            
            if (depositInventoryOrContinue() && withdrawScrollOrContinue()
                    && withdrawPotOrContinue() && withdrawPouchOrContinue()) {

                Bank.close();
            }
            
        } else {
            Utils.walk(Constants.BANK_COORD);
        }
    }

    private boolean openBank() {
        GameObject bank = GameObjects.newQuery().names("Well of Goodwill").results().nearest();

        if (bank != null && (bank.isVisible() || Camera.turnTo(bank))) {

            if (bank.interact("Open Bank", bank.getDefinition().getName())) {
                Execution.delayUntil(() -> Bank.isOpen(), 500, 5000);
            }
        }

        return Bank.isOpen();
    }

    private boolean depositInventoryOrContinue() {
        if (Inventory.getEmptySlots() < 20) {

            if (Inventory.getQuantity(Constants.POTION_NAMES) < 8) {
                return Bank.depositAllExcept(ITEM_TO_KEEP_NAMES);

            } else {
                return Bank.depositAllExcept(scrollName);
            }
        }

        return true;
    }

    private boolean withdrawScrollOrContinue() {
        if (!Inventory.containsAnyOf(scrollName)) {
            SpriteItemQueryResults scrolls = Bank.getItems(scrollName);
            
            if (scrolls.isEmpty()) {
                Environment.getBot().stop();

            } else {
                return Bank.withdraw(scrolls.first(), 0);
            }
        }

        return true;
    }

    private boolean withdrawPotOrContinue() {
        SpriteItemQueryResults pots = Bank.getItems(Constants.POTION_NAMES);
        
        if (pots.isEmpty()) {
            
            if (Summoning.getPoints() < 1) {
                Environment.getBot().stop();     
            }
            
        } else if (!Inventory.containsAnyOf(Constants.POTION_NAMES[0], Constants.POTION_NAMES[1])) {
            return Bank.withdraw(pots.first(), 2);
        }

        return true;
    }

    private boolean withdrawPouchOrContinue() {
        if (Familiars.getLoaded().isEmpty() && !Inventory.containsAnyOf(pouchName)) {
            SpriteItemQueryResults pouches = Bank.getItems(pouchName);
            
            if (!pouches.isEmpty()) {
                return Bank.withdraw(pouches.first(), 1);

            } else {
                Environment.getBot().stop();
            }
        }
 
        return true;
    }
    
    private void setFields() {
        scrollName = BOT.getScrollName();
        pouchName = BOT.getPouchName();
        ITEM_TO_KEEP_NAMES[4] = scrollName;
    }
}
