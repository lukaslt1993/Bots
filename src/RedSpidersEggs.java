
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

public class RedSpidersEggs extends LoopingBot {

    private Coordinate bankCoord = new Coordinate(3163, 3454);

    private String[] potsAndScroll = new String[]{"Summoning potion (4)", "Summoning potion (3)", "Summoning potion (2)", "Summoning potion (1)", "Egg spawn scroll"};

    private String[] pots = new String[]{"Summoning potion (4)", "Summoning potion (3)", "Summoning potion (2)", "Summoning potion (1)"};

    private String pouch = "Spirit spider pouch";

    private enum State {
        bank, summon, spawn, pick, restore, walkToBank, outOfStuff;
    }

    private int counter = 0;

    @Override
    public void onStop() {

    }

    @Override
    public void onStart(String... args) {
        setLoopDelay(150, 600);
    }

    @Override
    public void onLoop() {
        if (RuneScape.isLoggedIn() && Skill.CONSTITUTION.getExperience() > 0) {
            switch (getCurrentState()) {
                case bank:
                    bank();
                    break;
                case summon:
                    summon();
                    break;
                case spawn:
                    spawn();
                    break;
                case pick:
                    pick();
                    break;
                case restore:
                    restore();
                    break;
                case walkToBank:
                    walk(bankCoord);
                    break;
                case outOfStuff:
                    stop();
                    break;
            }
        }
    }

    private State getCurrentState() {
        if (!Inventory.containsAnyOf("Egg spawn scroll") || Inventory.isFull()) {

            if (isVisible("Well of Goodwill") || Bank.isOpen()) {
                return State.bank;

            } else {
                return State.walkToBank;
            }

        } else if (pick()) {
            return State.pick;

        } else if (Familiars.getLoaded().size() > 0) {

            if (isRestore()) {
                return State.restore;

            } else {
                return State.spawn;
            }

        } else {
            return State.summon;
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

            do {

            } while (!bank.interact("Open Bank", bank.getDefinition().getName()) && ++counter % 10 != 0);

            Execution.delayUntil(() -> Bank.isOpen(), 500, 5000);

        } else {

            if (!Inventory.containsAnyOf("Egg spawn scroll")) {

                if (!Bank.containsAnyOf("Egg spawn scroll")) {
                    pause();

                } else {

                    do {

                    } while (!Bank.withdraw("Egg spawn scroll", 9999999) && ++counter % 10 != 0);

                }
            }

            do {

            } while (!Bank.depositAllExcept(potsAndScroll) && ++counter % 10 != 0);

            if (Inventory.getEmptySlots() < 20) {

                do {

                } while (!Bank.depositAllExcept("Egg spawn scroll") && ++counter % 10 != 0);

            }

            if (!Inventory.containsAnyOf(Pattern.compile("Summoning potion \\([3-4]\\)"))) {

                for (String s : pots) {

                    if (Bank.containsAnyOf(s)) {

                        do {

                        } while (!Bank.withdraw(s, 1) && ++counter % 10 != 0);

                        break;
                    }
                }
            }

            if (Familiars.getLoaded().size() == 0) {

                if (Bank.containsAnyOf(pouch)) {

                    do {

                    } while (!Bank.withdraw(pouch, 1) && ++counter % 10 != 0);

                } else {
                    pause();
                }
            }

            do {

            } while (!Bank.close() && ++counter % 10 != 0);

        }
    }

    private void summon() {
        if (Inventory.containsAnyOf("Spirit spider pouch")) {

            do {

            } while (!Inventory.getItems("Spirit spider pouch").first().click() && ++counter % 10 != 0);

            do {
                try {
                    Thread.sleep(250);
                } catch (Exception ex) {

                }
            } while (Familiars.getLoaded().size() == 0 && ++counter % 10 != 0);

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
        GroundItem eggs = GroundItems.newQuery().names("Red spiders' eggs").results().nearest();
        if (eggs != null) {

            if (eggs.isVisible() || Camera.turnTo(eggs)) {
                int usedSlots = Inventory.getUsedSlots();

                for (int i = 0; i < 10; i++) {

                    if (eggs.interact("Take", eggs.getDefinition().getName())) {
                        Execution.delayUntil(() -> Inventory.getUsedSlots() > usedSlots, 500, 5000);
                        return true;
                    }

                }
                Coordinate eggsCoordinates = eggs.getPosition();

                if (eggsCoordinates != null) {
                    walk(eggsCoordinates);
                }

                return true;

            } else {
                return false;
            }

        } else {
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
            try {
                Thread.sleep(500);
            } catch (Exception ex) {

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
            } while ((path == null || !path.step()) && ++counter % 10 != 0);

            walk(new Coordinate(c.getX() + 1, c.getY()));
        }
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
