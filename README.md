# 🛡️ SolarGuard

**SolarGuard** é um plugin completo de proteção e segurança para servidores Minecraft (Spigot/Paper 1.8-1.20). Desenvolvido para manter seu servidor seguro contra ameaças, exploits e ataques.

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Minecraft](https://img.shields.io/badge/minecraft-1.8--1.20-green)
![License](https://img.shields.io/badge/license-Proprietary-red)

---

## ⚡ Funcionalidades

### 🛡️ **Anti-Exploit**
- Proteção contra exploits de duplicação
- Bloqueio de glitches de inventário
- Prevenção de crash do servidor

### 🤖 **Anti-Bot**
- Bloqueio de bots de ataque
- Sistema de captcha automatizado
- Limite de conexões por IP

### 🌍 **Anti-VPN/Proxy**
- Bloqueio de VPNs e proxies
- Verificação de IP em tempo real
- Whitelist por país (opcional)

### 🔒 **Proteção de Comandos**
- Blacklist de comandos perigosos
- Sistema de permissões avançado
- Log de comandos suspeitos

### 📋 **Sistema de Whitelist**
- Whitelist automática
- Sistema de convites
- Expiração automática

### ⚙️ **Outras Proteções**
- Anti-OP (proteção contra dar OP)
- Anti-Console Spam
- Limite de pacotes por segundo
- Proteção de portas

---

## 📥 Instalação

1. Baixe o arquivo `SolarGuard.jar`
2. Coloque na pasta `plugins/` do seu servidor
3. Reinicie o servidor
4. Configure o arquivo `config.yml`

```yaml
# Exemplo de configuração
anti_bot:
  enabled: true
  captcha: true
  max_connections_per_ip: 3
  
anti_vpn:
  enabled: true
  block_proxy: true
  api_key: "sua_chave_aqui"

whitelist:
  enabled: false
  auto_add_players: true
```

---

📋 Comandos

Comando Descrição Permissão
/sguard Menu principal solar.guard.admin
/sguard reload Recarregar config solar.guard.admin
/sguard whitelist add <player> Adicionar à whitelist solar.guard.whitelist
/sguard whitelist remove <player> Remover da whitelist solar.guard.whitelist
/sguard status Ver status das proteções solar.guard.admin

---

🔐 Permissões
```
Permissão Descrição
solar.guard.* Todas as permissões
solar.guard.admin Acesso administrativo
solar.guard.whitelist Gerenciar whitelist
solar.guard.bypass Ignorar proteções
```
---

🛠️ Compilação

```bash
# Clone o repositório
git clone https://github.com/sakiidev/SolarGuard.git

# Entre na pasta
cd SolarGuard

# Compile com Maven
mvn clean package
```

O arquivo compilado estará em target/SolarGuard-1.0.0.jar

---

📦 Dependências

· Spigot/Paper 1.8 - 1.20
· Java 8 ou superior
· MySQL (opcional, para dados avançados)

---

📊 Logs e Monitoramento

O SolarGuard mantém logs detalhados de todas as ameaças bloqueadas:

```
[SolarGuard] Bloqueado IP 123.456.789.0 - VPN detectada
[SolarGuard] Bot bloqueado - Conexões excessivas
[SolarGuard] Comando bloqueado: /plugins (sem permissão)
```

---

👤 Autor

Saki - Desenvolvido com 💜 para a comunidade Minecraft

· GitHub: @sakiidev
· Discord: sakiidev

---

📄 Licença

```
Copyright (c) 2025 Saki. Todos os direitos reservados.

Este software é propriedade exclusiva de Saki.
É proibida a distribuição, modificação ou venda sem autorização expressa do autor.
```

---

⭐ Suporte

Se você gostou do plugin e quer apoiar o desenvolvimento:

· Deixe uma ⭐ no GitHub
· Reporte bugs na issues page
· Compartilhe com outros administradores

---

🔗 Links Úteis

· Repositório GitHub
· Discord de Suporte

---

SolarGuard - Protegendo servidores desde 2025 🛡️
