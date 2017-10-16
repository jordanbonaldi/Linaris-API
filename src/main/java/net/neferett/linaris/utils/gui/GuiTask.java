package net.neferett.linaris.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.neferett.linaris.BukkitAPI;



public class GuiTask extends BukkitRunnable {
 
    @SuppressWarnings("unused")
	private final BukkitAPI plugin;
    private final Player p;
    private final GuiScreen gui;
 
    public GuiTask(BukkitAPI plugin,Player p,GuiScreen gui) {
        this.plugin = plugin;
        this.p = p;
        this.gui = gui;
        gui.open();
    }
 
    @Override
    public void run() {
    	
    	if (!gui.getInventory().getViewers().contains(p)) {
    		this.cancel();
    		return;
    	}
    	
    	BukkitAPI.get().getServer().getPluginManager().callEvent(new GuiUpdateEvent(p,gui,false));
    	gui.drawScreen();

    }
 
}
