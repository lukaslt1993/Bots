package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.common.Constants;
import com.runemate.warrior55.summoner.tasks.common.Utils;

public class BankTask extends Task {

    private final Summoner bot;

    private final String scrollName;

    private final String pouchName;

    private final String[] itemToKeepNames;

    private final Validators validators;
    
    private LocatableEntity spawningBank;

    public BankTask(Summoner s) {
        bot = s;
        validators = new Validators(bot);
        scrollName = bot.getScrollName();
        pouchName = bot.getPouchName();
        itemToKeepNames = new String[]{"Summoning potion (4)", "Summoning potion (3)", "Summoning potion (2)", "Summoning potion (1)", scrollName};
    }

    @Override
    public boolean validate() {
        return Players.getLocal() != null && validators.isBank();
    }

    @Override
    public void execute() {

        if (Bank.isOpen() || openBank()) {
            //setFields();

            if (bot.isUsingPresets()) {
                withdrawPreset();

            } else if (depositInventoryOrContinue() && withdrawScrollOrContinue()
                    && withdrawPotOrContinue() && withdrawPouchOrContinue()
                    && withdrawEggsOrContinue()) {

                Bank.close();
            }

        } else {
            Utils.walk(getSpawningBank().getPosition(), bot);
        }
    }

    private boolean openBank() {
        if (spawningBank != null && (spawningBank.isVisible() || Camera.turnTo(spawningBank))) {

            if (/*Bank.open() ||*/spawningBank.interact(Constants.BANK_PATTERN/*, bank.getDefinition().getName()*/)) {
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
                bot.showAndLogAlert("Out of scrolls");
                bot.stop();

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
                bot.showAndLogAlert("Out of summoning potions and points");
                bot.stop();
            }

        } else if (!Inventory.containsAnyOf(Constants.POTION_NAMES[0], Constants.POTION_NAMES[1]) && Inventory.getQuantity(Constants.POTION_NAMES) < 4) {
            return Bank.withdraw(pots.sort((pot1, pot2) -> pot1.getDefinition().getName().compareTo(pot2.getDefinition().getName())).last(), 2);
        }

        return true;
    }

    private boolean withdrawPouchOrContinue() {
        if (Players.getLocal().getFamiliar() == null && !Inventory.containsAnyOf(pouchName)) {
            SpriteItemQueryResults pouches = Bank.getItems(pouchName);

            if (!pouches.isEmpty()) {
                return Bank.withdraw(pouches.first(), 1);

            } else {
                bot.showAndLogAlert("Out of pouches");
                bot.stop();
            }
        }

        return true;
    }

    private boolean withdrawEggsOrContinue() {
        if (pouchName.equals("Spirit cobra pouch") && !Inventory.containsAnyOf("Egg")) {
            SpriteItemQueryResults eggs = Bank.getItems("Egg");

            if (eggs.isEmpty()) {
                bot.showAndLogAlert("Out of eggs");
                bot.stop();

            } else {
                return Bank.withdraw(eggs.first(), 0);
            }
        }

        return true;
    }

    private void withdrawPreset() {
        if (Players.getLocal().getFamiliar() == null || Summoning.getMinutesRemaining() < 1) {
            SpriteItemQueryResults pouches = Bank.getItems(pouchName);

            if (!pouches.isEmpty()) {
                loadPresetAndWait(2);

            } else {
                bot.showAndLogAlert("Out of pouches");
                bot.stop();
            }

        } else {
            loadPresetAndWait(1);
        }
    }

    private void loadPresetAndWait(int preset) {
        if (Bank.loadPreset(preset, false)) {

            Execution.delayUntil(() -> !Bank.isOpen(), 2000);

            if (!Bank.isOpen()) {
                Execution.delayUntil(() -> !validate(), 5000);

                if (validate()) {
                    bot.showAndLogAlert("Out of stuff");
                    bot.stop();
                }
            }
        }
    }

    private LocatableEntity getSpawningBank() {
        Player player = Players.getLocal();
        if (spawningBank == null /*|| player.distanceTo(spawningBank) >= 25*/) {
            LocatableEntityQueryResults banks = GameObjects.newQuery().actions(Constants.BANK_PATTERN).surroundingsReachable().results();
            banks.add(GameObjects.newQuery().actions(Constants.BANK_PATTERN).reachable().results().nearest());
            banks.add(Npcs.newQuery().actions(Constants.BANK_PATTERN).reachable().results().nearest());
            banks.add(Npcs.newQuery().actions(Constants.BANK_PATTERN).surroundingsReachable().results().nearest());
            banks.add(GameObjects.newQuery().names("Bank chest").results().nearest());
            spawningBank = banks.nearest();
            if (spawningBank != null /*&& player.distanceTo(spawningBank) < 25*/) {
                //bot.setInitialPos(player.getPosition());
                return spawningBank;
            } else {
                bot.showAndLogAlert("No bank(er) nearby, try to run closer to bank(er)");
                bot.stop();
                return null;
            }
        } else {
            return spawningBank;
        }
    }

    /*private void setFields() {
     scrollName = bot.getScrollName();
     pouchName = bot.getPouchName();
     itemToKeepNames[4] = scrollName;
     }*/
}
