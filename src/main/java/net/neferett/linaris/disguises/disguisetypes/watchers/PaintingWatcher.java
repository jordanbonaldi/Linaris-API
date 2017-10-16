package net.neferett.linaris.disguises.disguisetypes.watchers;

import org.bukkit.Art;

import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.disguises.disguisetypes.FlagWatcher;
import net.neferett.linaris.disguises.utilities.DisguiseUtilities;

public class PaintingWatcher extends FlagWatcher {

    private Art painting;

    public PaintingWatcher(Disguise disguise) {
        super(disguise);
    }

    @Override
    public PaintingWatcher clone(Disguise disguise) {
        PaintingWatcher watcher = (PaintingWatcher) super.clone(disguise);
        watcher.setArt(getArt());
        return watcher;
    }

    public Art getArt() {
        return painting;
    }

    public void setArt(Art newPainting) {
        this.painting = newPainting;
        if (getDisguise().getEntity() != null && getDisguise().getWatcher() == this) {
            DisguiseUtilities.refreshTrackers(getDisguise());
        }
    }

}
