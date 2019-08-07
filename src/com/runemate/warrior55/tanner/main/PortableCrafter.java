package com.runemate.warrior55.tanner.main;

import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.TreeBot;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.tanner.branches.RootBranch;
import com.runemate.warrior55.tanner.gui.Controller;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class PortableCrafter extends TreeBot implements EmbeddableUI {

    private boolean isMember, canContinue, canUseOwnCrafter;
    private ObjectProperty<Node> botInterfaceProperty;

    public PortableCrafter() {
        setEmbeddableUI(this);
    }
    
    @Override
    public void onStart(String... args) {
        Execution.delayUntil(() -> canContinue);
        setLoopDelay(25, 50);
    }

    @Override
    public TreeTask createRootTask() {
        return new RootBranch();
    }

    public boolean isMember() {
        return isMember;
    }

    public void setIsMember(boolean isMember) {
        this.isMember = isMember;
    }

    public void setCanContinue(boolean canContinue) {
        this.canContinue = canContinue;
    }

    @Override
    public ObjectProperty<? extends Node> botInterfaceProperty() {
        if (botInterfaceProperty == null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(new Controller(this));

            try {
                Node node = loader.load(getPlatform().invokeAndWait(() -> Resources.getAsStream("com/runemate/warrior55/tanner/gui/View.fxml")));
                botInterfaceProperty = new SimpleObjectProperty<>(node);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        return botInterfaceProperty;
    }

    public boolean canUseOwnCrafter() {
        return canUseOwnCrafter;
    }

    public void setCanUseOwnCrafter(boolean canUseOwnCrafter) {
        this.canUseOwnCrafter = canUseOwnCrafter;
    }
    
}
