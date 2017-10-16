package net.neferett.linaris.disguises.disguisetypes.watchers;

import java.util.Random;

import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.disguises.disguisetypes.RabbitType;

public class RabbitWatcher extends AgeableWatcher {

    public RabbitWatcher(Disguise disguise) {
        super(disguise);
        setType(RabbitType.values()[new Random().nextInt(RabbitType.values().length)]);
    }

    public RabbitType getType() {
        return RabbitType.getType((Integer) getValue(18, 0));
    }

    public void setType(RabbitType type) {
        setValue(18, (byte) type.getTypeId());
        sendData(18);
    }

}
