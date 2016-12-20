
package tasks.common;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.location.Coordinate;
import main.EggSpawner;

public class Constants {
    
    public static final String[] POTION_NAMES = new String[]{
        "Summoning potion (4)", "Summoning potion (3)",
        "Summoning potion (2)", "Summoning potion (1)"};

    public static final String SCROLL_NAME = "Egg spawn scroll";

    public static final String POUCH_NAME = "Spirit spider pouch";
    
    public static final Coordinate BANK_COORD = new Coordinate(3164, 3454);
    
    public static final EggSpawner BOT = (EggSpawner) Environment.getBot();
}
