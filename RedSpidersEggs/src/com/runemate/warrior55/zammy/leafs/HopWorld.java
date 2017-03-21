package com.runemate.warrior55.zammy.leafs;

import com.runemate.game.api.hybrid.local.Worlds;
import com.runemate.game.api.hybrid.local.hud.interfaces.WorldHop;
import com.runemate.game.api.script.framework.tree.LeafTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class HopWorld extends LeafTask {

    private int world;
    private final ZammyWineGrabber bot;

    public HopWorld(ZammyWineGrabber zwg) {
        bot = zwg;
    }

    @Override
    public void execute() {
        int currentWorld = Worlds.getCurrent();
        if (world <= 0) {
            world = currentWorld;
        } else if (currentWorld != world) {
            if (WorldHop.hopTo(world)) {
                bot.setFailedPicks(0);
            }
            /*if (GameEvents.Universal.LOBBY_HANDLER.isEnabled()) {
                GameEvents.Universal.LOBBY_HANDLER.disable();
            } else if (RuneScape.isLoggedIn()) {
                RuneScape.logout(true);
            } else if (!WorldSelect.isOpen()) {
                WorldSelect.open();
            } else if (!WorldSelect.isSelected(world)) {
                WorldSelect.select(world);
            } else {
                GameEvents.Universal.LOBBY_HANDLER.enable();
                bot.setFailedPicks(0);
            }*/
        } else if (bot.isMember()) {
            world = Worlds.newQuery().filter((x) -> !x.isPVP() && !x.isBounty() && x.getId() != currentWorld).results().random().getId();
        } else {
            world = Worlds.newQuery().filter((x) -> !x.isPVP() && !x.isBounty() && !x.isMembersOnly() && x.getId() != currentWorld).results().random().getId();
        }
    }
}
