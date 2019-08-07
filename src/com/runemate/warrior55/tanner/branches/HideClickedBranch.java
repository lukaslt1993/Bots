
package com.runemate.warrior55.tanner.branches;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.tanner.leafs.ClickCrafter;

public class HideClickedBranch extends BranchTask{
    
    private final MakeXInterfaceBranch INTERFACE_BRANCH = new MakeXInterfaceBranch();
    private final ClickCrafter CLICK_CRAFTER_LEAF = new ClickCrafter();

    @Override
    public TreeTask successTask() {
        return INTERFACE_BRANCH;
    }

    @Override
    public TreeTask failureTask() {
        return CLICK_CRAFTER_LEAF;
    }

    @Override
    public boolean validate() {
        return Inventory.getSelectedItem() == null;
    }
}
