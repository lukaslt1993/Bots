
package com.runemate.warrior55.zammy.main;

import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.framework.tree.TreeBot;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.zammy.branches.RootBranch;
import com.runemate.warrior55.zammy.gui.Controller;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class ZammyWineGrabber extends TreeBot implements EmbeddableUI {
    
    private int failedPicks, preset, eatAt, prayAt, hopAfterFails;
    private boolean isMember;
    private String wineName;
    private Coordinate spotCoord, wineCoord, safeSpotCoord, furtherSafeSpotCoord, safeSpotHalfwayCoord;
    private ObjectProperty<Node> botInterfaceProperty;
    
    public ZammyWineGrabber() {
        setEmbeddableUI(this);
    }

    public int getFailedPicks() {
        return failedPicks;
    }

    public void setFailedPicks(int failedPicks) {
        this.failedPicks = failedPicks;
    }
    
    public boolean isMember() {
        return isMember;
    }
    
    public int getPreset() {
        return preset;
    }

    public void setPreset(int preset) {
        this.preset = preset;
    }

    public int getEatAt() {
        return eatAt;
    }

    public void setEatAt(int eatAt) {
        this.eatAt = eatAt;
    }

    public String getWineName() {
        return wineName;
    }

    public void setWineName(String wineName) {
        this.wineName = wineName;
    }

    public Coordinate getSpotCoord() {
        return spotCoord;
    }

    public void setSpotCoord(Coordinate spotCoord) {
        this.spotCoord = spotCoord;
    }

    public Coordinate getWineCoord() {
        return wineCoord;
    }

    public void setWineCoord(Coordinate wineCoord) {
        this.wineCoord = wineCoord;
    }
    
    public void setIsMember(boolean isMember) {
        this.isMember = isMember;
    }
    
    public int getPrayAt() {
        return prayAt;
    }

    public void setPrayAt(int prayAt) {
        this.prayAt = prayAt;
    }

    public int getHopAfterFails() {
        return hopAfterFails;
    }

    public void setHopAfterFails(int hopAfterFails) {
        this.hopAfterFails = hopAfterFails;
    }
    
    public Coordinate getSafeSpotCoord() {
        return safeSpotCoord;
    }

    public void setSafeSpotCoord(Coordinate safeSpotCoord) {
        this.safeSpotCoord = safeSpotCoord;
    }

    public Coordinate getFurtherSafeSpotCoord() {
        return furtherSafeSpotCoord;
    }

    public void setFurtherSafeSpotCoord(Coordinate furtherSafeSpotCoord) {
        this.furtherSafeSpotCoord = furtherSafeSpotCoord;
    }
    
    public Coordinate getSafeSpotHalfwayCoord() {
        return safeSpotHalfwayCoord;
    }

    public void setSafeSpotHalfwayCoord(Coordinate safeSpotHalfwayCoord) {
        this.safeSpotHalfwayCoord = safeSpotHalfwayCoord;
    }
    
    @Override
    public void onStart(String... args) {
        pause();
        /*setEatAt(40);
        setPreset(1);
        setIsMember(true);*/
    }

    @Override
    public TreeTask createRootTask() {
        return new RootBranch(this);
    }
    
    @Override
    public ObjectProperty<? extends Node> botInterfaceProperty() {
        if (botInterfaceProperty == null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(new Controller(this));

            try {
                Node node = loader.load(getPlatform().invokeAndWait(() -> Resources.getAsStream("com/runemate/warrior55/zammy/gui/View.fxml")));
                botInterfaceProperty = new SimpleObjectProperty<>(node);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        return botInterfaceProperty;
    }
    
}