
package com.runemate.warrior55.zammy.leafs;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.hud.InteractablePoint;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.script.framework.tree.LeafTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class ClickTelegrabAndHoverMouse extends LeafTask {
    
    private final ZammyWineGrabber bot;
    
    public ClickTelegrabAndHoverMouse(ZammyWineGrabber zwg) {
        bot = zwg;
    } 
    
    @Override
    public void execute () {
        if (Environment.isOSRS()) {
            Magic.TELEKINETIC_GRAB.activate();
        } else {
            ActionBar.Slot slot = ActionBar.newQuery().names("Telekinetic Grab")/*filter((x) -> x.getActionBar() == ActionBar.getNumber())*/.results().first();
            if (slot != null) {
                slot.activate();
            } else {
                
                //throw new IllegalStateException("No telekinetic grab in action bar");
            }
        }
        InteractablePoint oldp = bot.getOldWinePoint();
        InteractablePoint newp = bot.getNewWinePoint();
        if (oldp != null && newp != null && oldp.x == newp.x && oldp.y == newp.y) {
            Mouse.move(newp);
        } else {
            Mouse.move(bot.getWineCoord());   
            bot.setOldWinePoint(newp);
        }
    }
}
