package fr.rammex.headreward.commands;

import fr.rammex.headreward.HeadReward;
import fr.rammex.headreward.database.DataManager;
import fr.rammex.headreward.gui.HeadGui;
import fr.rammex.headreward.utils.HeadManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandHeadReward implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cVous devez être un joueur pour exécuter cette commande.");
            return false;
        }

        Player player = (Player) sender;

        if(args.length == 0) {
            player.openInventory(HeadGui.getHeadGui());
            return false;
        }

        switch (args[0]) {
            case "reload":
                HeadReward.getInstance().reloadConfig();
                player.sendMessage("§aLe plugin a été rechargé.");
                break;
            case "help":
                player.sendMessage("§a/headreward : Ouvre le menu des têtes.");
                player.sendMessage("§a/headreward reload : Recharge la configuration du plugin.");
                player.sendMessage("§a/headreward create : <headName> <HDB_ID> <rewardId> : crée une tête.");
                player.sendMessage("§a/headreward delete <headName> : supprime une tête.");
                player.sendMessage("§a/headreward edit <headName> <HDB_ID> <rewardID> : edit une tête.");
            case "create":
                if(args.length != 4) {
                    player.sendMessage("§cUtilisation: /headreward create <headName> <HDB_ID> <rewardId>");
                    return false;
                }

                String headName = args[1];
                String HDB_ID = args[2];
                String rewardId = args[3];
                String headLocation = player.getLocation().getWorld().getName()+","+player.getLocation().getBlockX()+","+player.getLocation().getBlockY()+","+player.getLocation().getBlockZ();
                if(!HeadManager.isHeadExist(HDB_ID)) {
                    player.sendMessage("§cL'ID de cette tête n'existe pas.");
                    return false;
                }

                if(DataManager.headExists(headName)) {
                    player.sendMessage("§cCette tête existe déjà.");
                    return false;
                }

                DataManager.addHead(headName, HDB_ID, headLocation,rewardId);
                player.sendMessage("§aLa tête a été ajoutée.");
                break;

            case "delete":
                if(args.length != 2) {
                    player.sendMessage("§cUtilisation: /headreward delete <headName>");
                    return false;
                }

                headName = args[1];
                if(!DataManager.headExists(headName)) {
                    player.sendMessage("§cCette tête n'existe pas.");
                    return false;
                }

                DataManager.removeHead(headName);
                player.sendMessage("§aLa tête a été supprimée.");

                break;
            case "edit":
                if(args.length != 4) {
                    player.sendMessage("§cUtilisation: /headreward edit <headName> <HDB_ID> <rewardId>");
                    return false;
                }

                headName = args[1];
                HDB_ID = args[2];
                rewardId = args[3];

                if(!DataManager.headExists(headName)) {
                    player.sendMessage("§cCette tête n'existe pas.");
                    return false;
                }

                if(!HeadManager.isHeadExist(HDB_ID)) {
                    player.sendMessage("§cCette tête n'existe pas.");
                    return false;
                }

                DataManager.updateHead(HDB_ID, headName, rewardId);
                player.sendMessage("§aLa tête a été modifiée.");

                break;
            default:
                player.sendMessage("§cCommande inconnue.");
                break;
        }
        return false;
    }
}
