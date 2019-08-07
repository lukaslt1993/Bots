
package com.runemate.warrior55.tanner.branches;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.tanner.leafs.DeployCrafter;

public class InventoryContainsCrafterBranch extends BranchTask {

    private final DeployCrafter DEPLOY_CRAFTER_LEAF = new DeployCrafter();
    private final BankBranch BANK_BRANCH = new BankBranch();

    @Override
    public TreeTask successTask() {
        return DEPLOY_CRAFTER_LEAF;
    }

    @Override
    public TreeTask failureTask() {
        return BANK_BRANCH;
    }

    @Override
    public boolean validate() {
        return Inventory.contains("Portable crafter");
    }

}
