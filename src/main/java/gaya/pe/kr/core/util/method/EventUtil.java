package gaya.pe.kr.core.util.method;

import gaya.pe.kr.core.ItemMail;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class EventUtil {

    public static void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, ItemMail.getPlugin());
    }

}
