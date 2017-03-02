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
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.queries.LocatableEntityQueryBuilder;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.common.Constants;
import com.runemate.warrior55.summoner.tasks.common.Utils;
import java.util.regex.Pattern;

public class BankTask extends Task {

    private final Summoner bot;

    private final String scrollName;

    private final String pouchName;

    private final String[] itemToKeepNames;

    private final Validators validators;

    private LocatableEntity spawningBank;

    public final Pattern bankPattern = Pattern.compile("^.*Bank.*$");

    public final Pattern actionPattern = Pattern.compile("^.*Bank.*$|^Use$");

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
            LocatableEntity le = getSpawningBank();

            if (le != null) {
                Coordinate c = le.getPosition();

                if (c != null) {
                    Utils.walk(c, bot);

                } else {
                    bot.showAndLogAlert("Can not walk to bank, try to start closer to bank or find another");
                    bot.stop();
                }
            }
        }
    }

    private boolean openBank() {
        if (spawningBank != null && (spawningBank.isVisible() || Camera.turnTo(spawningBank))) {

            if (spawningBank.interact(actionPattern/*, bank.getDefinition().getName()*/)) {
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
            boolean playerVisible = player.isVisible();
            if (!playerVisible) {
                Execution.delayUntil(() -> player.isVisible());
            }
            int oldResultsCount = -1, resultsCount = -1;
            LocatableEntityQueryResults bankEntities;
            for (int i = 0; i < 5; i++) {
                oldResultsCount = resultsCount;
                LocatableEntityQueryBuilder banksBuilder = GameObjects.newQuery().actions(bankPattern);
                LocatableEntityQueryBuilder bankersBuilder = Npcs.newQuery().actions(bankPattern);
                //LocatableEntityQueryBuilder bankChestsBuilder = GameObjects.newQuery().names("Bank chest");
                bankEntities = (LocatableEntityQueryResults) banksBuilder.surroundingsReachable().results();
                addIfNotNull(bankEntities, ((LocatableEntityQueryResults) banksBuilder.reachable().results()).nearest());
                addIfNotNull(bankEntities, ((LocatableEntityQueryResults) bankersBuilder.reachable().results()).nearest());
                addIfNotNull(bankEntities, ((LocatableEntityQueryResults) bankersBuilder.surroundingsReachable().results()).nearest());
                addIfNotNull(bankEntities, ((LocatableEntityQueryResults) GameObjects.newQuery().names("Bank chest").reachable().results()).nearest());
                addIfNotNull(bankEntities, ((LocatableEntityQueryResults) GameObjects.newQuery().names("Bank chest").surroundingsReachable().results()).nearest());
                resultsCount = bankEntities.size();
                if (resultsCount == oldResultsCount || playerVisible) {
                    spawningBank = bankEntities.nearest();
                    break;
                }
                Execution.delay(1000);
            }
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

    private void addIfNotNull(LocatableEntityQueryResults leqr, LocatableEntity le) {
        if (le != null) {
            leqr.add(le);
        }
    }

    /*private void setFields() {
     scrollName = bot.getScrollName();
     pouchName = bot.getPouchName();
     itemToKeepNames[4] = scrollName;
     }*/
}
