package com.runemate.warrior55.summoner.gui;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.script.framework.core.LoopingThread;
import com.runemate.warrior55.summoner.main.Summoner;
import com.runemate.warrior55.summoner.tasks.BankBarbarianTask;
import com.runemate.warrior55.summoner.tasks.BankTask;
import com.runemate.warrior55.summoner.tasks.BankTaverleyTask;
import com.runemate.warrior55.summoner.tasks.InfuseTask;
import com.runemate.warrior55.summoner.tasks.InteractTrapDoorTask;
import com.runemate.warrior55.summoner.tasks.PickTask;
import com.runemate.warrior55.summoner.tasks.RestoreTask;
import com.runemate.warrior55.summoner.tasks.SpawnTask;
import com.runemate.warrior55.summoner.tasks.SummonKyattTask;
import com.runemate.warrior55.summoner.tasks.SummonTask;
import com.runemate.warrior55.summoner.tasks.TeleportTask;
import java.io.InputStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
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

    private final Summoner bot;
    private final ObservableList<String> modes = FXCollections.observableArrayList("Spider eggs", "Cobra eggs", "Fruits"); // Creating a final list for the list of modes
    private final ObservableList<String> types = FXCollections.observableArrayList("Spawn", "Infuse");
    private final ObservableList<String> summonModes = FXCollections.observableArrayList("Ring of Kinship", "Taverley");

    public Controller(Summoner s) {
        bot = s;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startButton.setOnAction(handleButtonClicked());
        modeSelector.getItems().setAll(modes);
        modeSelector.getSelectionModel().selectFirst();
        typeSelector.getItems().setAll(types);
        typeSelector.getSelectionModel().selectFirst();
        summonMode.getItems().setAll(summonModes);
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
                    bot.setPouchName("Spirit spider pouch");
                    bot.setScrollName("Egg spawn scroll");
                    bot.setLootNames(new String[]{"Red spiders' eggs"});

                } else if (mode.equals("Cobra eggs")) {
                    bot.setPouchName("Spirit cobra pouch");
                    bot.setScrollName("Oph. incubation scroll");

                } else {
                    bot.setPouchName("Fruit bat pouch");
                    bot.setScrollName("Fruitfall scroll");
                    bot.setLootNames(new String[]{"Papaya fruit", "Orange"});
                }

                bot.setLootAll(lootAll.isSelected());
                bot.setUsingPresets(usePresets.isSelected());
                bot.setType("Spawn");

            } else {
                String summoningMode = summonMode.getSelectionModel().getSelectedItem();

                if (summoningMode.equals("Ring of Kinship")) {
                    bot.setSummonMethod("Ring of Kinship");

                } else {
                    bot.setSummonMethod("Taverley");
                }

                bot.setType("Summon");
            }

            startButton.setDisable(true);
            bot.setLoopDelay(25, 50);
            bot.add(new BankTask(bot), new PickTask(bot), new RestoreTask(bot), new SpawnTask(bot), new SummonTask(bot), new TeleportTask(bot), new InteractTrapDoorTask(bot), new InfuseTask(bot), new BankBarbarianTask(bot), new SummonKyattTask(bot), new BankTaverleyTask(bot));

            try {
                URLConnection connection = new URL("http://warrior55.byethost12.com/?id=" + Environment.getForumId()).openConnection();
                // to treat JAVA like normal browser
                connection.addRequestProperty("Cookie", "__test=" + bot.getCookie());
                InputStream response = connection.getInputStream();
                Scanner scanner = new Scanner(response);
                Summoner.playedToday = scanner.nextLong();
            } catch (Throwable t) {

            }

            bot.runtime.start();

            new LoopingThread(() -> {
                Summoner.playedToday += bot.runtime.getRuntime();

                if (Summoner.playedToday > TimeUnit.HOURS.toMillis(3) && bot.getMetaData().getHourlyPrice().doubleValue() <= 0 && bot.getType().equals("Spawn")) {
                    System.err.println("[UsageMonitor] Looks like you've used your 3 free hours today!");
                    bot.stop();
                }

            }, (int) TimeUnit.SECONDS.toMillis(3)).start();

            /*if (bot.isPaused()) {
                bot.resume();
            }*/
        };
    }
}
