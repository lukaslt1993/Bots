package com.runemate.warrior55.summoner.tasks.common;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.warrior55.summoner.main.Summoner;

public class Utils {

    public static void walk(Coordinate c) {
        Coordinate currentCoord = Players.getLocal().getPosition();

        if (currentCoord.getX() == c.getX() && currentCoord.getY() == c.getY()) {
            return;
        }

        BresenhamPath path = BresenhamPath.buildTo(c);

        if (path != null) {
            path.step();

        } else if (((Summoner) Environment.getBot()).getType().equals("Spawn")) {
            BresenhamPath bankPath = BresenhamPath.buildTo(Constants.BANK_COORD);

            if (bankPath != null) {
                bankPath.step();

            } else {
                throw new IllegalStateException("Can not generate walking path");
                //Environment.getBot().stop();
            }

        } else {
            throw new IllegalStateException("Can not generate walking path");
            //Environment.getBot().stop();
        }
    }

    public static void smartWalk(Coordinate[] c) {
        PredefinedPath path = PredefinedPath.create(c);

        for (int i = 0; i < c.length; i++) {

            if (!Execution.delayUntil(() -> path.step(), 8000)) {
                Environment.getBot().stop();
            }
        }
    }

    public static void loadPresetAndWait(int preset) {
        if (Bank.loadPreset(preset, false)) {
            Execution.delayUntil(() -> isLoaded(), 5000);

            if (!isLoaded()) {
                Environment.getBot().stop();
            }
        }
    }

    public static boolean isLoaded() {
        return Inventory.getItems(Constants.SUMM_STUFF).size() == 3
                || Inventory.getItems(Constants.POUCH_PATTERN).size() >= 27;
    }
}
