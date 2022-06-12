package gaya.pe.kr.core.util.method;

import gaya.pe.kr.core.DellunaPostOffice;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class EventUtil {

    public static void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, DellunaPostOffice.getPlugin());
    }

}
