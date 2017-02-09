package com.runemate.warrior55.summoner.main;

import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.script.framework.task.TaskBot;
import com.runemate.warrior55.summoner.gui.Controller;
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
import com.runemate.warrior55.summoner.tasks.TimerTask;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class Summoner extends TaskBot implements EmbeddableUI {

    private GroundItem loot;

    private LocatableEntityQueryResults<GroundItem> allLoot;

    private String scrollName;

    private String pouchName;

    private String[] lootNames;

    private int noDropCounter = 0;

    private boolean usingPresets;

    private boolean lootAll;

    private String type;

    private String summonMethod;
    
    public static long totalUsage = 0;

    public final Timer TIMER = new Timer(Long.MAX_VALUE);

    private ObjectProperty<Node> botInterfaceProperty;

    public Summoner() {
        setEmbeddableUI(this);
    }

    public GroundItem getLoot() {
        return loot;
    }

    public void setLoot(GroundItem loot) {
        this.loot = loot;
    }

    public LocatableEntityQueryResults<GroundItem> getAllLoot() {
        return allLoot;
    }

    public void setAllLoot(LocatableEntityQueryResults<GroundItem> allLoot) {
        this.allLoot = allLoot;
    }

    public String getScrollName() {
        return scrollName;
    }

    public void setScrollName(String scrollName) {
        this.scrollName = scrollName;
    }

    public String getPouchName() {
        return pouchName;
    }

    public void setPouchName(String pouchName) {
        this.pouchName = pouchName;
    }

    public String[] getLootNames() {
        return lootNames;
    }

    public void setLootNames(String[] lootNames) {
        this.lootNames = lootNames;
    }

    public int getNoDropCounter() {
        return noDropCounter;
    }

    public void setNoDropCounter(int noDropCounter) {
        this.noDropCounter = noDropCounter;
    }

    public boolean isUsingPresets() {
        return usingPresets;
    }

    public void setUsingPresets(boolean b) {
        usingPresets = b;
    }

    public boolean isLootAll() {
        return lootAll;
    }

    public void setLootAll(boolean b) {
        lootAll = b;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSummonMethod() {
        return summonMethod;
    }

    public void setSummonMethod(String summonMethod) {
        this.summonMethod = summonMethod;
    }

    @Override
    public ObjectProperty<? extends Node> botInterfaceProperty() {
        if (botInterfaceProperty == null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(new Controller());

            try {
                Node node = loader.load(getPlatform().invokeAndWait(() -> Resources.getAsStream("com/runemate/warrior55/summoner/gui/View.fxml")));
                botInterfaceProperty = new SimpleObjectProperty<>(node);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        return botInterfaceProperty;
    }

    @Override
    public void onStart(String... args) {
        pause();
        setLoopDelay(25, 50);
        add(new BankTask(), new PickTask(), new RestoreTask(), new SpawnTask(), new SummonTask(), new TeleportTask(), new InteractTrapDoorTask(), new InfuseTask(), new BankBarbarianTask(), new SummonKyattTask(), new BankTaverleyTask(), new TimerTask());
        try {
            URLConnection connection = new URL("http://warrior55.byethost12.com/?id=" + Environment.getForumId()).openConnection();
            // to treat JAVA like normal browser
            connection.addRequestProperty("Cookie", "__test=dbc5a27a82164d79234d9578423a81c0");
            InputStream response = connection.getInputStream();
            Scanner scanner = new Scanner(response);
            totalUsage = scanner.nextLong();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        TIMER.start();
    }

    @Override
    public void onPause() {
        TIMER.stop();
    }

    @Override
    public void onResume() {
        TIMER.start();
    }

    @Override
    public void onStop() {
        try {
            URLConnection connection = new URL("http://warrior55.byethost12.com/?id="
                    + Environment.getForumId()
                    + "&usage=" + TIMER.getElapsedTime()).openConnection();
            // to treat JAVA like normal browser
            connection.addRequestProperty("Cookie", "__test=dbc5a27a82164d79234d9578423a81c0");
            // to trigger HTTP GET request
            connection.getInputStream();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
