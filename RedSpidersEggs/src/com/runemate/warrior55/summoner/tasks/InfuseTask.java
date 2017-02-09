package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.MakeXInterface;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.common.Utils;

public class InfuseTask extends Task {

    private final Validators VALIDATORS = new Validators();
    private final Summoner BOT = (Summoner) Environment.getBot();
    private final Coordinate[] PATH_TO_OBELISK = {new Coordinate(2884, 3418), new Coordinate(2896, 3415), new Coordinate(2909, 3418), new Coordinate(2920, 3425), new Coordinate(2923, 3439), new Coordinate(2930, 3448)};

    @Override
    public boolean validate() {
        return VALIDATORS.isInfuse();
    }

    @Override
    public void execute() {
        if (MakeXInterface.isOpen()) {
            Keyboard.typeKey(' ');
            Execution.delayUntil(() -> !MakeXInterface.isOpen(), 2500);

            if (MakeXInterface.isOpen()) {
                BOT.stop();
            }

        } else {
            GameObject obelisk = GameObjects.newQuery().names("Obelisk").results().nearest();

            if (obelisk != null && Players.getLocal().distanceTo(obelisk) < 10) {

                if (obelisk.click()) {
                    Execution.delayUntil(() -> MakeXInterface.isOpen(), 15000);

                    if (!MakeXInterface.isOpen()) {
                        BOT.stop();
                    }

                } else {
                    Camera.turnTo(obelisk);
                }

            } else {
                Utils.smartWalk(PATH_TO_OBELISK);
                Player player = Players.getLocal();
                Execution.delayUntil(() -> !player.isMoving(), 1000, 8000);
            }
        }
    }
}
