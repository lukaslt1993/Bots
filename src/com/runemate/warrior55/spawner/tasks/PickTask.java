package com.runemate.warrior55.spawner.tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.spawner.main.EggSpawner;
import com.runemate.warrior55.spawner.tasks.common.Utils;

public class PickTask extends Task {

    private int failedPicksCounter = 0;

    private final Validators VALIDATORS = new Validators();

    private final EggSpawner BOT = (EggSpawner) Environment.getBot();

    @Override
    public boolean validate() {
        if (Players.getLocal() != null) {

            if (VALIDATORS.isPick()) {
                BOT.setNoDropCounter(0);
                return true;
            }

            if (!Bank.isOpen()) {
                BOT.setNoDropCounter(BOT.getNoDropCounter() + 1);
            }
        }
    
        return false;
    }

    @Override
    public void execute() {
        GroundItem loot = BOT.getLoot();

        if (failedPicksCounter > 5) {
            Utils.walk(loot.getPosition());
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
