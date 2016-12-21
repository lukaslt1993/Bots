
package tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import main.EggSpawner;
import tasks.common.Utils;

public class PickTask extends Task {
    
    private int failedPicksCounter = 0;
    
    private final Validators VALIDATORS = new Validators();
    
    private final EggSpawner BOT = (EggSpawner) Environment.getBot();
    
    @Override
    public boolean validate() {
        if (VALIDATORS.isPick()) {
            BOT.setNoDropCounter(0);
            return true;
        }
        
        if (!Bank.isOpen()) {
            BOT.setNoDropCounter(BOT.getNoDropCounter() + 1);
        }
        
        return false;
    }

    @Override
    public void execute() {
        GroundItem eggs = BOT.getEggs();
        
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
