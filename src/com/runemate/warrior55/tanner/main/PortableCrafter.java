package com.runemate.warrior55.tanner.main;

import com.runemate.game.api.script.framework.tree.TreeBot;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.tanner.branches.RootBranch;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class PortableCrafter extends TreeBot {

    private Boolean isMember = null;

    @Override
    public void onStart(String... args) {
        setLoopDelay(25, 50);
        if (isMember == null) {
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Dialog");
                alert.setHeaderText("Are you a member");

                ButtonType yes = new ButtonType("Yes");
                ButtonType no = new ButtonType("No");

                alert.getButtonTypes().setAll(yes, no);

                Optional<ButtonType> result = alert.showAndWait();
                isMember = result.get() == yes;
            });
        }
    }

    @Override
    public TreeTask createRootTask() {
        return new RootBranch();
    }

    public boolean isMember() {
        return isMember;
    }

}
