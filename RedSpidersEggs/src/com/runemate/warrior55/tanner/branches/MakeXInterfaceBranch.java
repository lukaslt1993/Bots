
package com.runemate.warrior55.tanner.branches;

import com.runemate.game.api.rs3.local.hud.interfaces.MakeXInterface;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.tanner.leafs.ClickHide;
import com.runemate.warrior55.tanner.leafs.PressSpace;

public class MakeXInterfaceBranch extends BranchTask {
    
    private final ClickHide CLICK_HIDE_LEAF = new ClickHide();
    private final PressSpace PRESS_SPACE_LEAF = new PressSpace();

    @Override
    public TreeTask successTask() {
        return CLICK_HIDE_LEAF;
    }

    @Override
    public TreeTask failureTask() {
        return PRESS_SPACE_LEAF;
    }

    @Override
    public boolean validate() {
        return !MakeXInterface.isOpen();
    }
}
