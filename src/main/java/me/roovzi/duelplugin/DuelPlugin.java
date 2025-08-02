package me.roovzi.duelplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class DuelPlugin extends JavaPlugin {

    private static DuelPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getCommand("duel").setExecutor(new DuelCommand());
        getCommand("accept").setExecutor(new AcceptCommand());
        getCommand("deny").setExecutor(new DenyCommand());

        getServer().getPluginManager().registerEvents(new DuelManager(), this);
    }

    public static DuelPlugin getInstance() {
        return instance;
    }
}
