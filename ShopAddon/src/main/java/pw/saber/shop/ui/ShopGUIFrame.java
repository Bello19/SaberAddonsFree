package pw.saber.shop.ui;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.shade.stefvanschie.inventoryframework.Gui;
import com.massivecraft.factions.shade.stefvanschie.inventoryframework.GuiItem;
import com.massivecraft.factions.shade.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.massivecraft.factions.util.CC;
import com.massivecraft.factions.util.Cooldown;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pw.saber.shop.ShopAddon;
import pw.saber.shop.utils.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public class ShopGUIFrame {


    private final Gui gui;

    public ShopGUIFrame() {
        this.gui = new Gui(FactionsPlugin.getInstance(), ShopAddon.getInstance().getShopFile().getInt("GUI.Rows"), CC.translate(ShopAddon.getInstance().getShopFile().getString("GUI.Name")));
    }

    public void buildGUI(FPlayer fPlayer) {
        PaginatedPane pane = new PaginatedPane(0, 0, 9, this.gui.getRows());
        List<GuiItem> GUIItems = new ArrayList<>();

        List<String> list = ShopAddon.getInstance().getShopFile().getStringList("items");
        int i = 0;

        Faction faction = fPlayer.getFaction();
        int facPoints = faction.getPoints();
        for (String l : list) {
            int cost = ItemUtils.getCost(l);
            int amount = ItemUtils.getAmount(l);

            ItemStack x = ItemUtils.getItem(l);
            x.setAmount(amount);
            ItemMeta meta = x.getItemMeta();
            List<String> lore = null;
            if (meta != null) {
                lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
            }
            lore.add("");
            if (facPoints < cost) {
                lore.add(CC.translate(ShopAddon.getInstance().getShopFile().getString("item-affordable-lore").replace("{cost}", cost + "")));
                lore.add(CC.translate(ShopAddon.getInstance().getShopFile().getString("item-not-affordable-lore")));
            } else {
                lore.add(CC.translate(ShopAddon.getInstance().getShopFile().getString("item-affordable-lore").replace("{cost}", cost + "")));
            }
            meta.setLore(lore);
            x.setItemMeta(meta);
            GUIItems.add(i++, new GuiItem(x, (e) -> {
                e.setCancelled(true);
                if (facPoints >= cost) {
                    if (ShopAddon.getInstance().getShopFile().getInt("purchase-cooldown") != -1) {
                        Cooldown.setCooldown(faction, "factionShop", ShopAddon.getInstance().getShopFile().getInt("purchase-cooldown"));
                    }
                    faction.setPoints(facPoints - cost);
                    fPlayer.getPlayer().sendMessage(CC.translate(ShopAddon.getInstance().getShopFile().getString("prefix")
                            .replace("%item%", meta.getDisplayName())
                            .replace("%points%", cost + "")
                            .replace("%amount%", amount + "")));
                    fPlayer.getPlayer().getInventory().addItem(ItemUtils.getItem(l));
                    buildGUI(fPlayer);
                } else {
                    e.setCancelled(true);
                    fPlayer.getPlayer().closeInventory();
                    fPlayer.msg(TL.SHOP_NOT_ENOUGH_POINTS);
                }
            }));
        }
        pane.populateWithGuiItems(GUIItems);
        gui.addPane(pane);
        gui.update();
        gui.show(fPlayer.getPlayer());
    }
}
