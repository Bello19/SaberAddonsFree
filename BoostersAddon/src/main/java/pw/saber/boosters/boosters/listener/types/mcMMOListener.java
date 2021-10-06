package pw.saber.boosters.boosters.listener.types;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.CC;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import pw.saber.boosters.BoosterAddon;
import pw.saber.boosters.boosters.BoosterTypes;
import pw.saber.boosters.boosters.struct.BoosterManager;
import pw.saber.boosters.boosters.struct.CurrentBoosters;
import pw.saber.boosters.boosters.struct.FactionBoosters;


public class mcMMOListener implements Listener {

    @EventHandler
    public void onmcMMOChange(McMMOPlayerXpGainEvent event) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(event.getPlayer());
        Faction faction = fPlayer.getFaction();
        if (faction != null && faction.isNormal()) {
            Player player = event.getPlayer();
            BoosterManager manager = BoosterAddon.getInstance().getBoosterManager();
            FactionBoosters boosters = manager.getFactionBooster(faction);
            if (boosters != null && boosters.isBoosterActive(BoosterTypes.MCMMO)) {
                CurrentBoosters booster = boosters.get(BoosterTypes.MCMMO);
                event.setRawXpGained((float) ((double) event.getRawXpGained() * booster.getMultiplier()));
                int notify = 0;
                if (player.hasMetadata("mcmmoBoosterNotify") && player.getMetadata("mcmmoBoosterNotify").size() > 0) {
                    notify = player.getMetadata("mcmmoBoosterNotify").get(0).asInt();
                    if (notify >= BoosterAddon.getInstance().getBoosterFile().getInt("Booster-Types.Remind.mcMMO")) {
                        player.sendMessage(CC.translate(TL.BOOSTER_REMINDER_MCMMO.toString()
                                .replace("{multiplier}", String.valueOf(booster.getMultiplier()))
                                .replace("{player}", booster.getWhoApplied())
                                .replace("{time-left}", booster.getFormattedTimeLeft())));

                        notify = 0;
                    }
                }

                ++notify;
                player.setMetadata("mcmmoBoosterNotify", new FixedMetadataValue(FactionsPlugin.getInstance(), notify));
            }

        }
    }

}