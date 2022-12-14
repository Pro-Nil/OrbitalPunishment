package me.pronil.orbitalpunishment.commands;

import me.pronil.orbitalpunishment.utils.DatabaseUtils;
import me.pronil.orbitalpunishment.utils.Permissions;
import me.pronil.orbitalpunishment.utils.Punishment;
import me.pronil.orbitalpunishment.utils.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Unmute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permissions.UNMUTE.get())) {
            sender.sendMessage("§cYou do not have permission to execute this command!");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cIncorrect syntax. Correct: /unmute <name>");
            return true;
        }

        UUID target = UUIDFetcher.getUUIDOf(args[0]);

        if (target == null) {
            sender.sendMessage("§cCan't find player '" + args[0] + "'.");
            return true;
        }

        DatabaseUtils.getPunishments(target).forEach(punishment -> {
            if (punishment.getType() == Punishment.PunishmentType.MUTE) {
                DatabaseUtils.setRevoked(punishment.getId(), true);
            }
        });

        sender.sendMessage("§aUnmuted '" + args[0] + "'.");

        Player player = Bukkit.getPlayer(target);
        if (player != null) player.sendMessage("§aYou have been unmuted.");

        return true;
    }
}
