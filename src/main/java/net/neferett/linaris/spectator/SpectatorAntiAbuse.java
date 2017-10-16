package net.neferett.linaris.spectator;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SpectatorAntiAbuse implements Runnable
{
    public static boolean enabled;
    public static int maxAllowedDistance;
    
    @Override
    public void run() {
        if (!SpectatorAntiAbuse.enabled) {
            return;
        }
        if (Bukkit.getOnlinePlayers().size() <= 1) {
            return;
        }
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getGameMode().equals((Object)GameMode.SPECTATOR)) {
                continue;
            }
            Player closestPlayer = null;
            double minDistance = 2.147483647E9;
            for (final Player online : Bukkit.getOnlinePlayers()) {
                if (!online.equals(player)) {
                    if (online.getGameMode().equals((Object)GameMode.SPECTATOR)) {
                        continue;
                    }
                    if (!online.getLocation().getWorld().equals(player.getLocation().getWorld())) {
                        continue;
                    }
                    final double distance = player.getLocation().distance(online.getLocation());
                    if (distance >= minDistance) {
                        continue;
                    }
                    minDistance = distance;
                    closestPlayer = online;
                }
            }
            if (minDistance <= SpectatorAntiAbuse.maxAllowedDistance) {
                continue;
            }
            Player tp = closestPlayer;
            if (tp == null) {
                tp = this.getRandomNonSpecPlayer();
            }
            if (tp != null) {
	            player.teleport((Entity)tp);
	            player.sendMessage("[Mode Spectateur] §cNe vous \u00e9loignez pas autant !");
            }
        }
    }
    
    public Player getRandomNonSpecPlayer() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getGameMode().equals((Object)GameMode.SPECTATOR)) {
                return player;
            }
        }
        return null;
    }
    
    static {
        SpectatorAntiAbuse.enabled = true;
        SpectatorAntiAbuse.maxAllowedDistance = 80;
    }
}
