package com.runemate.warrior55.summoner.gui;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.warrior55.summoner.main.Summoner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class Controller implements Initializable {

    @FXML
    private ComboBox<String> modeSelector;

    @FXML
    private Button startButton;

    @FXML
    private CheckBox lootAll;

    @FXML
    private CheckBox usePresets;

    @FXML
    private ComboBox<String> typeSelector;

    @FXML
    private ComboBox<String> summonMode;
    
    @FXML
    private Label spawnLabel1;
    
    @FXML
    private Label spawnLabel2;
    
    @FXML
    private Label summonLabel;

    private final Summoner BOT = (Summoner) Environment.getBot();
    private final ObservableList<String> MODES = FXCollections.observableArrayList("Spider eggs", "Cobra eggs", "Fruits"); // Creating a final list for the list of MODES
    private final ObservableList<String> TYPES = FXCollections.observableArrayList("Spawn", "Infuse");
    private final ObservableList<String> SUMMON_MODES = FXCollections.observableArrayList("Ring of Kinship", "Taverley");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startButton.setOnAction(handleButtonClicked()); 
        modeSelector.getItems().setAll(MODES); 
        modeSelector.getSelectionModel().selectFirst();
        typeSelector.getItems().setAll(TYPES);
        typeSelector.getSelectionModel().selectFirst();
        summonMode.getItems().setAll(SUMMON_MODES);
        summonMode.getSelectionModel().selectFirst();
        summonLabel.setDisable(true);
        summonMode.setDisable(true);
        typeSelector.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("Spawn")) {
                    spawnLabel1.setDisable(false);
                    spawnLabel2.setDisable(false);
                    summonLabel.setDisable(true);
                    if (!modeSelector.getSelectionModel().getSelectedItem().equals("Cobra eggs")) {
                        lootAll.setDisable(false);   
                    }
                    usePresets.setDisable(false);
                    modeSelector.setDisable(false);
                    summonMode.setDisable(true);
                    
                } else {
                    spawnLabel1.setDisable(true);
                    spawnLabel2.setDisable(true);
                    summonLabel.setDisable(false);
                    lootAll.setDisable(true);
                    usePresets.setDisable(true);
                    modeSelector.setDisable(true);
                    summonMode.setDisable(false);
                }
            }
        });
        modeSelector.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("Cobra eggs")) {
                    lootAll.setDisable(true);
                    
                } else {
                    lootAll.setDisable(false);    
                }
            }
        });
    }
    
    

    public EventHandler<ActionEvent> handleButtonClicked() {
        return event -> {
            String type = typeSelector.getSelectionModel().getSelectedItem();
            
            if (type.equals("Spawn")) {    
                String mode = modeSelector.getSelectionModel().getSelectedItem();

                if (mode.equals("Spider eggs")) {
                    BOT.setPouchName("Spirit spider pouch");
                    BOT.setScrollName("Egg spawn scroll");
                    BOT.setLootNames(new String[]{"Red spiders' eggs"});

                } else if (mode.equals("Cobra eggs")) {
                    BOT.setPouchName("Spirit cobra pouch");
                    BOT.setScrollName("Oph. incubation scroll");

                } else {
                    BOT.setPouchName("Fruit bat pouch");
                    BOT.setScrollName("Fruitfall scroll");
                    BOT.setLootNames(new String[]{"Papaya fruit", "Orange"});
                }

                BOT.setLootAll(lootAll.isSelected());
                BOT.setUsingPresets(usePresets.isSelected());
                BOT.setType("Spawn");
                
            } else {
                String summoningMode = summonMode.getSelectionModel().getSelectedItem();
                
                if (summoningMode.equals("Ring of Kinship")) {
                    BOT.setSummonMethod("Ring of Kinship");
                    
                } else {
                    BOT.setSummonMethod("Taverley");
                }
                
                BOT.setType("Summon");
            }

            startButton.setDisable(true);

            if (BOT.isPaused()) {
                BOT.resume();
            }
        };
    }

}
