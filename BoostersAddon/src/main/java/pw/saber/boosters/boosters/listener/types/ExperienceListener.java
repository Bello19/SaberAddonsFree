package pw.saber.boosters.boosters.listener.types;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.CC;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import pw.saber.boosters.BoosterAddon;
import pw.saber.boosters.boosters.BoosterTypes;
import pw.saber.boosters.boosters.struct.BoosterManager;
import pw.saber.boosters.boosters.struct.CurrentBoosters;
import pw.saber.boosters.boosters.struct.FactionBoosters;

public class ExperienceListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
            Faction fac = fPlayer.getFaction();
            if (fac == null || !fac.isNormal()) {
                return;
            }

            BoosterManager manager = BoosterAddon.getInstance().getBoosterManager();
            FactionBoosters boosters = manager.getFactionBooster(fac);
            if (boosters != null && boosters.isBoosterActive(BoosterTypes.EXP)) {
                CurrentBoosters booster = boosters.get(BoosterTypes.EXP);
                event.setDroppedExp((int) ((double) event.getDroppedExp() * booster.getMultiplier()));
                int notify = 0;
                if (player.hasMetadata("expBoosterNotify") && player.getMetadata("expBoosterNotify").size() > 0) {
                    notify = player.getMetadata("expBoosterNotify").get(0).asInt();
                    if (notify >= BoosterAddon.getInstance().getBoosterFile().getInt("Boosters.Booster-Types.Remind.Exp")) {
                        player.sendMessage(CC.translate(TL.BOOSTER_REMINDER_EXP.toString()
                                .replace("{multiplier}", String.valueOf(booster.getMultiplier()))
                                .replace("{player}", booster.getWhoApplied())
                                .replace("{time-left}", booster.getFormattedTimeLeft())));
                        notify = 0;
                    }
                }

                ++notify;
                player.setMetadata("expBoosterNotify", new FixedMetadataValue(FactionsPlugin.getInstance(), notify));
            }
        }
    }
}

