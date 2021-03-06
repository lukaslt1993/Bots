
package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.rs3.local.hud.Powers;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class InCombatBranch extends BranchTask {
    
    private final ZammyWineGrabber bot;
    private final RS3CheckRunesBranch rs3CheckRunesBranch;
    private final PrayerBranch prayerBranch;
    
    public InCombatBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        rs3CheckRunesBranch = new RS3CheckRunesBranch(bot); 
        prayerBranch = new PrayerBranch(bot);
    }
    
    @Override
    public TreeTask successTask() {
        return prayerBranch;
    }

    @Override
    public TreeTask failureTask() {
        /*if (Powers.Prayer.isQuickPraying()) {
            Powers.Prayer.toggleQuickPrayers();
        }*/
        if (Powers.Prayer.PROTECT_FROM_MAGIC.isActivated()) {
            ActionBar.Slot slot = ActionBar.newQuery().names("Protect from Magic")/*filter((x) -> x.getActionBar() == ActionBar.getNumber())*/.results().first();
            if (slot != null) {
                if (slot.activate()) {
                    Execution.delayUntil(() -> !Powers.Prayer.PROTECT_FROM_MAGIC.isActivated(), 2500);
                }
            } else {
                throw new IllegalStateException("No protect from magic prayer in action bar");
            }
        }
        return rs3CheckRunesBranch;
    }

    @Override
    public boolean validate() {
        return bot.isInCombat();
    }
}
