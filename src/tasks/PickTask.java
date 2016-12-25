package tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
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
        System.out.println("Pick");
        LocatableEntityQueryResults drop = BOT.getDrop();
        drop = drop.sortByDistance();

        if (failedPicksCounter > 5) {
            System.out.println("Walk to nearest eggs");
            LocatableEntity nearestEggs = (LocatableEntity) drop.first();
            Utils.walk(nearestEggs.getPosition());
        }

        int iterations = drop.size();
        int loopCounter = 0;
        
        while (loopCounter < iterations) {
            LocatableEntity eggs = (LocatableEntity) drop.get(loopCounter);

            if (eggs.isVisible() || Camera.turnTo(eggs)) {
                System.out.println("Can interact with egss eggs");
                int usedSlots = Inventory.getUsedSlots();

                if (eggs.interact("Take")) {
                    System.out.println("Interaction with eggs successfull");
                    Execution.delayUntil(() -> Inventory.getUsedSlots() > usedSlots, 5000);
                    failedPicksCounter = 0;
                    drop.remove(eggs);
                    loopCounter--;
                    iterations--;

                } else {
                    System.out.println("Interaction with eggs failed");
                    failedPicksCounter++;
                }

            } else {
                System.out.println("Can not interact with eggs");
                failedPicksCounter++;
            }
            
            loopCounter++;
        }

    }
}
