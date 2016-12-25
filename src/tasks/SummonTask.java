
package tasks;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;
import tasks.common.Constants;

public class SummonTask extends Task {
    
    private final Validators VALIDATORS = new Validators();
    
    @Override
    public boolean validate() {
        return VALIDATORS.isSummon();
    }

    @Override
    public void execute() {
        System.out.println("Summon");
        SpriteItem pouch = Inventory.getItems(Constants.POUCH_NAME).first();
        
        if (pouch != null && pouch.click()) {
            Execution.delayWhile(() -> validate(), 5000);
        }     
    }    
}
