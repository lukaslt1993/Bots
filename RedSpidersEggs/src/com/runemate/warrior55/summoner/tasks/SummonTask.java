
package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.main.Summoner;

public class SummonTask extends Task {
    
    private final Validators validators;
   
    private final Summoner bot ;
    
    public SummonTask(Summoner s) {
        bot = s;
        validators = new Validators(bot);
    }
    
    @Override
    public boolean validate() {
        return Players.getLocal() != null && validators.isSummon();
    }

    @Override
    public void execute() {
        ChatDialog.Continue cc = ChatDialog.getContinue();
        
        if (cc != null) {
            cc.select(true);
        }
        
        SpriteItem pouch = Inventory.getItems(bot.getPouchName()).first();
        
        if (pouch != null && pouch.click()) {
            Execution.delayWhile(() -> validate(), 5000);
            // Strangely, special move from action bar stops working after resummon. This toggle fixes it.
            // TODO: maybe still needed to check if toggled after 20 sec
            Execution.delayUntil(() -> ActionBar.toggleLock(), 20000);
        }     
    }    
}
