package fr.rammex.headreward.utils;

import fr.rammex.headreward.database.DataManager;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HeadManager {


    public static ItemStack getHead(String headID) {
        HeadDatabaseAPI api = new HeadDatabaseAPI();
        try{
            ItemStack head = api.getItemHead(headID);
            return head;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isHeadExist(String headID) {
        HeadDatabaseAPI api = new HeadDatabaseAPI();
        try{
            ItemStack head = api.getItemHead(headID);
            return head != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static void placeHead(Location location, String headID) {
        if(isHeadExist(headID)) {
            Block block = location.getBlock();
            block.setType(Material.SKULL);

            HeadDatabaseAPI api = new HeadDatabaseAPI();
            try{
                api.setBlockSkin(block, headID);
            } catch (Exception e) {
                System.out.println("Error while setting head skin");
            }
        }
    }

    public static void removeHead(Block block) {
        block.setType(Material.AIR);
    }

    public static String getRandomHead() {
        Map<String, List<String>> heads = DataManager.getHeads();

        List<List<String>> values = new ArrayList<>(heads.values());
        Random random = new Random();
        if(values.isEmpty()) return null;
        List<String> randomHead = values.get(random.nextInt(values.size()));

        return randomHead.get(0);
    }
}
