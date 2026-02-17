package me.saki.solarguard.listeners;

import me.saki.solarguard.SolarGuard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Gerencia a imunidade temporária contra detecções pós-teletransporte.
 */
public class TeleportListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        
        // Aplica um metadado de expiração para pausar os checks de movimento
        player.setMetadata("sg-bypass", new FixedMetadataValue(SolarGuard.getInstance(), System.currentTimeMillis() + 2000));
    }
}
