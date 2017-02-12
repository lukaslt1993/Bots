
package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.tasks.common.Constants;
import com.runemate.warrior55.summoner.tasks.common.Utils;
import java.util.List;

public class InteractTrapDoorTask extends Task {
    
    private final Validators validators = new Validators();
    
    @Override
    public boolean validate() {
        return validators.isInteractTrapDoor();
    }
    
    @Override
    public void execute() {
        if (Camera.getPitch() < 0.2) {
            Camera.turnTo(Random.nextInt(88, 97), Random.nextDouble(0.566, 0.568));
        }
        
        GameObject door = GameObjects.newQuery().names("Trapdoor").results().nearest();
        boolean isClimbDown = isClimbDown(door);
        Player player = Players.getLocal();
        
        if (door == null || player.distanceTo(door) > 5 || !door.click()) {
            Utils.walk(Constants.TRAP_DOOR_COORD);
        }
        
        else if (!isClimbDown) {
            Execution.delayUntil(() -> !player.isMoving(), 1000, 8000);
             
        } else {
            Execution.delayUntil(() -> GameObjects.newQuery().names("Obelisk").results().nearest() != null, 4000);
        }
        
    }   
    
    private boolean isClimbDown(GameObject door) {
        List <String> actions = door.getDefinition().getActions();
        
        for (String s: actions) {
        
            if (s.contains("Climb-down")) {
                return true;
            }
        }
        
        return false;
    }  
}
