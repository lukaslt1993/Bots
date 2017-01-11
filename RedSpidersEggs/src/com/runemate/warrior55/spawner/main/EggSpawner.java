package com.runemate.warrior55.spawner.main;

import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.framework.task.TaskBot;
import com.runemate.warrior55.spawner.gui.Controller;
import com.runemate.warrior55.spawner.tasks.BankTask;
import com.runemate.warrior55.spawner.tasks.PickTask;
import com.runemate.warrior55.spawner.tasks.RestoreTask;
import com.runemate.warrior55.spawner.tasks.SpawnTask;
import com.runemate.warrior55.spawner.tasks.SummonTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class EggSpawner extends TaskBot implements EmbeddableUI {

    private GroundItem loot;
    
    private String scrollName;

    private String pouchName;
    
    private String[] lootNames;
   
    private int noDropCounter = 0;
    
    private ObjectProperty<Node> botInterfaceProperty;
    
    public EggSpawner() {
        setEmbeddableUI(this);
    }

    public GroundItem getLoot() {
        return loot;
    }

    public void setLoot(GroundItem loot) {
        this.loot = loot;
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

    @Override
    public ObjectProperty<? extends Node> botInterfaceProperty() {
        if (botInterfaceProperty == null) { // Check if there is no interface open
            FXMLLoader loader = new FXMLLoader();
            loader.setController(new Controller());
            
            try {
                Node node = loader.load(getPlatform().invokeAndWait(() -> Resources.getAsStream("com/runemate/warrior55/spawner/gui/GUI.fxml")));
                botInterfaceProperty = new SimpleObjectProperty<>(node);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        return botInterfaceProperty;
    }
    
    @Override
    public void onStart(String... args) {
        setLoopDelay(25, 50);
        add(new BankTask(), new PickTask(), new RestoreTask(), new SpawnTask(), new SummonTask());
        pause();
    }
}
