package br.com.ivesaxb.projetogenesis;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class ProjetoGenesis extends JavaPlugin {

    private Entity npc;
    private BukkitTask combatTask; // A tarefa de combate

    @Override
    public void onEnable() {
        getLogger().info("O ProjetoGenesis foi ativado com sucesso!");
        // O Gerente agora entrega um "crachá" de acesso a si mesmo para o Executor de Comandos
        this.getCommand("ia").setExecutor(new ComandoIA(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("O ProjetoGenesis foi desativado.");
        // Opcional: remover o NPC quando o plugin desliga
        if (npc != null) {
            npc.remove();
        }
    }

    // Métodos para que outras classes possam acessar e modificar o NPC e as tarefas
    public Entity getNpc() {
        return npc;
    }

    public void setNpc(Entity npc) {
        this.npc = npc;
    }

    public BukkitTask getCombatTask() {
        return combatTask;
    }

    public void setCombatTask(BukkitTask combatTask) {
        this.combatTask = combatTask;
    }
}