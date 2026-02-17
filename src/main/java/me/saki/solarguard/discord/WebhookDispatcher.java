package me.saki.solarguard.discord;

import me.saki.solarguard.SolarGuard;
import org.bukkit.Bukkit;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Gerencia a comunicação assíncrona com a API de Webhooks do Discord.
 */
public class WebhookDispatcher {

    /**
     * Despacha uma notificação formal de punição para o canal configurado no Discord.
     * * @param playerName Nome do jogador infrator.
     * @param reason     Motivo detalhado da infração detectada.
     */
    public static void sendAlert(String playerName, String reason) {
        final String webhookUrl = SolarGuard.getInstance().getConfig().getString("webhook-url");

        if (webhookUrl == null || webhookUrl.isEmpty() || webhookUrl.equals("URL_AQUI")) {
            return;
        }

        // Processamento assíncrono para evitar interrupções no ciclo do servidor (TPS)
        Bukkit.getScheduler().runTaskAsynchronously(SolarGuard.getInstance(), () -> {
            try {
                URL url = new URL(webhookUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("User-Agent", "SolarGuard-AntiCheat");
                connection.setDoOutput(true);

                // Construção do payload JSON conforme especificações estéticas
                String jsonPayload = "{"
                        + "\"embeds\": [{"
                        + "\"author\": { \"name\": \"🔨 BANIDO\" },"
                        + "\"title\": \"" + playerName + "\","
                        + "\"color\": 16733525," // Vermelho Claro decimal (#FF5555)
                        + "\"description\": \"### 📣 Motivo\\n" + reason + "\","
                        + "\"thumbnail\": { \"url\": \"https://mc-heads.net/avatar/" + playerName + "\" },"
                        + "\"footer\": { \"text\": \"SolarGuard Security System • 2026\" }"
                        + "}]"
                        + "}";

                try (OutputStream outputStream = connection.getOutputStream()) {
                    byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                    outputStream.write(input, 0, input.length);
                }

                // Validação da resposta da API
                int responseCode = connection.getResponseCode();
                if (responseCode != 204 && responseCode != 200) {
                    SolarGuard.getInstance().getLogger().warning("A API do Discord retornou o código: " + responseCode);
                }

                connection.disconnect();
            } catch (Exception exception) {
                SolarGuard.getInstance().getLogger().severe("Erro crítico ao enviar Webhook: " + exception.getMessage());
            }
        });
    }
}
