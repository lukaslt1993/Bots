
package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.leafs.HoverMouse;
import com.runemate.warrior55.zammy.leafs.WaitOrClickWine;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;
import java.awt.Point;

// Note - This class isn't used, just a possibility for the future
public class MouseHoveredBranch extends BranchTask {
    
    private final ZammyWineGrabber bot;
    private final HoverMouse hoverMouse; 
    private final WaitOrClickWine waitOrClickWine;
    
    public MouseHoveredBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        waitOrClickWine = new WaitOrClickWine(bot);
        hoverMouse = new HoverMouse(bot.getWineCoord());
    }
    
    @Override
    public TreeTask successTask() {
        return waitOrClickWine;
    }

    @Override
    public TreeTask failureTask() {
        return hoverMouse;
    }

    @Override
    public boolean validate() {
        Point p = Mouse.getPosition();
        Point p2 = bot.getWineCoord().getInteractionPoint();
        System.out.println(p.x == p2.x && p.y == p2.y);
        return p.x == p2.x && p.y == p2.y;
    }
}
