package fr.rammex.headreward.events;

import fr.rammex.headreward.HeadReward;
import fr.rammex.headreward.database.DataManager;
import fr.rammex.headreward.utils.HeadManager;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class HeadListener implements Listener {

    @EventHandler
    public void onHeadClick(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getClickedBlock() != null) {
            Block block = event.getClickedBlock();
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            if (api.getBlockID(block) != null) {
                if(DataManager.isHeadExist(api.getBlockID(block))){
                    String headRewardID = DataManager.getHeadReward(api.getBlockID(block));
                    if(headRewardID != null) {
                        List<String> rewards = HeadReward.getInstance().getConfig().getStringList("rewards." + headRewardID+".commands");
                        String message = HeadReward.getInstance().getConfig().getString("rewards." + headRewardID +".message");

                        for (String reward : rewards) {
                            HeadReward.getInstance().getServer().dispatchCommand(HeadReward.getInstance().getServer().getConsoleSender(), reward.replace("%player%", player.getName()));
                        }
                        if(message != null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
                        }


                        HeadManager.removeHead(block);
                    }
                }
            }
        }

    }
}
