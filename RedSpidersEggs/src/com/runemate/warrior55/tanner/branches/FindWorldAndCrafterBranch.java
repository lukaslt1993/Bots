package com.runemate.warrior55.tanner.branches;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.local.Worlds;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.script.framework.tree.BranchTask;
import com.runemate.game.api.script.framework.tree.TreeTask;
import com.runemate.warrior55.tanner.main.PortableCrafter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindWorldAndCrafterBranch extends BranchTask {

    private final UsingOwnCrafterBranch USING_OWN_CRAFTER_BRANCH = new UsingOwnCrafterBranch();
    private final Map<String, Set<Integer>> worlds = new LinkedHashMap<>();
    private int worldToHop;
    private Coordinate crafterSpot;

    private enum Location {
        CA("CA", new Coordinate(3214, 3256)),
        LC("LC", new Coordinate(3148, 3234));

        private final String name;
        private final Coordinate coord;

        Location(String name, Coordinate coord) {
            this.name = name;
            this.coord = coord;
        }

        public Coordinate getCoord() {
            return coord;
        }

        public String getName() {
            return name;
        }
    }

    @Override
    public TreeTask successTask() {
        return new PlayerInCrafterSpotBranch(crafterSpot, worldToHop);
    }

    @Override
    public TreeTask failureTask() {
        return USING_OWN_CRAFTER_BRANCH;
    }

    @Override
    public boolean validate() {
        if (worldToHop == Worlds.getCurrent()) {
            worldToHop = 0;
        }
        if (worldToHop == 0) {
            try {
                findWorldAndCrafter();
            } catch (Throwable t) {
                throw (new RuntimeException(t));
            }
        }
        return worldToHop != 0;
    }

    private void findWorldAndCrafter() throws IOException {
        URL url = new URL("https://docs.google.com/spreadsheets/d/16Yp-eLHQtgY05q6WBYA2MDyvQPmZ4Yr3RHYiBCBj2Hc/edit#gid=915793480");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;

        while ((line = reader.readLine()) != null) {

            int index = line.indexOf("Notes:");
            if (index >= 0) {

                line = reader.readLine();

                Pattern p = Pattern.compile(",\\S.*?,\\S");
                Matcher m = p.matcher(line);
                m.find();
                line = m.group();
                line = line.substring(0, line.length() - 1);
                for (Location loc : Location.values()) {
                    String s = loc.getName();
                    p = Pattern.compile("[^a-zA-Z]\\d+[^a-zA-Z].*?" + s);
                    m = p.matcher(line);
                    List l = getWorlds(m);
                    updateWorlds(s, l);
                }

                Iterator<String> it = worlds.keySet().iterator();
                while (it.hasNext()) {
                    String crafterSpotName = it.next();
                    crafterSpot = Location.valueOf(crafterSpotName).getCoord();
                    Set<Integer> s = worlds.get(crafterSpotName);
                    if (!s.isEmpty()) {
                        worldToHop = s.iterator().next();
                        s.remove(worldToHop);
                        if (Worlds.getOverview(worldToHop).isMembersOnly()
                                && !((PortableCrafter) Environment.getBot()).isMember()) {
                            worldToHop = 0;
                        }
                        break;
                    }
                }

                break;
            }
        }

        reader.close();
    }

    private List<Integer> getWorlds(Matcher m) {
        List<Integer> result = new ArrayList<>();
        while (m.find()) {
            String s = m.group();
            int i = Integer.parseInt(s.replaceAll("\\D", ""));
            result.add(i);
        }
        return result;
    }

    private void updateWorlds(String location, List<Integer> worlds) {
        if (!worlds.isEmpty()) {
            if (this.worlds.get(location) == null) {
                this.worlds.put(location, new LinkedHashSet<>());
            }
            this.worlds.get(location).addAll(worlds);
        }
    }
}
