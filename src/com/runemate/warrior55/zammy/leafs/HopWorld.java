package com.runemate.warrior55.zammy.leafs;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.local.Skills;
import com.runemate.game.api.hybrid.local.Worlds;
import com.runemate.game.api.hybrid.local.hud.interfaces.WorldHop;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;
import java.util.HashMap;
import java.util.stream.IntStream;

public class HopWorld extends LeafTask {

    private int world;
    private final ZammyWineGrabber bot;
    private final HashMap <Integer, Integer> map = new HashMap();

    public HopWorld(ZammyWineGrabber zwg) {
        bot = zwg;
        map.put(48, 2400);
        map.put(30, 2000);
        map.put(86, 1500);
        map.put(114, 1500);
        map.put(353, 1250);
        map.put(366, 1500);
        map.put(373, 1750);
        map.put(349, 2000);
        map.put(361, 2000);
        map.put(381, 500);
        map.put(385, 750);
    }

    @Override
    public void execute() {
        int currentWorld = Worlds.getCurrent();
        if (world <= 0) {
            world = currentWorld;
        } else if (currentWorld != world) {
            if (WorldHop.hopTo(world)) {
                bot.setFailedPicks(0);
                Execution.delay(5000);
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
            world = Worlds.newQuery().filter((x) ->
                        !x.isLegacyOnly() && !x.isDeadman()
                        && !x.isPVP() && !x.isBounty() && x.getId() != currentWorld
                        && isEnoughTotalLevel(x.getId()))
                    .results().random().getId();
        } else {
            world = Worlds.newQuery().filter((x) ->
                        !x.isLegacyOnly() && !x.isDeadman()
                        && !x.isPVP() && !x.isBounty() && !x.isMembersOnly()
                        && x.getId() != currentWorld && isEnoughTotalLevel(x.getId()))
                    .results().random().getId();
        }
    }
    
    private int getTotalLevel() {
        if (Environment.isOSRS()) {
            return IntStream.of(Skills.getBaseLevels()).sum() - 2;
        }
        return bot.getPlayer().getTotalLevel();
    }
    
    private boolean isEnoughTotalLevel(int world) {
        if (map.containsKey(world)) {
            if (map.get(world) > getTotalLevel()) {
                return false;
            }
        }
        return true;
    }
    
    /*private boolean isEOC() {
        return !CombatMode.LEGACY.isCurrent();
    }
    
    private boolean isLegacy() {
        return InterfaceMode.LEGACY.isCurrent();
    }*/
    
}
