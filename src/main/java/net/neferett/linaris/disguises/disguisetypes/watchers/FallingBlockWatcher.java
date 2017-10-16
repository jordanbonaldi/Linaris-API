package net.neferett.linaris.disguises.disguisetypes.watchers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.disguises.DisguiseAPI;
import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.disguises.disguisetypes.FlagWatcher;
import net.neferett.linaris.disguises.utilities.DisguiseUtilities;

public class FallingBlockWatcher extends FlagWatcher {

    private ItemStack block;

    public FallingBlockWatcher(Disguise disguise) {
        super(disguise);
    }

    @Override
    public FallingBlockWatcher clone(Disguise disguise) {
        FallingBlockWatcher watcher = (FallingBlockWatcher) super.clone(disguise);
        watcher.setBlock(getBlock());
        return watcher;
    }

    public ItemStack getBlock() {
        return block;
    }

    public void setBlock(ItemStack block) {
        this.block = block;
        if (block.getType() == null || block.getType() == Material.AIR) {
            block.setType(Material.STONE);
        }
        if (DisguiseAPI.isDisguiseInUse(getDisguise()) && getDisguise().getWatcher() == this) {
            DisguiseUtilities.refreshTrackers(getDisguise());
        }
    }
}
