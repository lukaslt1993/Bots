
package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.leafs.Eat;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class InventoryContainsFoodBranch extends BranchTask {
    
    private final ZammyWineGrabber bot;
    private Eat eat;
    private final PlayerInBankBranch playerInBankBranch;
    private SpriteItem food;
    
    public InventoryContainsFoodBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        playerInBankBranch = new PlayerInBankBranch(bot); 
    }
    
    @Override
    public TreeTask successTask() {
        eat = new Eat(food);
        return eat;
    }

    @Override
    public TreeTask failureTask() {
        return playerInBankBranch;
    }

    @Override
    public boolean validate() {
        return (food = Inventory.newQuery().actions("Eat").results().random()) != null;
    }
}
