package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerAchivement extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerAchievementAwardedEvent e = (PlayerAchievementAwardedEvent)event;
        return !GhostManager.isGhost(e.getPlayer());
    }
}
