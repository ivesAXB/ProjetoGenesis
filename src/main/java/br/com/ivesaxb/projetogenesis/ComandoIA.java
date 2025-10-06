package br.com.ivesaxb.projetogenesis;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.IronGolem;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class ComandoIA implements CommandExecutor {

    private final ProjetoGenesis plugin;

    // O construtor agora exige uma referência ao plugin principal (o "Gerente")
    public ComandoIA(ProjetoGenesis plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando só pode ser usado por um jogador.");
            return true;
        }

        Player player = (Player) sender;
        Entity npc = plugin.getNpc(); // Pega a referência do NPC a partir do plugin principal

        if (args.length == 0) {
            player.sendMessage("§cUse: /ia <spawn | remove | attack on/off>");
            return true;
        }

        if (args[0].equalsIgnoreCase("spawn")) {
            if (npc != null && !npc.isDead()) {
                player.sendMessage("§eO NPC Gênesis já existe!"); return true;
            }
            Location spawnLocation = player.getLocation();
            IronGolem golemNpc = (IronGolem) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.IRON_GOLEM);
            golemNpc.setCustomName("§b§lGênesis");
            golemNpc.setCustomNameVisible(true);
            golemNpc.setSilent(true); // Opcional, para não fazer sons
            golemNpc.setInvulnerable(true);
            plugin.setNpc(golemNpc); // Salva a referência do golem
            player.sendMessage("§aNPC Gênesis (Golem) foi criado!");
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (npc == null || npc.isDead()) {
                player.sendMessage("§eNão há NPC para remover."); return true;
            }
            npc.remove();
            plugin.setNpc(null);
            if (plugin.getCombatTask() != null) {
                plugin.getCombatTask().cancel();
                plugin.setCombatTask(null);
            }
            player.sendMessage("§aNPC Gênesis foi removido.");
            return true;
        }

        if (args[0].equalsIgnoreCase("attack")) {
            if (npc == null || npc.isDead()) {
                player.sendMessage("§cO NPC não existe! Crie um com /ia spawn.");
                return true;
            }
            if (args.length < 2) {
                player.sendMessage("§cUse: /ia attack <on | off>");
                return true;
            }

            if (args[1].equalsIgnoreCase("on")) {
                if (plugin.getCombatTask() != null) {
                    player.sendMessage("§eO modo de combate já está ativado.");
                    return true;
                }

                player.sendMessage("§d[DEBUG] Criando e agendando a CombatTask...");
                CombatTask task = new CombatTask(npc);
                BukkitTask bukkitTask = task.runTaskTimer(plugin, 0L, 20L); // A tarefa roda a cada 1 segundo
                plugin.setCombatTask(bukkitTask);
                player.sendMessage("§aModo de combate ATIVADO.");
            } else if (args[1].equalsIgnoreCase("off")) {
                if (plugin.getCombatTask() == null) {
                    player.sendMessage("§eO modo de combate já está desativado.");
                    return true;
                }
                plugin.getCombatTask().cancel();
                plugin.setCombatTask(null);
                if (npc instanceof Mob) {
                    ((Mob) npc).setTarget(null);
                }
                player.sendMessage("§aModo de combate DESATIVADO.");
            }
            return true;
        }

        player.sendMessage("§cSub-comando desconhecido.");
        return true;
    }
}