package com.runemate.warrior55.zammy.gui;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.warrior55.zammy.main.ZammyWineGrabber;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;

public class Controller implements Initializable {

    private final ZammyWineGrabber bot;

    @FXML
    private RadioButton memberRadio, presetRadio1, presetRadio2, eatRadio, prayRadio;

    @FXML
    private Slider healthSlider, praySlider;

    @FXML
    private Spinner failedPicksSpinner;

    @FXML
    private Button startButton;
    
    @FXML
    private Label eatLabel, prayLabel;

    public Controller(ZammyWineGrabber zwg) {
        bot = zwg;
    }

    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 3);
        failedPicksSpinner.setValueFactory(valueFactory);
        toggleGroup = new ToggleGroup();
        presetRadio1.setToggleGroup(toggleGroup);
        presetRadio2.setToggleGroup(toggleGroup);
        if (Environment.isOSRS()) {
            eatRadio.setDisable(true);
            prayRadio.setDisable(true);
            healthSlider.setDisable(true);
            praySlider.setDisable(true);
            eatLabel.setDisable(true);
            prayLabel.setDisable(true);
        } else {
            eatRadio.selectedProperty().addListener((obs, old, cur) -> {
                if (!cur) {
                    healthSlider.setDisable(true);
                    eatLabel.setDisable(true);
                } else {
                    healthSlider.setDisable(false);
                    eatLabel.setDisable(false);
                }
            }
            );
            prayRadio.selectedProperty().addListener((obs, old, cur) -> {
                if (!cur) {
                    praySlider.setDisable(true);
                    prayLabel.setDisable(true);
                } else {
                    praySlider.setDisable(false);
                    prayLabel.setDisable(false);
                }
            }
            );
        }
        startButton.setOnAction((event) -> {
            bot.setIsMember(memberRadio.isSelected());
            bot.setPreset(((RadioButton) toggleGroup.getSelectedToggle()).getText().contains("1") ? 1 : 2);
            bot.setEatAt((int) healthSlider.getValue());
            bot.setPrayAt((int) praySlider.getValue());
            bot.setHopAfterFails((int) failedPicksSpinner.getValue());
            bot.resume();
            startButton.setDisable(true);
        }
        );
    }
}
