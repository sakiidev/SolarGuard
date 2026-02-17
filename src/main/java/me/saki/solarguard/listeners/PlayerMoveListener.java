package me.saki.solarguard.listeners;

import me.saki.solarguard.SolarGuard;
import me.saki.solarguard.discord.WebhookDispatcher;
import me.saki.solarguard.utils.LatencyUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Motor de análise cinemática para detecção de anomalias de movimentação.
 * Esta classe monitora a consistência de deslocamento horizontal e vertical,
 * aplicando filtros de latência e estados de imunidade temporária.
 */
public class PlayerMoveListener implements Listener {

    /**
     * Cache persistente de violações por Identificador Único Global (UUID).
     */
    private final HashMap<UUID, Integer> violationTracker = new HashMap<>();

    /**
     * Intercepta a movimentação do jogador para análise heurística.
     * * @param event O evento de movimentação disparado pelo servidor.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        // 1. Salvaguardas de Estado: Isenta jogadores em condições específicas
        if (player.hasPermission("solarguard.bypass") 
                || player.getAllowFlight() 
                || player.getGameMode() == GameMode.CREATIVE 
                || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        // 2. Proteção contra Teletransporte Recente via Metadados
        if (player.hasMetadata("sg-bypass")) {
            long expiry = player.getMetadata("sg-bypass").get(0).asLong();
            if (System.currentTimeMillis() < expiry) {
                return;
            } else {
                player.removeMetadata("sg-bypass", SolarGuard.getInstance());
            }
        }

        // 3. Validação de Latência: Prevenção de falso-positivos por instabilidade (Lag)
        final int playerPing = LatencyUtils.getPing(player);
        if (playerPing > 350) {
            return;
        }

        final Location from = event.getFrom();
        final Location to = event.getTo();
        
        // Verificação de segurança para evitar NullPointerException
        if (to == null) return;

        // 4. Análise de Movimentação Horizontal (Speed Check)
        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();
        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        // Limite heurístico dinâmico conforme o estado de corrida do jogador
        double maxVelocity = player.isSprinting() ? 0.65 : 0.45;

        // 5. Análise de Movimentação Vertical (Fly Check)
        double deltaY = to.getY() - from.getY();
        boolean isAirborne = player.getLocation().getBlock().getRelative(0, -1, 0).getType() == Material.AIR;

        // Detecção de "Hover/Glide" (Suspensão aérea sem gravidade aplicada)
        if (deltaY == 0.0 && isAirborne && horizontalDistance > 0.15) {
            this.processViolation(player, "Suspensão Aérea Indevida (Fly/Glide)");
            event.setTo(from); // Aplica retrocesso imediato
            return;
        }

        // Detecção de Velocidade Excessiva em Solo (Speed)
        if (horizontalDistance > maxVelocity && !isAirborne) {
            this.processViolation(player, "Velocidade Horizontal Anômala (Speed)");
            event.setTo(from);
        }
    }

    /**
     * Gerencia o acúmulo de infrações e executa as medidas punitivas formais.
     * * @param player O jogador sob investigação.
     * @param reason O diagnóstico técnico da infração.
     */
    private void processViolation(Player player, String reason) {
        final UUID uuid = player.getUniqueId();
        final int currentViolations = this.violationTracker.getOrDefault(uuid, 0) + 1;
        this.violationTracker.put(uuid, currentViolations);

        // Notificação administrativa em tempo real para equipe de moderação
        String alertMessage = ChatColor.translateAlternateColorCodes('&', 
            "&8[&bSolarGuard&8] &7O jogador &b" + player.getName() + " &7falhou no teste: &f" + reason + " &8(&c" + currentViolations + "vl&8)");
        
        Bukkit.getOnlinePlayers().stream()
            .filter(staff -> staff.hasPermission("solarguard.alerts"))
            .forEach(staff -> staff.sendMessage(alertMessage));

        // Verificação do limiar de punição automática
        if (currentViolations >= 12) {
            this.violationTracker.remove(uuid);

            // Despacho assíncrono do relatório para o Discord Webhook
            WebhookDispatcher.sendAlert(player.getName(), reason);

            // Retorno à Thread Principal para execução de sanções disciplinares
            Bukkit.getScheduler().runTask(SolarGuard.getInstance(), () -> {
                player.kickPlayer(ChatColor.translateAlternateColorCodes('&', 
                    "&c[SolarGuard] \n\nSua conexão foi encerrada.\nO sistema detectou inconsistências cinemáticas em seu cliente."));
                
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "SolarGuard » " + ChatColor.GRAY + "O jogador " + 
                        ChatColor.WHITE + player.getName() + ChatColor.GRAY + " foi removido por detecção de trapaça.");
                Bukkit.broadcastMessage("");
            });
        }
    }
}
