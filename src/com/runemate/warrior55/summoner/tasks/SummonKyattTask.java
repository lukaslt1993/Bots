
package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.main.Summoner;

public class SummonKyattTask extends Task {
    
    private final Validators validators;
    private final Summoner bot;
    
    public SummonKyattTask(Summoner s) {
        bot = s;
        validators = new Validators(bot);
    }
    
    @Override
    public boolean validate() {
        return validators.isSummonKyatt();
    }
    
    @Override
    public void execute() {
        ChatDialog.Continue cc = ChatDialog.getContinue();
        
        if (cc != null) {
            cc.select(true);
        }
        
        SpriteItem pouch = Inventory.getItems("Spirit kyatt pouch").first();
        
        if (pouch != null) {
            
            if (pouch.click()) {
                Player player = Players.getLocal();
                Execution.delayUntil(() -> player.getFamiliar() != null, 5000);
            } 
        }
    }    
    
}
