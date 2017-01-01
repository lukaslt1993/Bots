
package com.runemate.warrior55.spawner.tasks.common;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.Players;

public class Utils {

    public static void walk(Coordinate c) {
        Coordinate currentCoord = Players.getLocal().getPosition();
        
        if (currentCoord.getX() == c.getX() && currentCoord.getY() == c.getY()) {
            return;
        }
        
        BresenhamPath path = BresenhamPath.buildTo(c);

        if (path != null) {
            path.step();

        } else {
            BresenhamPath bankPath = BresenhamPath.buildTo(Constants.BANK_COORD);
            
            if (bankPath != null) {
                bankPath.step();
                
            } else {
                Environment.getBot().stop();   
            }
        }
    }
    
}
