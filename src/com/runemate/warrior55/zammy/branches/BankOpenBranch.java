package com.runemate.warrior55.zammy.branches;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.tanner.leafs.OpenBank;
import com.runemate.warrior55.zammy.leafs.LoadPreset;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;

public class BankOpenBranch extends BranchTask {
    
    private final ZammyWineGrabber bot;
    private final OpenBank openBank = new OpenBank(); 
    private final LoadPreset loadPreset;
    
    public BankOpenBranch(ZammyWineGrabber zwg) {
        bot = zwg;
        loadPreset = new LoadPreset(bot);
    }
    
    @Override
    public TreeTask successTask() {
        return loadPreset;
    }

    @Override
    public TreeTask failureTask() {
        return openBank;
    }

    @Override
    public boolean validate() {
        return Bank.isOpen();
    }
}
