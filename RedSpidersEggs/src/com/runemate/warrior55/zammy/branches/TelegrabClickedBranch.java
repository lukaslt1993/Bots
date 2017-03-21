
package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.leafs.ClickTelegrabAndHoverMouse;
import com.runemate.warrior55.zammy.leafs.WaitOrClickWine;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class TelegrabClickedBranch extends BranchTask {
    
    private final ClickTelegrabAndHoverMouse clickTelegrabAndHoverMouse;
    private final WaitOrClickWine waitOrClickWine;
    private final ZammyWineGrabber bot;
    
    public TelegrabClickedBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        waitOrClickWine = new WaitOrClickWine(bot);
        clickTelegrabAndHoverMouse = new ClickTelegrabAndHoverMouse(bot);
    }
    
    @Override
    public TreeTask successTask() {
        return waitOrClickWine;
    }

    @Override
    public TreeTask failureTask() {
        return clickTelegrabAndHoverMouse;
    }

    @Override
    public boolean validate() {
        if (Environment.isOSRS()) {
            return Magic.TELEKINETIC_GRAB.isSelected();
        } else {
            ActionBar.Slot slot = ActionBar.newQuery().names("Telekinetic Grab")/*.filter((x) -> x.getActionBar() == ActionBar.getNumber())*/.results().first();
            if (slot != null) {
                return slot.isSelected();
            } else {
                throw new IllegalStateException("No telekinetic grab in action bar");
            }
        }
    }
}
