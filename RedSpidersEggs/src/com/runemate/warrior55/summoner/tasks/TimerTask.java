package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.main.Summoner;

public class TimerTask extends Task {
   
     private final Summoner bot = (Summoner) Environment.getBot();
     private final Timer timer = bot.timer;

    @Override
    public boolean validate() { 
        Summoner.totalUsage += timer.getElapsedTime();
        return Summoner.totalUsage >= 10800000 && bot.getMetaData().getHourlyPrice().doubleValue() <= 0 && bot.getType().equals("Spawn");
    }

    @Override
    public void execute() {
        bot.stop();
    }
}
