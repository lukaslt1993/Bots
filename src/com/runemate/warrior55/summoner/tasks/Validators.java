package com.runemate.warrior55.summoner.tasks;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.rs3.local.hud.interfaces.LootInventory;
import com.runemate.game.api.rs3.local.hud.interfaces.MakeXInterface;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.common.Constants;

class Validators {

    private final Summoner bot;
    private final Timer timer = new Timer(50000);
    
    Validators(Summoner s) {
        bot = s;   
    }
    
    private boolean setOrCheckInitialPos() {
        if (bot.getInitialPos() == null) {
            Player player = Players.getLocal();
            
            if (player != null) {
                bot.setInitialPos(player.getPosition());
            }
        }
        
        return bot.getInitialPos() != null;
    }

    boolean isBank() {
        if (bot.getType().equals("Spawn") && setOrCheckInitialPos()) {
            String pouchName = bot.getPouchName();
            return !LootInventory.isOpen()
                    && (Bank.isOpen()
                    || !Inventory.containsAnyOf(bot.getScrollName())
                    || pouchName.equals("Spirit cobra pouch")
                    && !Inventory.containsAnyOf("Egg")
                    || Inventory.isFull()
                    && !pouchName.equals("Spirit cobra pouch")
                    || Players.getLocal().getFamiliar() == null
                    && !Inventory.containsAnyOf(pouchName)
                    || Summoning.getPoints() < 1
                    && !Inventory.containsAnyOf(Constants.POTION_NAMES));
        }

        return false;
    }

    boolean isPick() {
        if (bot.getType().equals("Spawn") && setOrCheckInitialPos()) {
            String[] s = bot.getLootNames();

            if (s != null) {
                bot.setAllLoot(GroundItems.newQuery().names(s).results());

                if (bot.isLootAll()) {
                    long time = timer.getElapsedTime();
                    
                    if (time == 0 || time == 50000) {
                        timer.reset();
                        timer.start();
                    }
    
                    LocatableEntityQueryResults<GroundItem> leqr = bot.getAllLoot();
                    return LootInventory.isOpen()
                            || !leqr.isEmpty()
                            && (leqr.size() >= Inventory.getEmptySlots()
                            || time == 50000 || Players.getLocal().distanceTo(bot.getInitialPos()) > 4)
                            && !isBank();

                } else {
                    bot.setLoot(bot.getAllLoot()./*first()*/nearest());
                    return bot.getLoot() != null && !isBank();
                }
            }
        }

        return false;
    }

    boolean isRestore() {
        return setOrCheckInitialPos() && bot.getType().equals("Spawn")
                && (Summoning.getPoints() < 1 || Summoning.getSpecialMovePoints() < 6)
                && !isBank() && !isPick();
    }

    boolean isSummon() {
        return setOrCheckInitialPos() && bot.getType().equals("Spawn") && !isBank() && !isPick() && !isRestore() && Players.getLocal().getFamiliar() == null;
    }

    boolean isSpawn() {
        return setOrCheckInitialPos() && bot.getType().equals("Spawn") && !isBank() && !isPick() && !isSummon() && !isRestore();
    }

    boolean isTeleport() {
        Player player = Players.getLocal();
        return player != null
                && bot.getType().equals("Summon")
                && bot.getSummonMethod().equals("Ring of Kinship")
                && Summoning.getMinutesRemaining() > 0
                && (Inventory.getItems(Constants.SUMM_STUFF).size() == 3
                && !Inventory.containsAnyOf(Constants.POUCH_PATTERN)
                || Inventory.getItems(Constants.POUCH_PATTERN).size() >= 27)
                && player.distanceTo(Constants.TRAP_DOOR_COORD) > 800
                && player.distanceTo(Constants.BARBARIAN_BANK_COORD) < 200;
    }

    boolean isInteractTrapDoor() {
        Player player = Players.getLocal();
        return player != null && bot.getType().equals("Summon")
                && GameObjects.newQuery().names("Obelisk").results().nearest() == null
                && bot.getSummonMethod().equals("Ring of Kinship")
                && player.distanceTo(Constants.TRAP_DOOR_COORD) < 200;
    }

    boolean isInfuse() {
        GameObject obelisk = GameObjects.newQuery().names("Obelisk").results().nearest();
        return Players.getLocal() != null
                && bot.getType().equals("Summon")
                && (Inventory.getItems(Constants.POUCH_PATTERN).size() >= 27
                || !Inventory.containsAnyOf(Constants.POUCH_PATTERN)
                && Inventory.getItems(Constants.SUMM_STUFF).size() == 3)
                && (bot.getSummonMethod().equals("Taverley") || obelisk != null && (obelisk.isVisible() || Camera.turnTo(obelisk)))
                || MakeXInterface.isOpen();
    }

    boolean isSummonKyatt() {
        Player player = Players.getLocal();
        return player != null
                && bot.getType().equals("Summon")
                && bot.getSummonMethod().equals("Ring of Kinship")
                && Inventory.containsAnyOf("Spirit kyatt pouch")
                && Summoning.getMinutesRemaining() <= 0;
    }

    boolean isBankBarbarian() {
        Player player = Players.getLocal();
        int pouchesCount = Inventory.getItems(Constants.POUCH_PATTERN).size();
        int sumStuffCount = Inventory.getItems(Constants.SUMM_STUFF).size();
        return player != null
                && bot.getType().equals("Summon")
                && bot.getSummonMethod().equals("Ring of Kinship")
                && (player.distanceTo(Constants.TRAP_DOOR_COORD) > 800
                && pouchesCount < 27
                && !(pouchesCount <= 0 && sumStuffCount == 3)
                || Summoning.getMinutesRemaining() <= 0
                && !Inventory.containsAnyOf("Spirit kyatt pouch"));
    }
    
    boolean isBankTaverley() {
        Player player = Players.getLocal();
        int pouchesCount = Inventory.getItems(Constants.POUCH_PATTERN).size();
        int sumStuffCount = Inventory.getItems(Constants.SUMM_STUFF).size();
        return player != null
                && bot.getType().equals("Summon")
                && bot.getSummonMethod().equals("Taverley")
                && pouchesCount < 28
                && !(pouchesCount <= 0 && sumStuffCount == 3);
    }
}
