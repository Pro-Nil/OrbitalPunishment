package me.pronil.orbitalpunishment.commands;

import me.pronil.orbitalpunishment.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Mute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permissions.MUTE.get())) {
            sender.sendMessage("§cYou do not have permission to execute this command!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cIncorrect syntax. Correct: /mute <name> <reason>");
            return true;
        }

        UUID target = UUIDFetcher.getUUIDOf(args[0]);

        if (target == null) {
            sender.sendMessage("§cCan't find player '" + args[0] + "'.");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(target);

        StringBuilder reason = new StringBuilder();

        for (int i = 2; i < args.length; ++i) {
            reason.append(args[i]).append(" ");
        }

        reason = new StringBuilder(reason.substring(0, reason.length() - 1));

        String id = Utils.getRandomHexString(8);
        while (DatabaseUtils.getPunishment(id) != null) {
            id = Utils.getRandomHexString(8);
        }

        if (DatabaseUtils.hasActiveMute(target)) {
            sender.sendMessage("§cTarget already muted. '/unmute " + args[0] + "'.");
            return true;
        }

        long issuedAt = System.currentTimeMillis() / 1000L;

        Punishment punishment = new Punishment(
            Punishment.PunishmentType.MUTE,
            target,
            (sender instanceof Player) ? ((Player) sender).getUniqueId() : new UUID(0,0),
            id,
            reason.toString(),
            issuedAt,
            -1
        );

        DatabaseUtils.addPunishment(punishment);

        if (targetPlayer != null && targetPlayer.isOnline()) {
            MessageUtils.sendPermMute(targetPlayer, id, reason.toString());
        }

        sender.sendMessage("§aMuted '" + args[0] + "' for " + args[1] + ".");

        return true;
    }
}
