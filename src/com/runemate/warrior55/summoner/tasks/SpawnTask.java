package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.common.Utils;

public class SpawnTask extends Task {

    private final Summoner bot;
    private final Validators validators;

    public SpawnTask(Summoner s) {
        bot = s;
        validators = new Validators(bot);
    }

    @Override
    public boolean validate() {
        return Players.getLocal() != null && validators.isSpawn();
    }

    @Override
    public void execute() {
        Player player = Players.getLocal();
        Coordinate initialPos = bot.getInitialPos();
        Double d = player.distanceTo(initialPos);

        if (d <= 4 && bot.isLootAll() || d <= 8 && !bot.isLootAll() /*|| !bot.isLootAll()*/) {
            ActionBar.Slot firstSlot = ActionBar.getFilledSlots().first();

            if (firstSlot != null) {
                firstSlot.activate(false);

            } else {
                bot.showAndLogAlert("Can not get first action bar slot, maybe empty");
                // TODO: check more places to stop
                bot.stop();
            }

            if (!bot.getPouchName().equals("Spirit cobra pouch")) {

                if (bot.getNoDropCounter() > 10) {
                    Utils.walk(initialPos, bot);
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
            
        } else if (/*bot.getAllLoot() != null*/ bot.getAllLoot().isEmpty()) {
            Utils.walk(initialPos, bot);
        }
    }
}
