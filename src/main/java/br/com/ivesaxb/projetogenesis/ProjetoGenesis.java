package br.com.ivesaxb.projetogenesis;

import org.bukkit.plugin.java.JavaPlugin;

public final class ProjetoGenesis extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("O ProjetoGenesis foi ativado com sucesso!");
    }
    @Override
    public void onDisable() {
        getLogger().info("O ProjetoGenesis foi desativado.");
    }
}

@Override
public void onEnable() {
    getLogger().info("O ProjetoGenesis foi ativado com sucesso!");

    // Adicione esta linha para registrar o executor do comando
    this.getCommand("ia").setExecutor(new ComandoIA());
}