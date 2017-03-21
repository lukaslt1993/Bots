package com.runemate.warrior55.zammy.leafs;

import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.script.framework.tree.LeafTask;
import java.util.List;

public class Run extends LeafTask {

    private Path path;
    private final Coordinate coord;
    private final Coordinate[] coords;
    private final boolean Bresenham;
    
    public Run(Coordinate c, boolean Bresenham) {
        coord = c;
        coords = null;
        this.Bresenham = Bresenham;
    }
    
    public Run(Coordinate[] c) {
        coords = c;
        coord = null;
        this.Bresenham = false;
    }

    @Override
    public void execute() {
        if (path == null || path.getNext() == null) {
            if (Bresenham) {
                path = BresenhamPath.buildTo(coord);
            } else if (coord != null) {
                path = Traversal.getDefaultWeb().getPathBuilder().buildTo(coord);   
            } else {
                path = PredefinedPath.create(coords);
            }
        }

        if (path != null) {
            List <? extends Locatable> l = path.getVertices();
            for (Locatable loc: l) {
                System.out.print(loc.getPosition().getX() + " " + loc.getPosition().getY() + " ");
            } 
            System.out.println(path.getNext().getPosition().getX() + " " + path.getNext().getPosition().getY());
            path.step(false);
        } else {
            throw new IllegalStateException("Can not generate walking path");
        }
    }
}
