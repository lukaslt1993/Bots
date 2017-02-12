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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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

    public final Timer timer = new Timer(Long.MAX_VALUE);

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
            connection.addRequestProperty("Cookie", "__test=" + getCookie());
            InputStream response = connection.getInputStream();
            Scanner scanner = new Scanner(response);
            totalUsage = scanner.nextLong();
        } catch (Throwable t) {
            
        }
        
        timer.start();
    }

    @Override
    public void onPause() {
        timer.stop();
    }

    @Override
    public void onResume() {
        timer.start();
    }

    @Override
    public void onStop() {
        try {
            URLConnection connection = new URL("http://warrior55.byethost12.com/?id="
                    + Environment.getForumId()
                    + "&usage=" + timer.getElapsedTime()).openConnection();
            // to treat JAVA like normal browser
            connection.addRequestProperty("Cookie", "__test=" + getCookie());
            // to trigger HTTP GET request
            connection.getInputStream();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    private String readURL (URL url) throws Exception {
        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine + "\n");
        }

        in.close();
        return response.toString();
    }

    private String getDecryptor() throws Exception {
        URL website = new URL("http://warrior55.byethost12.com/aes.js");
        return readURL(website);
    }
    
    private String getEncryptor() throws Exception {
        URL website = new URL("http://warrior55.byethost12.com");
        String content = readURL(website);
        return content.substring(content.indexOf("<script>") + 8, content.indexOf("document.cookie="));
    }
    
    private String getCookie() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        return (String) engine.eval(getDecryptor() + getEncryptor() + "toHex(slowAES.decrypt(c,2,a,b));");
    }
}
