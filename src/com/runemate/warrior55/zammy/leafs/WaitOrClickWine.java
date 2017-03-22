package com.runemate.warrior55.zammy.leafs;

import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class WaitOrClickWine extends LeafTask {
    
    private final ZammyWineGrabber bot;
    private final Player player = Players.getLocal();
    
    public WaitOrClickWine(ZammyWineGrabber zwg) {
        bot = zwg;
    }
    
    @Override
    public void execute() {
        GroundItem wine = GroundItems.newQuery().names(bot.getWineName()).results().first();
        if (wine != null) {
            int usedSlots = Inventory.getUsedSlots();
            if (wine.interact("Cast")) {
                if (!Execution.delayUntil(() -> Inventory.getUsedSlots() > usedSlots, 5000)) {
                    bot.setFailedPicks(bot.getFailedPicks() + 1);  
                } else {
                    bot.setFailedPicks(0);
                    Execution.delayUntil(() -> !Npcs.newQuery().targeting(player).results().isEmpty(), 4500);
                }
            }
        }
    }
}
