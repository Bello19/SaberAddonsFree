package pw.saber.shop.cmds;

import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.CC;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pw.saber.shop.ShopAddon;
import pw.saber.shop.utils.ItemUtils;

import java.io.IOException;
import java.util.List;

public class CmdShopRemove extends FCommand {

    public CmdShopRemove() {
        super();
        this.aliases.add("remove");
        this.requirements = new CommandRequirements.Builder(Permission.BYPASS)
                .playerOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        Player player = context.player;
        ItemStack item = player.getItemInHand();

        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(CC.translate("&c&l[!] &7The Item Cannot be Null!"));
            return;
        }
        String displayName = ItemUtils.getDisplayName(item);
        ItemStack[] items = ItemUtils.getLootTableItems();
        List<String> list = ShopAddon.getInstance().getShopFile().getStringList("items");
        for (int i = list.size() - 1; i >= 0; --i) {
            ItemStack x = items[i];
            if (x == null || x.getType() == Material.AIR) {
                list.remove(i);
                ShopAddon.getInstance().getShopFile().set("items", list);
                try {
                    ShopAddon.getInstance().getShopFile().save(ShopAddon.getInstance().getFile());
                    ShopAddon.getInstance().getShopFile().load(ShopAddon.getInstance().getFile());
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
                    player.sendMessage(CC.Green + "Found null item... DELETING");
                } else if (x.equals(item)) {
                    list.remove(i);
                    ShopAddon.getInstance().getShopFile().set("items", list);
                    try {
                        ShopAddon.getInstance().getShopFile().save(ShopAddon.getInstance().getFile());
                        ShopAddon.getInstance().getShopFile().load(ShopAddon.getInstance().getFile());
                    } catch (IOException | InvalidConfigurationException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(CC.Green + "Found item... Deleting " + item.getAmount() + " " + displayName);
                    return;
                }
            }
        }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SHOP_DESCRIPTION;
    }
}
