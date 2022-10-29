package me.pronil.orbitalpunishment.handler;

import me.pronil.orbitalpunishment.utils.DatabaseUtils;
import me.pronil.orbitalpunishment.utils.MessageUtils;
import me.pronil.orbitalpunishment.utils.Punishment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;

public class BanHandler implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ArrayList<Punishment> punishments = DatabaseUtils.getPunishments(player.getUniqueId());
        punishments.forEach(punishment -> {
            if (punishment.getType() == Punishment.PunishmentType.BAN) {
                if (!punishment.getRevoked()) {
                    if (punishment.getExpiresAt() == -1) {
                        event.getPlayer().kickPlayer(MessageUtils.getPermBan(punishment.getId(), punishment.getReason()));


                    }
                    else if (punishment.getExpiresAt() > (System.currentTimeMillis() / 1000L)) {
                        event.getPlayer().kickPlayer(MessageUtils.getTempBan(
                            punishment.getId(),
                            punishment.getReason(),
                            punishment.getExpiresAt()
                        ));
                    }
                    Bukkit.getConsoleSender().sendMessage(
                        player.getName() + " (" + event.getPlayer().getUniqueId() + ") attempted to join while banned. Ban ID: #"
                            + punishment.getId()
                    );
                }
            }
        });
    }
}
