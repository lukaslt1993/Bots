package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.local.hud.interfaces.MakeXInterface;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.common.Utils;

public class InfuseTask extends Task {

    private final Validators validators;
    private final Summoner bot;
    private final Coordinate[] pathToObelisk = {new Coordinate(2884, 3418), new Coordinate(2896, 3415), new Coordinate(2909, 3418), new Coordinate(2920, 3425), new Coordinate(2923, 3439), new Coordinate(2930, 3448)};
    private final Coordinate[] pathToObelisk2 = {new Coordinate(2889, 3414), new Coordinate(2903, 3415), new Coordinate(2915, 3423), new Coordinate(2922, 3435), new Coordinate(2927, 3448)};
    private final Coordinate[][] paths = {pathToObelisk, pathToObelisk2};
    //private WebPath pathToObelisk;
    
    public InfuseTask(Summoner s) {
        bot = s;
        validators = new Validators(bot);
    }

    @Override
    public boolean validate() {
        return validators.isInfuse();
    }

    @Override
    public void execute() {
        if (MakeXInterface.isOpen()) {
            Keyboard.typeKey(' ');
            Execution.delayUntil(() -> !MakeXInterface.isOpen(), 2500);

            if (MakeXInterface.isOpen()) {
                bot.stop();
            }

        } else {
            GameObject obelisk = GameObjects.newQuery().names("Obelisk").results().nearest();

            if (obelisk != null && Players.getLocal().distanceTo(obelisk) < 10) {

                if (obelisk.click()) {
                    Execution.delayUntil(() -> MakeXInterface.isOpen(), 15000);

                    /*if (!MakeXInterface.isOpen()) {
                        bot.stop();
                    }*/

                } else {
                    Camera.turnTo(obelisk);
                }

            } else {

                /*if (pathToObelisk == null || pathToObelisk.getNext() == null) {
                    pathToObelisk = Traversal.getDefaultWeb().getPathBuilder().buildTo(new Coordinate(2930, 3448));
                }

                if (pathToObelisk != null) {
                    pathToObelisk.step();

                } else {
                    throw new IllegalStateException("Can not generate walking path");
                }*/

                Utils.smartWalk(paths[Random.nextInt(paths.length)]);
                Player player = Players.getLocal();
                Execution.delayUntil(() -> !player.isMoving(), 1000, 8000);
            }
        }
    }
}
