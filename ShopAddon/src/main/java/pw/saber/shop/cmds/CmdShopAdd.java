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
import pw.saber.shop.utils.BaseUtils;
import pw.saber.shop.utils.ItemUtils;

import java.io.IOException;
import java.util.List;

public class CmdShopAdd extends FCommand {


    public CmdShopAdd() {
        super();
        this.aliases.add("add");
        this.requiredArgs.add("cost");

        this.requirements = new CommandRequirements.Builder(Permission.BYPASS)
                .playerOnly()
                .build();
    }


    @Override
    public void perform(CommandContext context) {
        Player player = context.player;
        ItemStack item = player.getItemInHand();

        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(CC.translate("&c&l[!] &7The Item In Your Hand Cannot Be Null"));
            return;
        }
        List<String> list = ShopAddon.getInstance().getShopFile().getStringList("items");
        for (String l : list) {
            ItemStack x = ItemUtils.getItem(l);
            if (item.equals(x)) {
                player.sendMessage(CC.translate("&c&l[!] That item is already in the Faction Shop!"));
                return;
            }
        }
        list.add(BaseUtils.toBase64(new ItemStack[]{item}, 9));
        int cost = 0;
        if (context.args.get(0) != null) {
            try {
                cost = Integer.parseInt(context.args.get(0));
            } catch (NumberFormatException e) {
                cost = 1;
            }
            String l = list.get(list.size() - 1);
            l = l + ":" + cost;
            list.set(list.size() - 1, l);
        }
        int amount = item.getAmount();
        String l = list.get(list.size() - 1);
        l = l + ":" + amount;
        list.set(list.size() - 1, l);

        ShopAddon.getInstance().getShopFile().set("items", list);
        try {
            ShopAddon.getInstance().getShopFile().save(ShopAddon.getInstance().getFile());
            ShopAddon.getInstance().getShopFile().load(ShopAddon.getInstance().getFile());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        player.sendMessage(CC.translate("&f[&4&lFaction Shop&f] &dYou have successfully added " + ItemUtils.getDisplayName(item) + " &dfor &b" + cost + " points!"));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SHOP_DESCRIPTION;
    }
}
