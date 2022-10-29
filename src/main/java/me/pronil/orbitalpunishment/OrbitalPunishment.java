package me.pronil.orbitalpunishment;

import me.pronil.orbitalpunishment.commands.*;
import me.pronil.orbitalpunishment.handler.BanHandler;
import me.pronil.orbitalpunishment.handler.MuteHandler;
import me.pronil.orbitalpunishment.utils.BanFilter;
import net.minecraft.server.v1_8_R3.LoginListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class OrbitalPunishment extends JavaPlugin {

    public static OrbitalPunishment instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        ((Logger) LogManager.getLogger(LoginListener.class)).addFilter(new BanFilter());

        Bukkit.getConsoleSender().sendMessage("§8[§aOrbitalPunishment§8] §aPlugin loaded.");

        getCommand("warn").setExecutor(new Warn());
        getCommand("kick").setExecutor(new Kick());


        try {

            Server bukkit = Bukkit.getServer();
            Method method = bukkit.getClass().getMethod("getCommandMap");
            ((CommandMap) method.invoke(bukkit)).register("customBook", new TempMute());

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        getCommand("mute").setExecutor(new Mute());
        getCommand("unmute").setExecutor(new Unmute());

        getCommand("tempban").setExecutor(new TempBan());
        getCommand("ban").setExecutor(new Ban());
        getCommand("unban").setExecutor(new Unban());

        getCommand("phistory").setExecutor(new PunishmentHistory());
        getCommand("getpunishment").setExecutor(new GetPunishment());

        Bukkit.getPluginManager().registerEvents(new BanHandler(), this);
        Bukkit.getPluginManager().registerEvents(new MuteHandler(), this);
    }

    @Override
    public void onDisable() {
    }
}
