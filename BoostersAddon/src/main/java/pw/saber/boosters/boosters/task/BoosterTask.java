package pw.saber.boosters.boosters.task;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.util.CC;
import com.massivecraft.factions.zcore.util.TL;
import pw.saber.boosters.BoosterAddon;
import pw.saber.boosters.boosters.BoosterTypes;
import pw.saber.boosters.boosters.struct.CurrentBoosters;

import java.util.Map;

public class BoosterTask implements Runnable {

    public void run() {
        BoosterAddon.getInstance().getBoosterManager().getFactionBoosters().forEach((fId, boosters) -> {

            for (Map.Entry<BoosterTypes, CurrentBoosters> boosterTypesCurrentBoostersEntry : boosters.entrySet()) {

                CurrentBoosters boost = boosterTypesCurrentBoostersEntry.getValue();

                if (boost.getSecondsElapsed() >= boost.getMaxSeconds()) {

                    boosters.remove(boosterTypesCurrentBoostersEntry.getKey());
                    Faction faction = Factions.getInstance().getFactionById(fId);

                    if (faction != null) {
                        faction.sendMessage(CC.translate(TL.BOOSTER_EXPIRED.toString()
                                .replace("{multiplier}", String.valueOf(boost.getMultiplier()))
                                .replace("{boosterType}", boosterTypesCurrentBoostersEntry.getKey().getItemName())
                                .replace("{player}", boost.getWhoApplied())));

                    }
                    if (boosters.isEmpty()) {
                        BoosterAddon.getInstance().getBoosterManager().getFactionBoosters().remove(fId);
                        BoosterAddon.getInstance().getBoosterManager().saveActiveBoosters();
                    }
                } else {
                    boost.setSecondsElapsed(boost.getSecondsElapsed() + 1);
                }
            }
        });
    }
}
