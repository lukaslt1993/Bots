package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.Environment;
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

    private final Validators VALIDATORS = new Validators();

    private final Summoner BOT = (Summoner) Environment.getBot();

    @Override
    public boolean validate() {
        if (Players.getLocal() != null) {

            if (VALIDATORS.isPick()) {
                BOT.setNoDropCounter(0);
                return true;
            }

            if (BOT.getType().equals("Spawn") && !Bank.isOpen() && !BOT.isLootAll() && !BOT.getPouchName().equals("Spirit cobra pouch")) {
                BOT.setNoDropCounter(BOT.getNoDropCounter() + 1);
            }
        }

        return false;
    }

    @Override
    public void execute() {
        if (BOT.isLootAll()) {
            pickWithLootAll();

        } else {
            pickWithoutLootAll();
        }
    }

    private void pickWithLootAll() {
        if (BOT.getAllLoot().first().interact("Take")) {
            Execution.delayUntil(() -> LootInventory.isOpen(), 3500);    
        }

        if (LootInventory.isOpen()) {
            LootInventory.takeAll();
            LootInventory.close();
        }
    }

    private void pickWithoutLootAll() {
        GroundItem loot = BOT.getLoot();

        if (failedPicksCounter > 5) {
            Coordinate c = loot.getPosition();

            if (c != null) {
                Utils.walk(loot.getPosition());

            } else {
                throw new RuntimeException("Didn't get the coordinate of drop, so stopped");
            }
        }

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

}
