
package com.runemate.warrior55.spawner.gui;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.warrior55.spawner.main.EggSpawner;
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

public class Controller implements Initializable {

    @FXML
    private ComboBox<String> modeSelector;  // Creating object for the ComboBox in the GUI
                                            // Make sure to define the type so you don't have to cast it later on
                                            // Any objects that are in your GUI are in the package javafx.*
                                            // And ALWAYS add '@FXML' when creating your GUI items in the controller
                                            // Do NOT define what buttons and stuff do in the fxml file itself, always do it in the controller
                                            // And ALWAYS define the controller of the GUI in the main class when loading the botInterface

    @FXML
    private Button startButton; // Creating object for the 'Start' button in the GUI
    
    private final EggSpawner BOT = (EggSpawner) Environment.getBot();
    private final ObservableList<String> modes = FXCollections.observableArrayList("Eggs", "Fruits"); // Creating a final list for the list of modes

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       startButton.setOnAction(handleButtonClicked()); //-- Setting the action to be performed when the 'Start' button is clicked
       modeSelector.getItems().setAll(modes); //-   -   - - Populating the ComboBox with the list we made above
       modeSelector.getSelectionModel().selectFirst(); // - Setting the default value, so the user can't select a 'blank' or 'null' value
    }   
    
    public EventHandler<ActionEvent> handleButtonClicked() {
        return event -> {
            String mode = modeSelector.getSelectionModel().getSelectedItem(); // Getting the selected item. Note, no casting.

            if (mode.equals("Eggs")) {
                BOT.setPouchName("Spirit spider pouch");
                BOT.setScrollName("Egg spawn scroll");
                BOT.setLootNames(new String[]{"Red spiders' eggs"});

            } else {
                BOT.setPouchName("Fruit bat pouch");
                BOT.setScrollName("Fruitfall scroll");
                BOT.setLootNames(new String[]{"Papaya fruit", "Orange"});
            }
            
            startButton.setDisable(true);

            if (BOT.isPaused()) {
                BOT.resume();
            }
        };
    }
    
}
