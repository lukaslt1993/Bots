package com.runemate.warrior55.zammy.common;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;

public class Stuff {

    public static boolean check(boolean isBank) {
        if (isBank) {
            return (Inventory.contains("Law rune") || Bank.contains("Law rune"))
                    && (Equipment.containsAnyOf("Staff of air", "Air battlestaff", "Mystic air staff")
                    || Inventory.contains("Air rune") || Bank.contains("Air rune"));
        } else {
            return Inventory.contains("Law rune")
                    && (Equipment.containsAnyOf("Staff of air", "Air battlestaff", "Mystic air staff")
                    || Inventory.contains("Air rune"));
        }
    }

}
