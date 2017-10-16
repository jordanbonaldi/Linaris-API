package net.neferett.linaris.disguises.disguisetypes.watchers;

import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.disguises.disguisetypes.FlagWatcher;

public class ArrowWatcher extends FlagWatcher {

    public ArrowWatcher(Disguise disguise) {
        super(disguise);
    }

    public boolean isCritical() {
        return (Byte) getValue(16, (byte) 0) == 1;
    }

    public void setCritical(boolean critical) {
        setValue(16, (byte) (critical ? 1 : 0));
        sendData(16);
    }

}
