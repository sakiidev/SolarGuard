package me.saki.solarguard.utils;

import org.bukkit.entity.Player;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Utilitário formal para análise de latência de rede.
 */
public class LatencyUtils {

    /**
     * Recupera o tempo de resposta (Ping) do jogador via reflexão.
     * @param player O jogador a ser analisado.
     * @return int O valor do ping em milissegundos.
     */
    public static int getPing(Player player) {
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Field pingField = entityPlayer.getClass().getField("ping");
            return pingField.getInt(entityPlayer);
        } catch (Exception e) {
            // Caso a versão do servidor mude a estrutura do NMS
            return 0; 
        }
    }
}
