package com.runemate.warrior55.tanner.gui;

import com.runemate.warrior55.tanner.main.PortableCrafter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

public class Controller implements Initializable {

    private final PortableCrafter bot;

    @FXML
    private RadioButton memberRadio, crafterRadio;

    @FXML
    private Button startButton;

    public Controller(PortableCrafter bot) {
        this.bot = bot;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startButton.setOnAction((event) -> {
            bot.setIsMember(memberRadio.isSelected());
            bot.setCanUseOwnCrafter(crafterRadio.isSelected());
            bot.setCanContinue(true);
            startButton.setDisable(true);
        });
    }
}
