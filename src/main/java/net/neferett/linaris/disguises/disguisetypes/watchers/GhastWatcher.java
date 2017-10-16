package net.neferett.linaris.disguises.disguisetypes.watchers;

import net.neferett.linaris.disguises.disguisetypes.Disguise;

public class GhastWatcher extends LivingWatcher {

    public GhastWatcher(Disguise disguise) {
        super(disguise);
    }

    public boolean isAggressive() {
        return (Byte) getValue(16, (byte) 0) == 1;
    }

    public void setAggressive(boolean isAggressive) {
        setValue(16, (byte) (isAggressive ? 1 : 0));
        sendData(16);
    }

}
