package pw.saber.boosters.boosters.listener.types;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import pw.saber.boosters.BoosterAddon;
import pw.saber.boosters.boosters.BoosterTypes;
import pw.saber.boosters.boosters.struct.BoosterManager;
import pw.saber.boosters.boosters.struct.CurrentBoosters;
import pw.saber.boosters.boosters.struct.FactionBoosters;

import java.util.List;

public class MobDropListener implements Listener {

    private List<String> mobList = BoosterAddon.getInstance().getBoosterFile().getStringList("Boosters.Booster-Types.Remind.Mob-Drops.Types");

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) return;
        if (mobList.contains(e.getEntity().getType().toString())) {
            FLocation fLocation = new FLocation(e.getEntity().getLocation());
            Faction faction = Board.getInstance().getFactionAt(fLocation);
            BoosterManager manager = BoosterAddon.getInstance().getBoosterManager();
            FactionBoosters boosters = manager.getFactionBooster(faction);
            if (boosters != null && boosters.isBoosterActive(BoosterTypes.MOB)) {
                CurrentBoosters booster = boosters.get(BoosterTypes.MOB);
                e.getDrops().forEach(drop -> drop.setAmount(drop.getAmount() * (int) booster.getMultiplier()));
            }
        }
    }
}
