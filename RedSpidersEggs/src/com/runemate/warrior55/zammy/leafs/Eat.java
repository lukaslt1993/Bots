
package com.runemate.warrior55.zammy.leafs;

import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.script.framework.tree.LeafTask;

public class Eat extends LeafTask {
    
    private final SpriteItem food;
    
    public Eat (SpriteItem food) {
        this.food = food;
    }   
    
    @Override
    public void execute () {
        food.click();
    }
}
