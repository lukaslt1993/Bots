package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.main.Summoner;

public class TimerTask extends Task {
   
     private final Summoner BOT = (Summoner) Environment.getBot();
     private final Timer TIMER = BOT.TIMER;

    @Override
    public boolean validate() { 
        Summoner.totalUsage += TIMER.getElapsedTime();
        return Summoner.totalUsage >= 10800000 && BOT.getMetaData().getHourlyPrice().doubleValue() <= 0 && BOT.getType().equals("Spawn");
    }

    @Override
    public void execute() {
        BOT.stop();
    }
}
