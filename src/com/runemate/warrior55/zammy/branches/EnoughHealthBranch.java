
package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.local.hud.interfaces.Health;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class EnoughHealthBranch extends BranchTask {
    
    private final ZammyWineGrabber bot;
    private final RestorePrayerBranch restorePrayerBranch;
    private final InventoryContainsFoodBranch inventoryContainsFoodBranch;
    
    public EnoughHealthBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        inventoryContainsFoodBranch = new InventoryContainsFoodBranch(bot); 
        restorePrayerBranch = new RestorePrayerBranch(bot);
    }
    
    @Override
    public TreeTask successTask() {
        return restorePrayerBranch;
    }

    @Override
    public TreeTask failureTask() {
        return inventoryContainsFoodBranch;
    }

    @Override
    public boolean validate() {
        return Health.getCurrentPercent() >= bot.getEatAt() && !Inventory.isFull();
    }
}
