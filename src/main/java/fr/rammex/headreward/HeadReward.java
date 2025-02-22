package fr.rammex.headreward;

import fr.rammex.headreward.commands.CommandHeadReward;
import fr.rammex.headreward.database.DataManager;
import fr.rammex.headreward.events.GuiListener;
import fr.rammex.headreward.events.HeadListener;
import fr.rammex.headreward.task.HeadSpawnManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class HeadReward extends JavaPlugin {

    @Getter
    public static HeadReward instance;


    @Override
    public void onEnable() {
        instance = this;

        //CONFIG
        saveDefaultConfig();

        //DATABASE
        DataManager dataManager = new DataManager("headreward", new File(getDataFolder(), "data.db"));
        dataManager.load();

        //COMMANDS
        this.getCommand("headreward").setExecutor(new CommandHeadReward());

        //EVENTS
        getServer().getPluginManager().registerEvents(new HeadListener(), this);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);

        // TASK
        HeadSpawnManager.spawnHead();
    }

    @Override
    public void onDisable() {


    }
}
