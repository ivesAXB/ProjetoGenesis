package br.com.ivesaxb.projetogenesis;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

// A interface "CommandExecutor" nos obriga a criar um metodo que lida com o comando
public class ComandoIA implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Verifica se quem digitou o comando foi um jogador
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Envia uma mensagem de confirmação para o jogador
            player.sendMessage("§aComando /ia recebido!");
        } else {
            // Mensagem para o console, se o comando for digitado lá
            sender.sendMessage("Este comando só pode ser usado por um jogador.");
        }

        // Retorna true para indicar que o comando foi tratado com sucesso
        return true;
    }
}