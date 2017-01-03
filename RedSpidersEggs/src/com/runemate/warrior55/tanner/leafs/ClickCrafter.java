package com.runemate.warrior55.tanner.leafs;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.hud.interfaces.WorldHop;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.rs3.local.hud.interfaces.MakeXInterface;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClickCrafter extends LeafTask {

    private int noCrafterCounter = 0;
    private int worldNumber = 0;
    private LocatableEntityQueryResults<GameObject> crafters;

    @Override
    public void execute() {
        if (noCrafterCounter > 5) {
            Environment.getBot().stop();
        }

        Execution.delayUntil(() -> !(crafters = GameObjects.newQuery().names("Portable crafter").results()).isEmpty(), 60000);

        if (!crafters.isEmpty()) {
            noCrafterCounter = 0;
            crafters.nearest().click();
            Execution.delayUntil(() -> MakeXInterface.isOpen(), 2500);

        } else {
            noCrafterCounter++;
            
            try {
                hopWorld();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
            
            execute();
        }
    }

    private void hopWorld() throws IOException {
        URL url = new URL("https://docs.google.com/spreadsheets/d/16Yp-eLHQtgY05q6WBYA2MDyvQPmZ4Yr3RHYiBCBj2Hc/edit#gid=915793480");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;

        while ((line = reader.readLine()) != null) {

            if (line.contains("Crafters,") && (line = reader.readLine()) != null) {
                Pattern p = Pattern.compile("\\d+");
                line = line.split(",", 2)[1];
                Matcher m = p.matcher(line);

                if (m.find()) {
                    worldNumber = Integer.parseInt(m.group());
                }

                break;
            }
        }

        reader.close();

        if (worldNumber != 0) {
            Execution.delayUntil(() -> WorldHop.hopTo(worldNumber), 60000);
        }    
    }
}
