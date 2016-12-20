
package tasks;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import tasks.common.Constants;

public class Validators {
    
    public static boolean isBank() {
        return Bank.isOpen()
                || Inventory.isFull()
                || !Inventory.containsAnyOf(Constants.SCROLL_NAME)
                || Players.getLocal().getFamiliar() == null
                && !Inventory.containsAnyOf(Constants.POUCH_NAME)
                || Summoning.getPoints() < 1
                && !Inventory.containsAnyOf(Constants.POTION_NAMES);
    }

    public static boolean isPick() {
        Constants.BOT.setEggs(GroundItems.newQuery().names("Red spiders' eggs").results().nearest());
        return !isBank() && Constants.BOT.getEggs() != null;
    }

    public static boolean isRestore() {
        return (Summoning.getPoints() < 1 || Summoning.getSpecialMovePoints() < 6)
                && !isBank() && !isPick();
    }
    
    public static boolean isSummon() {
        return !isBank() && !isPick() && !isRestore() && Players.getLocal().getFamiliar() == null;
    }
    
    public static boolean isSpawn() {
        return !isBank() && !isPick() && !isSummon() && !isRestore();
    }
}
