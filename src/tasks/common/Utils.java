
package tasks.common;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;

public class Utils {

    public static void walk(Coordinate c) {
        BresenhamPath path = BresenhamPath.buildTo(c);

        if (path != null) {
            path.step();

        } else {
            Environment.getBot().stop();
        }
    }
    
}
