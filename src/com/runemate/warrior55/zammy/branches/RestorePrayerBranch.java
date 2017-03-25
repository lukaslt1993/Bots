
package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.rs3.local.hud.Powers;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class RestorePrayerBranch extends BranchTask {
    
    private final PlayerNearAltarBranch playerNearAltarBranch;
    private final PlayerInSpotBranch playerInSpotBranch;
    private final ZammyWineGrabber bot;
    
    public RestorePrayerBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        playerInSpotBranch = new PlayerInSpotBranch(bot);
        playerNearAltarBranch = new PlayerNearAltarBranch(bot);
    }
    
    @Override
    public TreeTask successTask() {
        return playerInSpotBranch;
    }

    @Override
    public TreeTask failureTask() {
        return playerNearAltarBranch;
    }

    @Override
    public boolean validate() {
        return Powers.Prayer.getPoints() >= Powers.Prayer.getMaximumPoints() * bot.getPrayAt() / 100 && Skill.PRAYER.getCurrentLevel() >= 37;
    }
}
