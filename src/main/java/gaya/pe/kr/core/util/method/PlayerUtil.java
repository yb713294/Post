package gaya.pe.kr.core.util.method;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerUtil {

    public static String getPlayerName(UUID targetPlayerUUID) {
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if ( offlinePlayer.getUniqueId().equals(targetPlayerUUID) ) {
                return offlinePlayer.getName();
            }
        }
        return null;
    }

    public static Player getPlayer(Player player, String targetPlayerName) {
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (targetPlayer != null) {
            if (targetPlayer.isOnline()) {
                return targetPlayer;
            }
        }
        player.sendMessage("§c존재하지 않거나 접속중이지 않은 플레이어 입니다");
        return null;
    }

    public static boolean isOnline(Player player) {
        if ( player != null) {
            return player.isOnline();
        }
        return false;
    }



}
