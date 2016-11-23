
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.web.WebPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.rs3.region.Familiars;
import com.runemate.game.api.script.framework.LoopingScript;
import java.util.regex.Pattern;

public class RedSpidersEggs extends LoopingScript {

    private Coordinate bankCoord;

    private String[] potsAndScroll = new String[]{"Summoning potion (4)", "Summoning potion (3)", "Summoning potion (2)", "Summoning potion (1)", "Egg spawn scroll"};

    private String[] pots = new String[]{"Summoning potion (4)", "Summoning potion (3)", "Summoning potion (2)", "Summoning potion (1)"};

    private String pouch = "Spirit spider pouch";

    private enum State {
        bank, summon, spawn, pick, restore, walkToBank;
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onStart(String... args) {
        setLoopDelay(150, 600);
        bankCoord = Players.getLocal().getPosition();
        //System.out.println(Summoning.Familiar.SPIRIT_SPIDER.getName());
        //System.out.println(Familiars.getLoaded().size());
    }

    @Override
    public void onLoop() {
        //System.out.println(getCurrentState());
        switch (getCurrentState()) {
            case bank:
                bank(false);
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
                walkToBank();
                break;
        }
    }

    private State getCurrentState() {
        if (Inventory.isEmpty() || Inventory.isFull()) {
            /*!*/
            if (isVisible("Well of Goodwill")) {
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

    private void bank(boolean withdrawPouch) {
        GameObject bank = GameObjects.newQuery().names("Well of Goodwill").results().nearest();
        bank.interact("Open Bank", bank.getDefinition().getName());
        //Bank.open();
        Bank.depositAllExcept(potsAndScroll);
        if (!Inventory.contains(Pattern.compile("Summoning potion \\([3-4]\\)"))) {
            for (String s : pots) {
                if (Bank.withdraw(s, 1)) {
                    break;
                }
            }
        }
        if (withdrawPouch) {
            Bank.withdraw(pouch, 1);
        }
        Bank.close();
        do {
            
        } while (Bank.isOpen());
        //System.out.println("Bank closed");
    }

    private void summon() {
        if (Inventory.contains("Spirit spider pouch")) {
            Inventory.getItems("Spirit spider pouch").first().click();
        } else {
            if (isVisible("Well of Goodwill")) {
                bank(true);  
                /*do {
                    
                } while (!Inventory.contains("Spirit spider pouch"));*/
            } else {
                walkToBank();
                bank(true);
                /*do {
                    
                } while (!Inventory.contains("Spirit spider pouch"));*/
            }
            if (Inventory.contains("Spirit spider pouch")) {
                Inventory.getItems("Spirit spider pouch").first().click();  
            } 
        }

    }

    private void spawn() {
        Summoning.FamiliarOption.getLeftClick().select();
    }

    private boolean pick() {
        GroundItem eggs = GroundItems.newQuery().names("Red spiders' eggs").results().nearest();
        if (eggs != null) {
            if (eggs.isVisible() || Camera.turnTo(eggs)) {
                eggs.interact("Take", eggs.getDefinition().getName());
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
        /*!*/
        /*SpriteItem pot = Inventory.getItems(pots).first();
        for (int i = 0; i < 4; i++) {
            pot.click();
        }*/
        Inventory.getItems(pots).first().click();
    }

    private void walkToBank() {
        final WebPath path = Traversal.getDefaultWeb().getPathBuilder().buildTo(bankCoord);
        if (path != null) {
            path.step();
        }
    }

}
