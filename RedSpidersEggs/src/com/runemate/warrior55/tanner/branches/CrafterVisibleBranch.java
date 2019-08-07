
package com.runemate.warrior55.tanner.branches;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;

public class CrafterVisibleBranch extends BranchTask {

    private final HideClickedBranch HIDE_CLICKED_BRANCH = new HideClickedBranch();
    private final FindWorldAndCrafterBranch FIND_WORLD_AND_CRAFTER_BRANCH = new FindWorldAndCrafterBranch();

    @Override
    public TreeTask successTask() {
        return HIDE_CLICKED_BRANCH;
    }

    @Override
    public TreeTask failureTask() {
        return FIND_WORLD_AND_CRAFTER_BRANCH;
    }

    @Override
    public boolean validate() {
        GameObject crafter = GameObjects.newQuery().names("Portable crafter").results().nearest();
        return crafter != null && (crafter.isVisible() || Camera.turnTo(crafter));
    }

}
