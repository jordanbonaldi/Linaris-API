package net.neferett.linaris.utils;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


/**
 * @author created by ThisIsMac on 26/05/2015.
 */
public class SoundUtils {

    /**
     * @param sound - The sound that you want to be played to players
     */
    public static void broadcastSound(Sound sound) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), sound, 1, 1);
        }
    }

    /**
     * @param sound The sound that you want to be played to players
     * @param volume The volume that player will hear sound
     */
    public static void broadcastSound(Sound sound, int volume) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getPlayer().getPlayer().getLocation(), sound, volume, 1);
        }
    }

    /**
     * @param sound The sound that you want to be played to players
     * @param loc Where the sound will be played
     */
    public static void broadcastSound(Sound sound, Location loc)  {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(loc, sound, 1, 1);
        }
    }

    /**
     * @param sound The sound that you want to be played to players
     * @param loc Where the sound will be played
     * @param volume The volume that player will hear sound
     */
    public static void broadcastSound(Sound sound, Location loc, float volume)  {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(loc, sound, volume, 1);
        }
    }



    /**
     * TODO: Test this function
     * @param sound The sound that you want to be played to players
     * @param source Where the sound will come from (for every player)
     * @param distance The distance at the player will hear the sound (ignored if SoundSource.ONPLAYER)
     * @param volume The volume that player will hear sound
     */
    @SuppressWarnings("static-access")
	public static void broadcastSound(Sound sound, SoundSource source, int distance, int volume) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(source.getLocation(source, player.getLocation(), distance), sound, volume, 1);
        }
    }

    public enum SoundSource {
        BEHIND(),
        FRONT(),
        ONPLAYER();

        /**
         * Get location at a certain distance in the SoundSource direction
         * @param type The sound that you want to be played to players
         * @param source Where the sound will come from (for every player)
         * @param distance The distance at the player will hear the sound (ignored if SoundSource.ONPLAYER)
         * @return Location where sound will be played
         */
        public static Location getLocation(SoundSource type, Location source, int distance) {
            return type == BEHIND ? source.setDirection(source.getDirection().normalize().multiply(-distance)) :
                        type == FRONT ? source.setDirection(source.getDirection().normalize().multiply(distance)) :
                             type == ONPLAYER ? source :
                                    null;
        }
    }
}
