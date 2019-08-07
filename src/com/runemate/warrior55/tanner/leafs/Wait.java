
package com.runemate.warrior55.tanner.leafs;

import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;

public class Wait extends LeafTask {

    @Override
    public void execute() {
        Execution.delay(30000);
    }
}
