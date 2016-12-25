
package com.runemate.warrior55.tanner.leafs;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.rs3.local.hud.interfaces.MakeXInterface;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;
import java.awt.event.KeyEvent;


public class PressSpace extends LeafTask {
    
    private final int SPACECODE = KeyEvent.VK_SPACE;
    
    @Override
    public void execute() {
        if (Keyboard.pressKey(SPACECODE)) {
            
            if (!Keyboard.releaseKey(SPACECODE)) {
                Environment.getBot().stop();
            }
            
            Execution.delayUntil(() -> !MakeXInterface.isOpen(), 2500);
        }  
    }
}
