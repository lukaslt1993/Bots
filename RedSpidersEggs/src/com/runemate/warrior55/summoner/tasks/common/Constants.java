
package com.runemate.warrior55.summoner.tasks.common;

import com.runemate.game.api.hybrid.location.Coordinate;
import java.util.regex.Pattern;

public class Constants {
    
    public static final String[] POTION_NAMES = new String[]{
        "Summoning potion (4)", "Summoning potion (3)",
        "Summoning potion (2)", "Summoning potion (1)"};
    
    //public static final Coordinate BANK_COORD = new Coordinate(3164, 3454);
    
    public static final Pattern BANK_PATTERN = Pattern.compile("^.*Bank.*$|^Use$");
    
    public static final Coordinate BARBARIAN_BANK_COORD = new Coordinate(3449, 3719);
    
    public static final Coordinate TRAP_DOOR_COORD = new Coordinate(2328, 3644);
    
    public static final Pattern POUCH_PATTERN = Pattern.compile("^.* pouch$");
    
    public static final Pattern SUMM_STUFF = Pattern.compile("Spirit shards|Pouch|^.* charm$");
}
