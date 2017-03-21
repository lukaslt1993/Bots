
package com.runemate.warrior55.zammy.leafs;

import com.runemate.game.api.hybrid.entities.details.Interactable;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.script.framework.tree.LeafTask;

public class HoverMouse extends LeafTask {

    private final Interactable point;
    
    public HoverMouse(Interactable i) {
        point = i;
    }
    
    @Override
    public void execute() {
        Mouse.move(point);
    }
}
