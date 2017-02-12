package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.common.Constants;
import com.runemate.warrior55.summoner.tasks.common.Utils;

public class BankTask extends Task {

    private final Summoner bot = (Summoner) Environment.getBot();

    private String scrollName = bot.getScrollName();

    private String pouchName = bot.getPouchName();

    private final String[] itemToKeepNames = new String[]{"Summoning potion (4)", "Summoning potion (3)", "Summoning potion (2)", "Summoning potion (1)", scrollName};

    private final Validators validators = new Validators();

    @Override
    public boolean validate() {
        return Players.getLocal() != null && validators.isBank();
    }

    @Override
    public void execute() {
        if (Bank.isOpen() || openBank()) {
            setFields();

            if (bot.isUsingPresets()) {
                withdrawPreset();

            } else if (depositInventoryOrContinue() && withdrawScrollOrContinue()
                    && withdrawPotOrContinue() && withdrawPouchOrContinue()
                    && withdrawEggsOrContinue()) {

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
                return Bank.depositAllExcept(itemToKeepNames);

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
        if (Players.getLocal().getFamiliar() == null && !Inventory.containsAnyOf(pouchName)) {
            SpriteItemQueryResults pouches = Bank.getItems(pouchName);

            if (!pouches.isEmpty()) {
                return Bank.withdraw(pouches.first(), 1);

            } else {
                Environment.getBot().stop();
            }
        }

        return true;
    }

    private boolean withdrawEggsOrContinue() {
        if (pouchName.equals("Spirit cobra pouch") && !Inventory.containsAnyOf("Egg")) {
            SpriteItemQueryResults eggs = Bank.getItems("Egg");

            if (eggs.isEmpty()) {
                Environment.getBot().stop();

            } else {
                return Bank.withdraw(eggs.first(), 0);
            }
        }

        return true;
    }

    private void withdrawPreset() {
        if (Players.getLocal().getFamiliar() == null && !Inventory.containsAnyOf(pouchName)) {
            SpriteItemQueryResults pouches = Bank.getItems(pouchName);

            if (!pouches.isEmpty()) {
                Bank.loadPreset(2, true);

            } else {
                Environment.getBot().stop();
            }

        } else {
            Bank.loadPreset(1, true);
        }
    }

    private void setFields() {
        scrollName = bot.getScrollName();
        pouchName = bot.getPouchName();
        itemToKeepNames[4] = scrollName;
    }
}
