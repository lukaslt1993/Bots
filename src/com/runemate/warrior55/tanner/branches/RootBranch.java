
package com.runemate.warrior55.tanner.branches;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.tanner.common.Constants;

public class RootBranch extends BranchTask {
    
    private final BankBranch BANK_BRANCH = new BankBranch();
    private final CrafterVisibleBranch CRAFTER_VISIBLE_BRANCH = new CrafterVisibleBranch();

    @Override
    public TreeTask successTask() {
        return BANK_BRANCH;
    }

    @Override
    public TreeTask failureTask() {
        return CRAFTER_VISIBLE_BRANCH;
    }

    @Override
    public boolean validate() {
        return Inventory.isEmpty() || !Inventory.contains(Constants.PATTERN);
    }
}
