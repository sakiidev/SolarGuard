package me.saki.solarguard;

import me.saki.solarguard.commands.SolarGuardCommand;
import me.saki.solarguard.listeners.PlayerMoveListener;
import me.saki.solarguard.listeners.TeleportListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Ponto de entrada definitivo do sistema SolarGuard.
 * Orquestra o registro de serviços, listeners e controladores de comando.
 */
public class SolarGuard extends JavaPlugin {

    private static SolarGuard instance;

    @Override
    public void onEnable() {
        instance = this;

        // Inicialização de recursos de configuração
        this.saveDefaultConfig();

        // Registro de componentes de software
        this.registerInternalServices();

        this.getLogger().info("-------------------------------------------------------");
        this.getLogger().info("   SolarGuard Anti-Cheat - Enterprise Edition          ");
        this.getLogger().info("   Status: Proteção Global Habilitada                  ");
        this.getLogger().info("-------------------------------------------------------");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("[SolarGuard] Serviços de proteção encerrados formalmente.");
    }

    /**
     * Centraliza o registro de todos os controladores e ouvintes de eventos.
     */
    private void registerInternalServices() {
        // Registro de Executores de Comando
        if (this.getCommand("solarguard") != null) {
            this.getCommand("solarguard").setExecutor(new SolarGuardCommand());
        }

        // Registro de Ouvintes de Eventos (Listeners)
        this.getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        this.getServer().getPluginManager().registerEvents(new TeleportListener(), this);
    }

    /**
     * Fornece a instância singleton para o ecossistema do plugin.
     * @return SolarGuard Instância ativa.
     */
    public static SolarGuard getInstance() {
        return instance;
    }
}
