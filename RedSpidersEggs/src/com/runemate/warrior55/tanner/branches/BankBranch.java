
package com.runemate.warrior55.tanner.branches;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.tanner.leafs.OpenBank;
import com.runemate.warrior55.tanner.leafs.WithdrawPreset;

public class BankBranch extends BranchTask {

    private final OpenBank OPEN_BANK_LEAF = new OpenBank();
    private final WithdrawPreset WITHDRAW_PRESET_LEAF = new WithdrawPreset();

    @Override
    public TreeTask successTask() {
        return OPEN_BANK_LEAF;
    }

    @Override
    public TreeTask failureTask() {
        return WITHDRAW_PRESET_LEAF;
    }

    @Override
    public boolean validate() {
        return !Bank.isOpen();
    }

}
