package pw.saber.boosters.boosters.struct;

import pw.saber.boosters.boosters.BoosterTypes;

import java.util.concurrent.ConcurrentHashMap;

public class FactionBoosters extends ConcurrentHashMap<BoosterTypes, CurrentBoosters> {
    public boolean isBoosterActive(BoosterTypes type) {
        return this.containsKey(type);
    }
}

