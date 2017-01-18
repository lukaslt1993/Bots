
package com.runemate.warrior55.spawner.tasks;

import com.runemate.game.api.hybrid.Environment;
//import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.warrior55.spawner.main.EggSpawner;
import com.runemate.warrior55.spawner.tasks.common.Constants;

class Validators {
    
    private final EggSpawner BOT = (EggSpawner) Environment.getBot();
    
    boolean isBank() {
        //Player player = Players.getLocal();
        return Bank.isOpen()
                || Inventory.isFull()
                || !Inventory.containsAnyOf(BOT.getScrollName())
                || /* player != null
                && */ Players.getLocal().getFamiliar() == null
                && !Inventory.containsAnyOf(BOT.getPouchName())
                || Summoning.getPoints() < 1
                && !Inventory.containsAnyOf(Constants.POTION_NAMES);
    }

    boolean isPick() {
        BOT.setLoot(GroundItems.newQuery().names(BOT.getLootNames()).results().nearest());
        return BOT.getLoot() != null && !isBank();
    }

    boolean isRestore() {
        return (Summoning.getPoints() < 1 || Summoning.getSpecialMovePoints() < 6)
                && !isBank() && !isPick();
    }
    
    boolean isSummon() {
        return !isBank() && !isPick() && !isRestore() && Players.getLocal().getFamiliar() == null;
    }
    
    boolean isSpawn() {
        return !isBank() && !isPick() && !isSummon() && !isRestore();
    }
}
