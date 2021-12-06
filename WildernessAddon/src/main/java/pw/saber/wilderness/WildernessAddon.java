package pw.saber.wilderness;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.addon.FactionsAddon;
import com.massivecraft.factions.cmd.FCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import pw.saber.wilderness.cmd.CmdWild;
import pw.saber.wilderness.utils.FileUtil;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class WildernessAddon extends FactionsAddon {
    public File file;

    public FileConfiguration config;

    public static WildernessAddon instance;

    public WildernessAddon(FactionsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        instance = this;
        file = new File(FactionsPlugin.getInstance().getServer().getWorldContainer().getAbsolutePath() + "/plugins/Factions/configuration/addons/wilderness.yml");
        if(!file.exists()) {
            try {
                FileUtil.ExportResource("/" + getAddonName().toLowerCase(Locale.ROOT) + ".yml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

    }

    @Override
    public void onDisable() {

    }

    @Override
    public Set<Listener> listenersToRegister() {
        return Collections.emptySet();
    }

    public static WildernessAddon getInstance() {
        return instance;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public Set<FCommand> fCommandsToRegister() {
        Set<FCommand> commands = new HashSet<>();
        commands.add(new CmdWild());
        return commands;
    }

    @Override
    public String getAddonName() {
        return "WILDERNESS";
    }
}
