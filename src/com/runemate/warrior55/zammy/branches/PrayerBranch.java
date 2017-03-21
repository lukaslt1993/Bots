
package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.rs3.local.hud.Powers;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class PrayerBranch extends BranchTask {
    
    private final PlayerInSafeSpotBranch playerInSafeSpotBranch;
    private final ProtectMageClickedBranch protectMageClickedBranch;
    private final ZammyWineGrabber bot;
    
    public PrayerBranch (ZammyWineGrabber zwg) {
        bot = zwg;
        protectMageClickedBranch = new ProtectMageClickedBranch(bot);
        playerInSafeSpotBranch = new PlayerInSafeSpotBranch(bot);
    }
    
    @Override
    public TreeTask successTask() {
        return protectMageClickedBranch;
    }

    @Override
    public TreeTask failureTask() {
        return playerInSafeSpotBranch;
    }

    @Override
    public boolean validate() {
        return Powers.Prayer.getPoints() >= Powers.Prayer.getMaximumPoints() * bot.getPrayAt() / 100 && Skill.PRAYER.getCurrentLevel() >= 37;
    }
}
