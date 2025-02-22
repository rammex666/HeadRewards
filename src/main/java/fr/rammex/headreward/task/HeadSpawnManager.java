package fr.rammex.headreward.task;

import fr.rammex.headreward.HeadReward;
import fr.rammex.headreward.database.DataManager;
import fr.rammex.headreward.utils.HeadManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class HeadSpawnManager {

    static int delayBetweenSpawn = HeadReward.getInstance().getConfig().getInt("heads_param.delay_between_spawn");

    public static void spawnHead() {
        Bukkit.getScheduler().runTaskTimer(HeadReward.getInstance(), () -> {
            String headName = HeadManager.getRandomHead();
            String headID = DataManager.getHeadID(headName);
            if(headName == null){
                System.out.println("No head found");
                return;
            }
            int headCount = HeadReward.getInstance().getConfig().getInt("heads_param.number_of_heads_on_spawn");

            String message = HeadReward.getInstance().getConfig().getString("heads_param.message_on_spawn");

            if(message != null) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',message).replace("%head%", headName).replace("%count%", String.valueOf(headCount)));
            }

            for (int i = 0; i < headCount; i++) {
                if (headName != null) {
                    HeadManager.placeHead(DataManager.getHeadLocation(headName), headID);
                }
            }
        }, 0, delayBetweenSpawn * 20);
    }

}
