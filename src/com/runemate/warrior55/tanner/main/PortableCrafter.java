
package com.runemate.warrior55.tanner.main;

import com.runemate.warrior55.tanner.branches.RootBranch;
import com.runemate.game.api.script.framework.tree.TreeBot;
import com.runemate.game.api.script.framework.tree.TreeTask;

public class PortableCrafter extends TreeBot {

        @Override
        public void onStart(String... args) {
            setLoopDelay(25, 50);
        }
    
	@Override
	public TreeTask createRootTask() {
            return new RootBranch();
	}
}