package pw.saber.boosters;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.addon.FactionsAddon;
import com.massivecraft.factions.cmd.FCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import pw.saber.boosters.boosters.commands.CmdBoosters;
import pw.saber.boosters.boosters.commands.CmdGiveBooster;
import pw.saber.boosters.boosters.listener.RedemptionListener;
import pw.saber.boosters.boosters.listener.types.ExperienceListener;
import pw.saber.boosters.boosters.listener.types.MobDropListener;
import pw.saber.boosters.boosters.listener.types.mcMMOListener;
import pw.saber.boosters.boosters.struct.BoosterManager;
import pw.saber.boosters.boosters.task.BoosterTask;
import pw.saber.boosters.util.FileUtil;

import java.io.File;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class BoosterAddon extends FactionsAddon {

    public static BoosterAddon instance;

    public BoosterManager boosterManager;

    public File file;

    public FileConfiguration config;

    public BoosterAddon(FactionsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        instance = this;
        file = new File(FactionsPlugin.getInstance().getServer().getWorldContainer().getAbsolutePath() + "/plugins/Factions/configuration/addons/boosters.yml");
        if(!file.exists()) {
            try {
                FileUtil.ExportResource("/" + getAddonName().toLowerCase(Locale.ROOT) + ".yml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        config = YamlConfiguration.loadConfiguration(file);

        (this.boosterManager = new BoosterManager()).loadActiveBoosters();
        getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(getPlugin(), new BoosterTask(), 20L, 20L);
    }


    @Override
    public void onDisable() {
        this.boosterManager.saveActiveBoosters();
    }

    @Override
    public Set<Listener> listenersToRegister() {
        Set<Listener> listeners = new HashSet<>();
        if (Bukkit.getPluginManager().isPluginEnabled("mcMMO")) {
            listeners.add(new mcMMOListener());
        }
        listeners.add(new MobDropListener());
        listeners.add(new ExperienceListener());
        listeners.add(new RedemptionListener(getBoosterManager()));
        return listeners;
    }


    @Override
    public Set<FCommand> fCommandsToRegister() {
        Set<FCommand> commands = new HashSet<>();
        commands.add(new CmdBoosters());
        commands.add(new CmdGiveBooster());
        return commands;
    }

    public FileConfiguration getBoosterFile() {
        return config;
    }

    public BoosterManager getBoosterManager() {
        return boosterManager;
    }

    public static BoosterAddon getInstance() {
        return instance;
    }



    @Override
    public String getAddonName() {
        return "Boosters";
    }
}
