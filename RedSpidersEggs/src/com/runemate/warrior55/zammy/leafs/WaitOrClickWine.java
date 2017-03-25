package com.runemate.warrior55.zammy.leafs;

import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class WaitOrClickWine extends LeafTask {

    private final ZammyWineGrabber bot;

    public WaitOrClickWine(ZammyWineGrabber zwg) {
        bot = zwg;
    }

    @Override
    public void execute() {
        GroundItem wine = GroundItems.newQuery().names(bot.getWineName()).results().first();
        if (wine != null && wine.interact("Cast")) {
            int usedSlots = Inventory.getUsedSlots();
            bot.setNewWinePoint(wine.getInteractionPoint());
            if (!Execution.delayUntil(() -> Inventory.getUsedSlots() > usedSlots, 5000)) {
                bot.setFailedPicks(bot.getFailedPicks() + 1);
            } else {
                bot.setFailedPicks(0);
                Execution.delayUntil(() -> !Npcs.newQuery().targeting(bot.getPlayer()).results().isEmpty(), 4500);
            }
        }
    }
}
