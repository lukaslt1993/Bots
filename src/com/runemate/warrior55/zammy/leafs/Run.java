package com.runemate.warrior55.zammy.leafs;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class Run extends LeafTask {

    private Path path;
    private final Coordinate coord;
    private final Coordinate[] coords;
    private final boolean Bresenham;
    private final ZammyWineGrabber bot;
    
    public Run(Coordinate c, boolean Bresenham, ZammyWineGrabber zwg) {
        coord = c;
        coords = null;
        this.Bresenham = Bresenham;
        bot = zwg;
    }
    
    public Run(Coordinate[] c, ZammyWineGrabber zwg) {
        coords = c;
        coord = null;
        this.Bresenham = false;
        bot = zwg;
    }

    @Override
    public void execute() {
        if (checkOrGeneratePath()) {
            path.step(false);
        } else {
            Execution.delay(30000);
            if (checkOrGeneratePath()) {
                path.step(false);
            } else {
                bot.showAlertAndStop("Can not generate walking path; was going to " + coord == null ? "safe spot" : coord.getX() + ", " + coord.getY() + "." );
            }
            //throw new IllegalStateException("Can not generate walking path");
        }
    }
    
    
    public boolean checkOrGeneratePath() {
        if (path == null || path.getNext() == null) {
            if (Bresenham) {
                path = BresenhamPath.buildTo(coord);
            } else if (coord != null) {
                path = Traversal.getDefaultWeb().getPathBuilder().buildTo(coord);   
            } else {
                path = PredefinedPath.create(coords);
            }
        }
        return path != null;
    }
}
