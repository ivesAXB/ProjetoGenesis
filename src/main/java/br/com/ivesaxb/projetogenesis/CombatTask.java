package br.com.ivesaxb.projetogenesis;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Comparator;
import java.util.Optional;

public class CombatTask extends BukkitRunnable {

    private final Mob npc;

    public CombatTask(Entity npc) {
        if (npc instanceof Mob) {
            this.npc = (Mob) npc;
        } else {
            this.npc = null;
            this.cancel();
        }
    }

    @Override
    public void run() {
        if (npc == null || npc.isDead()) {
            this.cancel();
            return;
        }

        // A CADA SEGUNDO, procuramos pelo melhor alvo possível.
        Optional<Entity> bestTarget = npc.getWorld().getNearbyEntities(npc.getLocation(), 15, 15, 15).stream()
                .filter(entity -> entity instanceof Monster && entity.isValid() && entity != this.npc)
                .min(Comparator.comparingDouble(entity -> entity.getLocation().distance(npc.getLocation())));

        // Verificamos o resultado da nossa busca.
        if (bestTarget.isPresent()) {
            // SE ENCONTRAMOS um monstro, nós o definimos como alvo.
            // Isso sobrescreve qualquer alvo que a IA nativa tenha escolhido (como o jogador).
            LivingEntity target = (LivingEntity) bestTarget.get();
            npc.setTarget(target);
            // E mandamos ele se mover.
            npc.getPathfinder().moveTo(target.getLocation(), 1.2);
        } else {
            // SE NÃO ENCONTRAMOS nenhum monstro, limpamos o alvo do NPC.
            // Isso o impede de atacar jogadores ou vaguear sem rumo. Ele ficará parado.
            npc.setTarget(null);
        }
    }
}