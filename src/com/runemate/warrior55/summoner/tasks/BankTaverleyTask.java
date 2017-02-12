
package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.common.Utils;

public class BankTaverleyTask extends Task{
    
    private final Validators validators = new Validators();
    private final Summoner bot = (Summoner)Environment.getBot();
    private final Coordinate[] pathToBank = {new Coordinate(2923, 3439), new Coordinate(2920, 3425), new Coordinate(2909, 3418), new Coordinate(2896, 3415), new Coordinate(2884, 3418), new Coordinate(2875, 3416)};
    private final Coordinate[] pathToBank2 = {new Coordinate(2921, 3437), new Coordinate(2914, 3424), new Coordinate(2902, 3414), new Coordinate(2888, 3417), new Coordinate(2875, 3416)};
    private final Coordinate[][] paths = {pathToBank, pathToBank2};
    //private WebPath pathToBank;
    
    @Override
    public boolean validate() {
        return validators.isBankTaverley();
    }
    
    @Override
    public void execute() {
        Player player = Players.getLocal();
        Coordinate c = player.getPosition();
        
        if (c.getPlane() == 1 && c.getX() == 2874 && c.getY() == 3420) {
            GameObjects.newQuery().names("Stairs").results().nearest().click();
            Execution.delayUntil(() -> player.getPosition().getPlane() == 0, 5000);
        }
        
        Npc banker = Npcs.newQuery().names("Banker").results().nearest();
        
        if (banker == null || player.distanceTo(banker) >= 10) {
            
            /*if (pathToBank == null || pathToBank.getNext() == null) { 
                pathToBank = Traversal.getDefaultWeb().getPathBuilder().buildTo(new Coordinate(2875, 3416));
            }
            
            if (pathToBank != null) {
                pathToBank.step();
                
            } else {
                throw new IllegalStateException("Can not generate walking path");
            }*/
            
            Utils.smartWalk(paths[Random.nextInt(paths.length)]);
            Execution.delayUntil(() -> !player.isMoving(), 1000, 8000);   
            
        } else if (!Bank.isOpen()) {
            Bank.open();
            
        } else {
            Utils.loadPresetAndWait(1);
        }
    }      
    
}
