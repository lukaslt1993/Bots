
package main;

import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.script.framework.task.TaskBot;
import tasks.BankTask;
import tasks.PickTask;
import tasks.RestoreTask;
import tasks.SpawnTask;
import tasks.SummonTask;

public class EggSpawner extends TaskBot {
    
    private GroundItem eggs;

    public GroundItem getEggs() {
        return eggs;
    }

    public void setEggs(GroundItem eggs) {
        this.eggs = eggs;
    }
    
    private int noDropCounter = 0;

    public int getNoDropCounter() {
        return noDropCounter;
    }

    public void setNoDropCounter(int noDropCounter) {
        this.noDropCounter = noDropCounter;
    }

    @Override
    public void onStart(String... args){
        setLoopDelay(250, 500);
        add(new BankTask(), new PickTask(), new RestoreTask(), new SpawnTask(), new SummonTask());
    }
}
