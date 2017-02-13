
package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning.FamiliarOption;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.common.Constants;

public class TeleportTask extends Task {
    
    private final Summoner bot;
    private final Validators validators;
    
    public TeleportTask(Summoner s) {
        bot = s;
        validators = new Validators(bot);
    }
    
    @Override
    public boolean validate() {
        return validators.isTeleport();
    }
    
    @Override
    public void execute() {
        FamiliarOption leftClick = Summoning.FamiliarOption.getLeftClick();
        
        if (leftClick != null && leftClick.select()) {
            Execution.delayUntil(() -> !ChatDialog.getOptions().isEmpty(), 15000);
            
            if (!ChatDialog.getOptions().isEmpty()) {
                Keyboard.typeKey('2');
                Execution.delayUntil(() -> Players.getLocal().distanceTo(Constants.TRAP_DOOR_COORD) < 200, 10000);
            }
        }
    }    
    
}
