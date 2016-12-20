
package tasks;

import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import tasks.common.Constants;
import tasks.common.Utils;

public class PickTask extends Task {
    
    private int failedPicksCounter = 0;
    
    @Override
    public boolean validate() {
        if (Validators.isPick()) {
            Constants.BOT.setNothingSpawnedCounter(0);
            return true;
        }
        
        if (!Bank.isOpen()) {
            Constants.BOT.setNothingSpawnedCounter(Constants.BOT.getNothingSpawnedCounter() + 1);
        }
        
        return false;
    }

    @Override
    public void execute() {
        GroundItem eggs = Constants.BOT.getEggs();
        
        if (failedPicksCounter > 5) {
            Utils.walk(eggs.getPosition());
        }

        if (eggs.isVisible() || Camera.turnTo(eggs)) {
            int usedSlots = Inventory.getUsedSlots();

            if (eggs.interact("Take", eggs.getDefinition().getName())) {
                Execution.delayUntil(() -> Inventory.getUsedSlots() > usedSlots, 500, 5000);
                failedPicksCounter = 0;

            } else {
                failedPicksCounter++;
            }

        } else {
            failedPicksCounter++;
        }
    }
}
