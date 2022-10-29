package me.pronil.orbitalpunishment.commands;

import me.pronil.orbitalpunishment.utils.DatabaseUtils;
import me.pronil.orbitalpunishment.utils.Permissions;
import me.pronil.orbitalpunishment.utils.Punishment;
import me.pronil.orbitalpunishment.utils.UUIDFetcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class Unban implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permissions.UNBAN.get())) {
            sender.sendMessage("§cYou do not have permission to execute this command!");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cIncorrect syntax. Correct: /unban <name>");
            return true;
        }

        UUID target = UUIDFetcher.getUUIDOf(args[0]);
           System.out.println(target);
        if (target == null) {
            sender.sendMessage("§cCan't find player '" + args[0] + "'.");
            return true;
        }

        DatabaseUtils.getPunishments(target).forEach(punishment -> {
            if (punishment.getType() == Punishment.PunishmentType.BAN) {
                DatabaseUtils.setRevoked(punishment.getId(), true);
            }
        });

        sender.sendMessage("§aUnbanned '" + args[0] + "'.");

        return true;
    }
}
