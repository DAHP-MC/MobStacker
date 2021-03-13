package me.jet315.stacker;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.jet315.stacker.commands.CommandHandler;
import me.jet315.stacker.events.OnEntityDamage;
import me.jet315.stacker.events.OnEntityDeath;
import me.jet315.stacker.manager.EntityStackerManager;
import me.jet315.stacker.manager.StackEntity;
import me.jet315.stacker.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by Jet on 24/01/2018.
 */
public class MobStacker extends JavaPlugin {

    private static EntityStackerManager entityStacker;
    private static StackEntity stackEntity;
    private static Config config;

    public static MobStacker getInstance() {
        return getPlugin(MobStacker.class);
    }

    public static Config getMobStackerConfig() {
        return config;
    }

    public static StackEntity getStackEntity() {
        return stackEntity;
    }

    public static EntityStackerManager getEntityStacker() {
        return entityStacker;
    }

    @Override
    public void onEnable() {
        createConfig();
        config = new Config(this.getConfig());
        entityStacker = new EntityStackerManager(config.stackRadius, config.mobsToStack);
        stackEntity = new StackEntity(config);

        // Register listeners
        Bukkit.getPluginManager().registerEvents(new OnEntityDeath(), this);
        Bukkit.getPluginManager().registerEvents(new OnEntityDamage(), this);

        //World Guard
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (!(plugin instanceof WorldGuardPlugin)) {
            config.worldguardEnabled = false;
        }

        //Register commands
        getCommand("mobstacker").setExecutor(new CommandHandler());
    }

    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                //noinspection ResultOfMethodCallIgnored
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
