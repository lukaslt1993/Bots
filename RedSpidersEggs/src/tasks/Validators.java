
package tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import main.EggSpawner;
import tasks.common.Constants;

class Validators {
    
    private final EggSpawner BOT = (EggSpawner) Environment.getBot();
    
    protected boolean isBank() {
        return Bank.isOpen()
                || Inventory.isFull()
                || !Inventory.containsAnyOf(Constants.SCROLL_NAME)
                || Players.getLocal().getFamiliar() == null
                && !Inventory.containsAnyOf(Constants.POUCH_NAME)
                || Summoning.getPoints() < 1
                && !Inventory.containsAnyOf(Constants.POTION_NAMES);
    }

    protected boolean isPick() {
        BOT.setEggs(GroundItems.newQuery().names("Red spiders' eggs").results().nearest());
        return !isBank() && BOT.getEggs() != null;
    }

    protected boolean isRestore() {
        return (Summoning.getPoints() < 1 || Summoning.getSpecialMovePoints() < 6)
                && !isBank() && !isPick();
    }
    
    protected boolean isSummon() {
        return !isBank() && !isPick() && !isRestore() && Players.getLocal().getFamiliar() == null;
    }
    
    protected boolean isSpawn() {
        return !isBank() && !isPick() && !isSummon() && !isRestore();
    }
}
