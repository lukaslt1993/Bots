package com.runemate.warrior55.zammy.leafs;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import com.runemate.game.api.rs3.local.hud.Powers;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class ClickProtectMage extends LeafTask {
    
    private final ZammyWineGrabber bot;
    
    public ClickProtectMage(ZammyWineGrabber zwg) {
        bot = zwg;
    }

    @Override
    public void execute() {
        /*if (Environment.isOSRS()) {
            Prayer.PROTECT_FROM_MAGIC.activate();
        } else {*/
            ActionBar.Slot slot = ActionBar.newQuery().names("Protect from Magic")/*.filter((x) -> x.getActionBar() == ActionBar.getNumber())*/.results().first();
            if (slot != null) {
                if (slot.activate()) {
                    Execution.delayUntil(() -> Powers.Prayer.PROTECT_FROM_MAGIC.isActivated(), 2500);
                }
            } else {
                throw new IllegalStateException("No protect from magic prayer in action bar");
            }
        //} 
        /*else if (!Powers.Prayer.isQuickPraying()) {
            if (Powers.Prayer.toggleQuickPrayers()) {
                Execution.delayUntil(() -> Powers.Prayer.PROTECT_FROM_MAGIC.isActivated(), 2500);
            }
        }*/
    }
}
