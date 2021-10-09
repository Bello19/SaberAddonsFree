package pw.saber.shop;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.addon.FactionsAddon;
import com.massivecraft.factions.cmd.FCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import pw.saber.shop.cmds.CmdShop;
import pw.saber.shop.utils.FileUtil;

import java.io.File;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ShopAddon extends FactionsAddon {

    public ShopAddon(FactionsPlugin plugin) {
        super(plugin);
    }

    public File file;

    public FileConfiguration config;

    public static ShopAddon instance;

    @Override
    public void onEnable() {
        instance = this;

        try {
            FileUtil.ExportResource("/" + getAddonName().toLowerCase(Locale.ROOT) + ".yml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        file = new File(FactionsPlugin.getInstance().getServer().getWorldContainer().getAbsolutePath() + "/plugins/Factions/configuration/addons/shop.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void onDisable() {

    }

    public static ShopAddon getInstance() {
        return instance;
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getShopFile() {
        return config;
    }


    @Override
    public Set<Listener> listenersToRegister() {
        return new HashSet<>();
    }

    @Override
    public Set<FCommand> fCommandsToRegister() {
        Set<FCommand> commands = new HashSet<>();
        commands.add(new CmdShop());
        return commands;
    }

    @Override
    public String getAddonName() {
        return "Shop";
    }
}
