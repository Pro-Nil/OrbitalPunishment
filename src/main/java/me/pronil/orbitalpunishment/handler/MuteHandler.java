package me.pronil.orbitalpunishment.handler;

import me.pronil.orbitalpunishment.OrbitalPunishment;
import me.pronil.orbitalpunishment.utils.DatabaseUtils;
import me.pronil.orbitalpunishment.utils.MessageUtils;
import me.pronil.orbitalpunishment.utils.Punishment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

public class MuteHandler implements Listener {

    private static final List<?> BLACKLISTED_CMDS = OrbitalPunishment.instance.getConfig().getList("mute-commands");

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (containsBlacklisted(event.getMessage())) {
            Player player = event.getPlayer();

            ArrayList<Punishment> punishments = DatabaseUtils.getPunishments(player.getUniqueId());
            punishments.forEach(punishment -> {
                if (punishment.getType() == Punishment.PunishmentType.MUTE) {

                    if (!punishment.getRevoked()) {
                        if (punishment.getExpiresAt() == -1) {
                            event.setCancelled(true);
                            MessageUtils.sendPermMute(
                                    player,
                                    punishment.getId(),
                                    punishment.getReason()
                            );
                        } else if (punishment.getExpiresAt() > (System.currentTimeMillis() / 1000L)) {
                            event.setCancelled(true);
                            MessageUtils.sendMute(
                                player,
                                punishment.getId(),
                                punishment.getReason(),
                                punishment.getExpiresAt()
                            );

                        }
                    }
                }
            });
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        ArrayList<Punishment> punishments = DatabaseUtils.getPunishments(player.getUniqueId());
        punishments.forEach(punishment -> {
            if (punishment.getType() == Punishment.PunishmentType.MUTE) {
                if (!punishment.getRevoked()) {
                    if (punishment.getExpiresAt() == -1) {
                        event.setCancelled(true);
                        MessageUtils.sendPermMute(
                                player,
                                punishment.getId(),
                                punishment.getReason()
                        );
                    } else if (punishment.getExpiresAt() > (System.currentTimeMillis() / 1000L)) {
                        event.setCancelled(true);
                        MessageUtils.sendMute(
                                player,
                                punishment.getId(),
                                punishment.getReason(),
                                punishment.getExpiresAt()
                        );

                    }
                }
            }
        });
    }


    private static boolean containsBlacklisted(String msg) {
        for (Object cmd : BLACKLISTED_CMDS) {
            if (msg.startsWith((String) cmd)) return true;
        }
        return false;
    }
}
