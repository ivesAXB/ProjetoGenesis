package br.com.ivesaxb.projetogenesis;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;

import java.util.Comparator;

public class CombatTask extends BukkitRunnable {

    private final Mob npc; // Usamos Mob em vez de Entity para ter acesso ao getTarget() e Pathfinder

    public CombatTask(Entity npc) {
        // Fazemos um "cast" aqui para garantir que estamos lidando com um Mob
        if (npc instanceof Mob) {
            this.npc = (Mob) npc;
        } else {
            // Se a entidade não for um Mob, cancelamos a tarefa pois não funcionaria
            this.npc = null;
            this.cancel();
        }
    }

    @Override
    public void run() {
        Bukkit.getLogger().info("[DEBUG] CombatTask.run() foi chamado!"); // MENSAGEM 1

        // Verificações de segurança
        if (npc == null || npc.isDead()) {
            this.cancel();
            return;
        }

        // Se o NPC já tem um alvo vivo, deixa ele terminar o que está fazendo
        if (npc.getTarget() != null && npc.getTarget().isValid()) {
            return;
        }

        // Procura por entidades hostis em um raio de 15 blocos
        npc.getWorld().getNearbyEntities(npc.getLocation(), 15, 15, 15).stream()
                // Filtra a lista para manter apenas as que são "Monstros" e não estão mortas
                .filter(entity -> entity instanceof Monster && entity.isValid())
                // Encontra o monstro mais próximo do NPC
                .min(Comparator.comparingDouble(entity -> entity.getLocation().distance(npc.getLocation())))
                // Se encontrou algum...
                .ifPresent(target -> {
                    // ...define ele como o alvo oficial do NPC
                    npc.setTarget((LivingEntity) target);
                    // E manda o NPC andar até ele usando a IA de navegação do próprio jogo
                    npc.getPathfinder().moveTo(target.getLocation(), 1.2);
                });
    }
}