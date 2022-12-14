package me.pronil.orbitalpunishment.commands;

import me.pronil.orbitalpunishment.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TempBan implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permissions.TEMPBAN.get())) {
            sender.sendMessage("§cYou do not have permission to execute this command!");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("§cIncorrect syntax. Correct: /tempban <name> <length> <reason>");
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

        if (DatabaseUtils.hasActiveBan(target)) {
            sender.sendMessage("§cTarget already banned. '/unban " + args[0] + "'.");
            return true;
        }

        long issuedAt = System.currentTimeMillis() / 1000L;
        long expiresAt = issuedAt + Utils.parsePeriod(args[1]) / 1000L - 1;

        Punishment punishment = new Punishment(
            Punishment.PunishmentType.BAN,
            target,
            (sender instanceof Player) ? ((Player) sender).getUniqueId() : new UUID(0,0),
            id,
            reason.toString(),
            issuedAt,
            expiresAt
        );

        DatabaseUtils.addPunishment(punishment);

        if (targetPlayer != null && targetPlayer.isOnline()) {
            targetPlayer.kickPlayer(MessageUtils.getTempBan(id, reason.toString(), expiresAt));
        }

        sender.sendMessage("§aTemporarily banned '" + args[0] + "' for " + args[1] + ".");

        return true;
    }
}
