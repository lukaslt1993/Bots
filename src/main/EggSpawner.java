
package main;

import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.script.framework.task.TaskBot;
import tasks.BankTask;
import tasks.PickTask;
import tasks.RestoreTask;
import tasks.SpawnTask;
import tasks.SummonTask;

public class EggSpawner extends TaskBot {
    
    private LocatableEntityQueryResults drop = GroundItems.newQuery().names("Red spiders' eggs").results();

    public LocatableEntityQueryResults getDrop() {
        return drop;
    }

    public void setDrop(LocatableEntityQueryResults drop) {
        this.drop = drop;
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
