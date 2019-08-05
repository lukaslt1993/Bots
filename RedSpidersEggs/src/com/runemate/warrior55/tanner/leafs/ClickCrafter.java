package com.runemate.warrior55.tanner.leafs;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.Worlds;
import com.runemate.game.api.hybrid.local.hud.interfaces.WorldHop;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.local.hud.interfaces.MakeXInterface;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.tree.LeafTask;
import com.runemate.warrior55.tanner.main.PortableCrafter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClickCrafter extends LeafTask {

    private int noCrafterCounter = 0;
    private int worldNumber = 0;
    private int failedClickCounter = 0;
    private int failedClickStreak = 0;
    private GameObject crafter = null;
    private final Queue<Integer> QUEUE = new LinkedList<Integer>();

    @Override
    public void execute() {
        if (noCrafterCounter > 5 || failedClickStreak > 12) {
            /*PortableCrafter bot = (PortableCrafter) Environment.getBot();

            if (bot != null) {
                System.out.println("A");
                bot.stop();

            } else {
                System.out.println("B");
                return;
            }*/
            Environment.getBot().stop();
        }

        if (crafter == null || !crafter.isValid()) {
            Execution.delayUntil(() -> (crafter = GameObjects.newQuery().names("Portable crafter").results().nearest()) != null, 60000);
        }

        if (crafter != null && failedClickCounter < 6) {
            noCrafterCounter = 0;

            if (crafter.click()) {
                failedClickCounter = 0;
                failedClickStreak = 0;
                Execution.delayUntil(() -> MakeXInterface.isOpen(), 2500);

            } else {
                failedClickCounter++;
                failedClickStreak++;
            }

        } else {
            noCrafterCounter++;

            try {
                findWorldNumber();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }

            hopWorld();
            PortableCrafter bot = (PortableCrafter) Environment.getBot();
            
            if (bot != null && bot.isRunning()) {
                execute();
            }
        }
    }

    private void findWorldNumber() throws IOException {
        URL url = new URL("https://docs.google.com/spreadsheets/d/16Yp-eLHQtgY05q6WBYA2MDyvQPmZ4Yr3RHYiBCBj2Hc/edit#gid=915793480");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;

        while ((line = reader.readLine()) != null) {

            int index = line.indexOf("Notes:");
            if (index >= 0) {
                
                /*while (!line.contains(">")) {
                    line += reader.readLine();
                }*/
                
                line = reader.readLine();
                
                /*Pattern p = Pattern.compile("\\d+");
                line = line.substring(index);
                line = line.split(",")[1];*/
                Pattern p = Pattern.compile(",\\S.*?,\\S");
                Matcher m = p.matcher(line);
                m.find();
                line = m.group();
                line = line.substring(0, line.length() - 1);
                p = Pattern.compile("\\d+");
                m = p.matcher(line);

                while (m.find()) {
                    int i = Integer.parseInt(m.group());
                    if (!QUEUE.contains(i)) {
                        QUEUE.add(i);
                    }
                }

                if (!QUEUE.isEmpty()) {
                    worldNumber = QUEUE.remove();
                    if (Worlds.getOverview(worldNumber).isMembersOnly()
                            && !((PortableCrafter)Environment.getBot()).isMember()) {
                        worldNumber = 0;
                    }
                }

                break;
            }
        }

        reader.close();
    }

    private void hopWorld() {
        //System.out.println(worldNumber);
        
        if (worldNumber != 0 && Worlds.getCurrent() != worldNumber) {
            Execution.delayUntil(() -> WorldHop.hopTo(worldNumber), 30000);

            if (Execution.delayUntil(() -> Worlds.getCurrent() == worldNumber, 30000)) {
                Execution.delayUntil(() -> Camera.turnTo(Random.nextInt(88, 97), Random.nextDouble(0.566, 0.568)), 10000);
                
                /*for (int i = 0; i < 10; i++) {
                    Camera.turnTo(Random.nextInt(88, 97), Random.nextDouble(0.566, 0.568));
                    double pitch = Camera.getPitch();
                    int yaw = Camera.getYaw();

                    if (!(pitch <= 0.566 || yaw < 88 || yaw > 96)) {
                        break;
                    }

                    Execution.delay(2000);
                }*/

                failedClickCounter = 0;
            }
        }
    }
}
