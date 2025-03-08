package fr.rammex.headreward.gui;

import fr.rammex.headreward.database.DataManager;
import fr.rammex.headreward.utils.HeadManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HeadGui {


    public static Inventory getHeadGui() {

        Map<String, List<String>> heads = DataManager.getHeads();

        Inventory inv = Bukkit.createInventory(null, 54, "§6Heads");

        for (Map.Entry<String, List<String>> head : heads.entrySet()) {
            String headName = head.getKey();
            String headID = head.getValue().get(0);
            String headLocation = head.getValue().get(1);
            String headReward = head.getValue().get(2);

            List<String> lore = new ArrayList<>();

            lore.add("§7Location: §e" + headLocation);
            lore.add("§7Reward ID: §e" + headReward);

            ItemStack headItem = HeadManager.getHead(headID);
            ItemMeta headMeta = headItem.getItemMeta();

            headMeta.setDisplayName("§6" + headName + " §7(#" + headID + ")");
            headMeta.setLore(lore);

            headItem.setItemMeta(headMeta);

            inv.addItem(headItem);


        }


        return inv;
    }
}
