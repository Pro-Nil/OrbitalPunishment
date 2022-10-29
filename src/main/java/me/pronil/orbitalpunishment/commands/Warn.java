package me.pronil.orbitalpunishment.commands;

import me.pronil.orbitalpunishment.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Warn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permissions.WARN.get())) {
            sender.sendMessage("§cYou do not have permission to execute this command!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cIncorrect syntax. Correct: /warn <name> <reason>");
            return true;
        }
        for(int i=0;i< args.length;i++){
            System.out.println(args[i]);
        }
         Player targetPlayer = Bukkit.getPlayerExact(args[0]);
        UUID target = targetPlayer.getUniqueId();

        if (target == null) {
            sender.sendMessage("§cCan't find player '" + args[0] + "'.");
            return true;
        }



        StringBuilder reason = new StringBuilder();

        for (int i = 1; i < args.length; ++i) {
            reason.append(args[i]).append(" ");
        }

        reason = new StringBuilder(reason.substring(0, reason.length() - 1));

        String id = Utils.getRandomHexString(8);
        while (DatabaseUtils.getPunishment(id) != null) {
            id = Utils.getRandomHexString(8);
        }

        long issuedAt = System.currentTimeMillis() / 1000L;

        Punishment punishment = new Punishment(
            Punishment.PunishmentType.WARN,
            target,
            (sender instanceof Player) ? ((Player) sender).getUniqueId() : new UUID(0,0),
            id,
            reason.toString(),
            issuedAt,
            -1
        );

        DatabaseUtils.addPunishment(punishment);
          System.out.println(targetPlayer + reason.toString());
        MessageUtils.sendWarn(targetPlayer, reason.toString());

        sender.sendMessage("§aWarned '" + args[0] + "'.");

        return true;
    }
}
