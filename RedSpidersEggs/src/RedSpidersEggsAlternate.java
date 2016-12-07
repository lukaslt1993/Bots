
import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.rs3.region.Familiars;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;
import java.util.regex.Pattern;

public class RedSpidersEggsAlternate extends LoopingBot {

    private Coordinate bankCoord = new Coordinate(3164, 3454);

    private String[] potsAndScroll = new String[]{"Summoning potion (4)", "Summoning potion (3)", "Summoning potion (2)", "Summoning potion (1)", "Egg spawn scroll"};

    private String[] pots = new String[]{"Summoning potion (4)", "Summoning potion (3)", "Summoning potion (2)", "Summoning potion (1)"};

    private String pouch = "Spirit spider pouch";

    private enum State {
        BANK, SUMMON, SPAWN, PICK, RESTORE, WALKTOBANK;
    }

    private int loopCounter = 0;
    
    private int pickedNoneCounter = 0;

    @Override
    public void onStop() {

    }

    @Override
    public void onStart(String... args) {
        setLoopDelay(150, 500);
    }

    @Override
    public void onLoop() {
        if (RuneScape.isLoggedIn() && Skill.CONSTITUTION.getExperience() > 0) {
            switch (getCurrentState()) {
                case BANK:
                    bank();
                    break;
                case SUMMON:
                    summon();
                    break;
                case SPAWN:
                    spawn();
                    break;
                case PICK:
                    pick();
                    break;
                case RESTORE:
                    restore();
                    break;
                case WALKTOBANK:
                    walk(bankCoord);
                    break;
            }
        }
    }

    private State getCurrentState() {
        if (!Inventory.containsAnyOf("Egg spawn scroll") || Inventory.isFull()) {

            if (isVisible("Well of Goodwill") || Bank.isOpen()) {
                return State.BANK;

            } else {
                return State.WALKTOBANK;
            }

        } else if (pick()) {
            return State.PICK;

        } else if (Familiars.getLoaded().size() > 0) {

            if (isRestore()) {
                return State.RESTORE;

            } else {
                return State.SPAWN;
            }

        } else {
            return State.SUMMON;
        }
    }

    private boolean isVisible(String name) {
        GameObject obj = GameObjects.newQuery().names(name).results().nearest();

        if (obj != null) {

            if (obj.isVisible() || Camera.turnTo(obj)) {
                return true;

            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    private void bank() {
        if (!Bank.isOpen()) {
            GameObject bank = GameObjects.newQuery().names("Well of Goodwill").results().nearest();
            Execution.delayWhile(() -> !bank.interact("Open Bank", bank.getDefinition().getName()), 20000);
            Execution.delayUntil(() -> Bank.isOpen(), 10000);
            
            if (!Bank.isOpen()) {
                walk(bankCoord);
            }

        } else {
            
            if (Summoning.getPoints() < 1 && !Bank.containsAnyOf(Pattern.compile("Summoning potion \\([1-4]\\)"))) {
                stop();    
            }

            if (!Inventory.containsAnyOf("Egg spawn scroll")) {

                if (!Bank.containsAnyOf("Egg spawn scroll")) {
                    stop();

                } else {
                    Execution.delayWhile(() -> !Bank.withdraw("Egg spawn scroll", 9999999), 25000);
                }
            }

            Execution.delayWhile(() -> !Bank.depositAllExcept(potsAndScroll), 25000);

            if (Inventory.getEmptySlots() < 20) {
                Execution.delayWhile(() -> !Bank.depositAllExcept("Egg spawn scroll"), 25000);
            }

            if (!Inventory.containsAnyOf(Pattern.compile("Summoning potion \\([3-4]\\)"))) {

                for (String s : pots) {

                    if (Bank.containsAnyOf(s)) {
                        Execution.delayWhile(() -> !Bank.withdraw(s, 1), 25000);
                        break;
                    }
                    
                }
            }

            if (Familiars.getLoaded().size() == 0) {

                if (Bank.containsAnyOf(pouch)) {
                    Execution.delayWhile(() -> !Bank.withdraw(pouch, 1), 25000);
                    
                } else {
                    stop();
                }
            }

            Execution.delayWhile(() -> !Bank.close(), 20000);
        }
    }

    private void summon() {
        if (Inventory.containsAnyOf("Spirit spider pouch")) {
            Inventory.getItems("Spirit spider pouch").first().click();
            Execution.delayWhile(() -> Familiars.getLoaded().size() == 0, 10000);

        } else if (isVisible("Well of Goodwill")) {
            bank();

        } else {
            walk(bankCoord);
            bank();
        }
    }

    private void spawn() {
        Summoning.FamiliarOption.getLeftClick().select();
    }

    private boolean pick() {
        if (pickedNoneCounter > 20) {
            walk(bankCoord);
        }
        
        GroundItem eggs = GroundItems.newQuery().names("Red spiders' eggs").results().nearest();
        if (eggs != null && !Inventory.isFull()) {

            if (eggs.isVisible() || Camera.turnTo(eggs)) {
                int usedSlots = Inventory.getUsedSlots();

                for (int i = 0; i < 10; i++) {

                    if (eggs.interact("Take", eggs.getDefinition().getName())) {
                        Execution.delayUntil(() -> Inventory.getUsedSlots() > usedSlots, 10000);
                        pickedNoneCounter = 0;
                        return true;
                    }

                }
                Coordinate eggsCoordinates = eggs.getPosition();

                if (eggsCoordinates != null) {
                    walk(eggsCoordinates);
                }

                pickedNoneCounter = 0;
                return true;

            } else {
                pickedNoneCounter++;
                return false;
            }

        } else {
            pickedNoneCounter++;
            return false;
        }
    }

    private boolean isRestore() {
        if (Summoning.getPoints() < 1 || Summoning.getSpecialMovePoints() < 6) {
            return true;

        } else {
            return false;
        }
    }

    private void restore() {
        SpriteItem pot = Inventory.getItems(pots).first();

        if (pot != null) {
            pot.click();

        } else {
            Execution.delayWhile(() -> isRestore(), 45000);
            
            if (isRestore()) {
                
                if (isVisible("Well of Goodwill")) {
                    bank();
                    
                } else {
                    walk(bankCoord);
                    bank();
                }
                
            }
        }
    }

    private void walk(Coordinate c) {
        BresenhamPath path = buildPath(c);

        if (path != null) {
            path.step();

        } else {
            int x = Players.getLocal().getPosition().getX();
            int y = Players.getLocal().getPosition().getY();

            do {
                x++;
                path = buildPath(new Coordinate(x, y));
            } while((path == null || !path.step()) && ++loopCounter % 10 != 0);

            walk(new Coordinate(c.getX() + 1, c.getY()));
        }
        
        Execution.delayUntil(() -> !Players.getLocal().isMoving(), 2500, 20000);
    }

    private BresenhamPath buildPath(Coordinate c) {
        BresenhamPath path;
        try {
            path = BresenhamPath.buildTo(bankCoord);
        } catch (Exception ex) {
            return null;
        }
        return path;
    }
}