package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.LootInventory;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.common.Utils;

public class PickTask extends Task {

    private int failedPicksCounter = 0;

    private final Validators validators;
    private final Summoner bot;

    public PickTask(Summoner s) {
        bot = s;
        validators = new Validators(bot);
    }

    @Override
    public boolean validate() {
        if (Players.getLocal() != null) {

            if (validators.isPick()) {
                bot.setNoDropCounter(0);
                return true;
            }

            if (bot.getAllLoot() != null) {
                
                if (bot.getType().equals("Spawn")
                        && !Bank.isOpen()
                        && bot.getAllLoot().isEmpty()
                        && !bot.getPouchName().equals("Spirit cobra pouch")) {

                    bot.setNoDropCounter(bot.getNoDropCounter() + 1);
                }
            }

        }

        return false;
    }

    @Override
    public void execute() {
        if (bot.isLootAll()) {
            pickWithLootAll();

        } else {
            pickWithoutLootAll();
        }
    }

    private void pickWithLootAll() {
        GroundItem loot = bot.getAllLoot()./*first()*/nearest();
        walkIfFailing(loot);

        if (loot.isVisible() || Camera.turnTo(loot)) {

            if (loot.interact("Take")) {
                failedPicksCounter = 0;
                Execution.delayUntil(() -> LootInventory.isOpen(), 3500);

            } else {
                failedPicksCounter++;
            }

        } else {
            failedPicksCounter++;
        }

        if (LootInventory.isOpen()) {
            LootInventory.takeAll();
            LootInventory.close();
        }
    }

    private void pickWithoutLootAll() {
        GroundItem loot = bot.getLoot();
        walkIfFailing(loot);

        if (loot.isVisible() || Camera.turnTo(loot)) {
            int usedSlots = Inventory.getUsedSlots();

            if (loot.interact("Take", loot.getDefinition().getName())) {
                Execution.delayUntil(() -> Inventory.getUsedSlots() > usedSlots, 500, 5000);
                failedPicksCounter = 0;

            } else {
                failedPicksCounter++;
            }

        } else {
            failedPicksCounter++;
        }
    }

    private void walkIfFailing(GroundItem loot) {

        if (failedPicksCounter > 5) {
            Coordinate c = loot.getPosition();

            if (c != null) {
                Utils.walk(loot.getPosition(), bot);

            } else {
                throw new RuntimeException("Didn't get the coordinate of drop, so stopped; bot was running? - " + bot.isRunning());
            }
        }
    }

}
