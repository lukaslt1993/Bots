
package tasks;

import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.script.framework.task.Task;
import tasks.common.Constants;
import tasks.common.Utils;

public class SpawnTask extends Task {

    @Override
    public boolean validate() {
        return Validators.isSpawn();
    }

    @Override
    public void execute() {
        if (Constants.BOT.getNothingSpawnedCounter() > 5) {
            Utils.walk(Constants.BANK_COORD);
        }
        
        Summoning.FamiliarOption.getLeftClick().select();
    }
}
