
package com.runemate.warrior55.tanner.branches;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.tanner.leafs.Wait;
import com.runemate.warrior55.tanner.main.PortableCrafter;

public class UsingOwnCrafterBranch extends BranchTask {

    private final Wait WAIT_LEAF = new Wait();
    private final InventoryContainsCrafterBranch INVENTORY_CONTAINS_CRAFTER_BRANCH = new InventoryContainsCrafterBranch();

    @Override
    public TreeTask successTask() {
        return INVENTORY_CONTAINS_CRAFTER_BRANCH;
    }

    @Override
    public TreeTask failureTask() {
        return WAIT_LEAF;
    }

    @Override
    public boolean validate() {
        PortableCrafter bot = (PortableCrafter) Environment.getBot();
        return bot.canUseOwnCrafter();
    }

}
