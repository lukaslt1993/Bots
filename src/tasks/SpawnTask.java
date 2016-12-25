
package tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import main.EggSpawner;
import tasks.common.Constants;
import tasks.common.Utils;

public class SpawnTask extends Task {
    
    private final EggSpawner BOT = (EggSpawner) Environment.getBot();
    
    private final Validators VALIDATORS = new Validators();

    @Override
    public boolean validate() {
        return VALIDATORS.isSpawn();
    }

    @Override
    public void execute() {
        System.out.println("Spawn");
        if (BOT.getNoDropCounter() > 5) {
            Utils.walk(Constants.BANK_COORD);
        }
        
        Summoning.FamiliarOption.getLeftClick().select();
        Execution.delayUntil(() -> Players.getLocal().getAnimationId() == -1 || checkDrop(), 3500);
        // locatable keisti i ground item ir tikrinti ar rasti eggs
    }
    
    private boolean checkDrop() {
        BOT.setDrop(GroundItems.newQuery().names("Red spiders' eggs").results()); 
        return !BOT.getDrop().isEmpty();
    } 
}
