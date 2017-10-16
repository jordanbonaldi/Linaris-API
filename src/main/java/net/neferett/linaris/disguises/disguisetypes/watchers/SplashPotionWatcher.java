package net.neferett.linaris.disguises.disguisetypes.watchers;

import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.disguises.disguisetypes.FlagWatcher;
import net.neferett.linaris.disguises.utilities.DisguiseUtilities;

public class SplashPotionWatcher extends FlagWatcher {

    private int potionId;

    public SplashPotionWatcher(Disguise disguise) {
        super(disguise);
    }

    @Override
    public SplashPotionWatcher clone(Disguise disguise) {
        SplashPotionWatcher watcher = (SplashPotionWatcher) super.clone(disguise);
        watcher.setPotionId(getPotionId());
        return watcher;
    }

    public int getPotionId() {
        return potionId;
    }

    public void setPotionId(int newPotionId) {
        this.potionId = newPotionId;
        if (getDisguise().getEntity() != null && getDisguise().getWatcher() == this) {
            DisguiseUtilities.refreshTrackers(getDisguise());
        }
    }

}
