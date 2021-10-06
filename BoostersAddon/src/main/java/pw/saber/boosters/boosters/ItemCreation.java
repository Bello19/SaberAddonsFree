package pw.saber.boosters.boosters;

import com.cryptomorin.xseries.XMaterial;
import com.massivecraft.factions.shade.nbtapi.nbtapi.NBTItem;
import com.massivecraft.factions.util.CC;
import com.massivecraft.factions.util.TimeUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pw.saber.boosters.BoosterAddon;

import java.util.ArrayList;
import java.util.List;

public class ItemCreation {

    public static ItemStack createBoosterItem(BoosterTypes type, int duration, double multiplier) {
        ItemStack itemStack = new ItemStack(XMaterial.matchXMaterial(BoosterAddon.getInstance().getBoosterFile().getString("Boosters.BoosterItem.Type")).get().parseMaterial());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(CC.translate(BoosterAddon.getInstance().getBoosterFile().getString("Boosters.BoosterItem.Name").replace("{boosterType}", type.getItemName())));
        List<String> configLore = BoosterAddon.getInstance().getBoosterFile().getStringList("Boosters.BoosterItem.Lore");
        List<String> lore = new ArrayList<>();
        for (String s : configLore) {
            lore.add(CC.translate(s).replace("{duration}", TimeUtil.formatDifference(duration)).replace("{multiplier}", String.valueOf(multiplier)));
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("BoosterType", type.getName());
        nbtItem.setDouble("Multiplier", multiplier);
        nbtItem.setInteger("Duration", duration);
        return nbtItem.getItem();
    }

}

