package me.saki.solarguard.commands;

import me.saki.solarguard.SolarGuard;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Interface de comando administrativo para a gestão do SolarGuard.
 * Implementa lógica de subcomandos para auxílio, diagnóstico e manutenção.
 */
public class SolarGuardCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Validação de autoridade administrativa
        if (!sender.hasPermission("solarguard.admin")) {
            sender.sendMessage(ChatColor.RED + "Erro de autorização: Privilégios insuficientes.");
            return true;
        }

        // Processamento de subcomandos
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            this.displayFormalHelpMenu(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            this.executeReloadProcedure(sender);
            return true;
        }

        // Tratamento de argumentos inválidos
        sender.sendMessage(ChatColor.RED + "Sintaxe incorreta. Utilize /solarguard help para diretrizes.");
        return true;
    }

    /**
     * Executa o procedimento de recarregamento dos arquivos de configuração.
     * @param sender O executor do procedimento.
     */
    private void executeReloadProcedure(CommandSender sender) {
        try {
            SolarGuard.getInstance().reloadConfig();
            sender.sendMessage(ChatColor.GOLD + "SolarGuard » " + ChatColor.GREEN + "Os arquivos de configuração foram reindexados com sucesso.");
        } catch (Exception exception) {
            sender.sendMessage(ChatColor.RED + "Falha crítica ao recarregar configurações: " + exception.getMessage());
        }
    }

    /**
     * Apresenta a interface gráfica textual de auxílio ao usuário.
     * @param sender O receptor da interface.
     */
    private void displayFormalHelpMenu(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GOLD + " " + ChatColor.BOLD + "CENTRAL DE COMANDOS - SOLARGUARD");
        sender.sendMessage(ChatColor.GRAY + " Monitoramento Heurístico & Proteção de Rede");
        sender.sendMessage("");
        
        sender.sendMessage(ChatColor.YELLOW + " /solarguard help " + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "Apresenta este menu de orientações técnicas.");
        sender.sendMessage(ChatColor.YELLOW + " /solarguard reload " + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "Atualiza o config.yml e as rotas de Webhook.");
        
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GOLD + " DIAGNÓSTICO DE INFRAESTRUTURA:");
        
        String webhookStatus = SolarGuard.getInstance().getConfig().getString("webhook-url");
        boolean isWebhookActive = webhookStatus != null && webhookStatus.startsWith("http");

        sender.sendMessage(ChatColor.GRAY + " • Sistema de Detecção: " + ChatColor.GREEN + "OPERACIONAL");
        sender.sendMessage(ChatColor.GRAY + " • Discord Webhook: " + (isWebhookActive ? ChatColor.GREEN + "ATIVO" : ChatColor.RED + "INATIVO"));
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_GRAY + " Desenvolvido sob padrões de excelência por saki.");
    }
}
