
package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.common.Utils;

public class BankTaverleyTask extends Task{
    
    private final Validators VALIDATORS = new Validators();
    private final Summoner BOT = (Summoner)Environment.getBot();
    private final Coordinate[] PATH_TO_BANK = {new Coordinate(2923, 3439), new Coordinate(2920, 3425), new Coordinate(2909, 3418), new Coordinate(2896, 3415), new Coordinate(2884, 3418), new Coordinate(2875, 3416)};
    
    @Override
    public boolean validate() {
        return VALIDATORS.isBankTaverley();
    }
    
    @Override
    public void execute() {
        Player player = Players.getLocal();
        Npc banker = Npcs.newQuery().names("Banker").results().nearest();
        
        if (banker == null || player.distanceTo(banker) >= 10) {
            Utils.smartWalk(PATH_TO_BANK);
            Execution.delayUntil(() -> !player.isMoving(), 1000, 8000);   
            
        } else if (!Bank.isOpen()) {
            Bank.open();
            
        } else {
            Utils.loadPresetAndWait(1);
        }
    }      
    
}
