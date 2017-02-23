package com.runemate.warrior55.summoner.tasks.common;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.warrior55.summoner.main.Summoner;

public class Utils {

    public static void walk(Coordinate c, Summoner bot) {
        //Summoner bot = (Summoner) Environment.getBot();

        if (bot != null && bot.isRunning()) {
            Coordinate currentCoord = Players.getLocal().getPosition();

            if (currentCoord.getX() == c.getX() && currentCoord.getY() == c.getY()) {
                return;
            }

            BresenhamPath path = BresenhamPath.buildTo(c);

            if (path != null) {
                path.step();

            } /*else if (bot.getType().equals("Spawn")) {
             BresenhamPath bankPath = BresenhamPath.buildTo(getSpawningBankCoord(bot));

             if (bankPath != null) {
             bankPath.step();

             } else {
             throw new IllegalStateException("Can not generate walking path; bot was running? - " + bot.isRunning());
             //Environment.getBot().stop();
             }

             }*/ else {
                throw new IllegalStateException("Can not generate walking path; bot was running? - " + bot.isRunning());
                //Environment.getBot().stop();
            }
        }
    }

    public static void smartWalk(Coordinate[] c, Summoner bot) {
        //Summoner bot = (Summoner) Environment.getBot();

        if (bot != null && bot.isRunning()) {
            PredefinedPath path = PredefinedPath.create(c);

            for (int i = 0; i < c.length; i++) {

                if (!Execution.delayUntil(() -> path.step(), 8000)) {
                    bot.showAndLogAlert("Can not walk, maybe started in wrong place");
                    bot.stop();
                    break;
                }
            }
        }
    }

    public static void loadPresetAndWait(int preset, Summoner bot) {
        //Summoner bot = (Summoner) Environment.getBot();

        if (bot != null && bot.isRunning()) {
            if (Bank.loadPreset(preset, false)) {
                Execution.delayUntil(() -> isLoaded(), 5000);

                if (!isLoaded()) {
                    bot.showAndLogAlert("Out of stuff");
                    bot.stop();
                }
            }
        }
    }

    private static boolean isLoaded() {
        return Inventory.getItems(Constants.SUMM_STUFF).size() == 3
                || Inventory.getItems(Constants.POUCH_PATTERN).size() >= 27;
    }
}
