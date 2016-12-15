
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
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.rs3.region.Familiars;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;
import java.util.regex.Pattern;

public class RedSpidersEggs extends LoopingBot {

    private final Coordinate bankCoord = new Coordinate(3164, 3454);
    
    private final String scroll = "Egg spawn scroll";

    private final String[] potsAndScroll = new String[]{"Summoning potion (4)", "Summoning potion (3)", "Summoning potion (2)", "Summoning potion (1)", scroll};

    private final String[] pots = new String[]{"Summoning potion (4)", "Summoning potion (3)", "Summoning potion (2)", "Summoning potion (1)"};

    private final String pouch = "Spirit spider pouch";

    private enum State {
        BANK, SUMMON, SPAWN, PICK, RESTORE;
    }

    private int pickedNoneTimes = 0;

    private GroundItem eggs;

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
            }
        }
    }

    private State getCurrentState() {
        if (isBank()) {
            return State.BANK;

        } else if ((eggs = GroundItems.newQuery().names("Red spiders' eggs").results().nearest()) != null) {
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

    private boolean isBank() {
        return Bank.isOpen()
                || Inventory.isFull()
                || !Inventory.containsAnyOf(scroll)
                || Familiars.getLoaded().isEmpty()
                && !Inventory.containsAnyOf(pouch);
    }

    private void bank() {
        if (Bank.isOpen() || openBank()) {

            if (checkOrWithdrawScroll() && checkOrDepositInventory()
                    && checkOrWithdrawPot() && checkOrWithdrawPouch()) {

                Bank.close();
            }
        }
    }

    private boolean openBank() {
        GameObject bank = GameObjects.newQuery().names("Well of Goodwill").results().nearest();

        if (bank != null && (bank.isVisible() || Camera.turnTo(bank))) {

            if (bank.interact("Open Bank", bank.getDefinition().getName())) {
                Execution.delayUntil(() -> Bank.isOpen(), 500, 5000);
            }
        }

        return Bank.isOpen();
    }

    private boolean checkOrWithdrawScroll() {
        if (!Inventory.containsAnyOf(scroll)) {

            if (!Bank.containsAnyOf(scroll)) {
                stop();

            } else {
                return Bank.withdraw(scroll, 9999999);
            }
        }

        return true;
    }

    private boolean checkOrDepositInventory() {
        if (Inventory.getEmptySlots() < 20) {

            if (Inventory.getQuantity(pots) < 8) {
                return Bank.depositAllExcept(potsAndScroll);

            } else {
                return Bank.depositAllExcept(scroll);
            }
        }

        return true;
    }

    private boolean checkOrWithdrawPot() {
        if (Summoning.getPoints() < 1 && !Bank.containsAnyOf(Pattern.compile("Summoning potion \\([1-4]\\)"))) {
            stop();
        }

        if (!Inventory.containsAnyOf(Pattern.compile("Summoning potion \\([3-4]\\)"))) {

            for (String s : pots) {

                if (Bank.containsAnyOf(s)) {
                    return Bank.withdraw(s, 1);
                }
            }
        }

        return true;
    }

    private boolean checkOrWithdrawPouch() {
        if (Familiars.getLoaded().isEmpty() && !Inventory.containsAnyOf(pouch)) {

            if (Bank.containsAnyOf(pouch)) {
                return Bank.withdraw(pouch, 1);

            } else {
                stop();
            }
        }

        return true;
    }

    private void summon() {
        Inventory.getItems(pouch).first().click();
        Execution.delayWhile(() -> Familiars.getLoaded().isEmpty(), 5000);
    }

    private void spawn() {
        Summoning.FamiliarOption.getLeftClick().select();
    }

    private void pick() {
        if (pickedNoneTimes > 20) {
            walk(eggs.getPosition());
        }

        if (eggs.isVisible() || Camera.turnTo(eggs)) {
            int usedSlots = Inventory.getUsedSlots();

            if (eggs.interact("Take", eggs.getDefinition().getName())) {
                Execution.delayUntil(() -> Inventory.getUsedSlots() > usedSlots, 500, 5000);
                pickedNoneTimes = 0;

            } else {
                pickedNoneTimes++;
            }

        } else {
            pickedNoneTimes++;
        }
    }

    private boolean isRestore() {
        return Summoning.getPoints() < 1 || Summoning.getSpecialMovePoints() < 6;
    }

    private void restore() {
        SpriteItemQueryResults potions = Inventory.getItems(pots);
        SpriteItem pot = potions.get(Random.nextInt(potions.size()));
        if (pot != null) {
            pot.click();

        } else {
            Execution.delayUntil(() -> !isRestore());
        }
    }

    private void walk(Coordinate c) {
        BresenhamPath path = BresenhamPath.buildTo(bankCoord);

        if (path != null) {
            path.step();
            Execution.delayUntil(() -> !Players.getLocal().isMoving(), 2500, 20000);

        } else {
            stop();
        }
    }
}
