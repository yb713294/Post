package gaya.pe.kr.core;

import gaya.pe.kr.mail.manager.MailManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Level;

public final class ItemMail extends JavaPlugin {

    private static Plugin plugin;
    MailManager mailManager = MailManager.getInstance();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        mailManager.init();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        mailManager.close();
    }

    public static void log(String message) {
        Bukkit.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', message));
    }


    public static void registerCommand(String command, CommandExecutor commandExecutor) {
        Bukkit.getPluginCommand(command).setExecutor(commandExecutor);
        log(String.format("&f[&6&l%s&f]의 클래스가 정상적으로 커맨드 핸들러에 등록됐습니다 커맨드 : &f[&6&l%s&f]", commandExecutor.getClass().getName(), command));
    }

    public static void registerCommandTab(String command, TabCompleter tabCompleter) {
        Bukkit.getPluginCommand(command).setTabCompleter(tabCompleter);
        log(String.format("&f[&6&l%s&f]의 클래스가 정상적으로 Tab Handler 에 등록됐습니다 커맨드 : &f[&6&l%s&f]", tabCompleter.getClass().getName(), command));
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static BukkitScheduler getBukkitScheduler() {
        return Bukkit.getScheduler();
    }

}
