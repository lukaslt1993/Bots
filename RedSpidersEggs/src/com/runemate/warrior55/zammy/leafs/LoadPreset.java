package com.runemate.warrior55.zammy.leafs;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class LoadPreset extends LeafTask {

    private final ZammyWineGrabber bot;

    public LoadPreset(ZammyWineGrabber zwg) {
        bot = zwg;
    }

    @Override
    public void execute() {

        if (!bot.checkInventory(true)) {
            bot.stop();
        } else if (Bank.loadPreset(bot.getPreset())) {
            if (!Execution.delayUntil(() -> bot.checkInventory(false), 5000)) {
                bot.stop();
            }
        }

    }

}
