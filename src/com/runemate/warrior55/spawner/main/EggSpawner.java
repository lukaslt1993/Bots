
package com.runemate.warrior55.spawner.main;

import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.script.framework.task.TaskBot;
import com.runemate.warrior55.spawner.tasks.BankTask;
import com.runemate.warrior55.spawner.tasks.PickTask;
import com.runemate.warrior55.spawner.tasks.RestoreTask;
import com.runemate.warrior55.spawner.tasks.SpawnTask;
import com.runemate.warrior55.spawner.tasks.SummonTask;

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
        setLoopDelay(25, 50);
        add(new BankTask(), new PickTask(), new RestoreTask(), new SpawnTask(), new SummonTask());
    }
}
