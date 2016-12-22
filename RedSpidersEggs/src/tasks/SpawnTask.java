
package tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
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
        if (BOT.getNoDropCounter() > 10) {
            Utils.walk(Constants.BANK_COORD);
        }
        
        Summoning.FamiliarOption.getLeftClick().select();
    }
}
