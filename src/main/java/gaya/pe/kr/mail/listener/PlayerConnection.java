package gaya.pe.kr.mail.listener;

import gaya.pe.kr.mail.data.PlayerMail;
import gaya.pe.kr.mail.manager.MailManager;
import jdk.jfr.Enabled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;

public class PlayerConnection implements Listener {

    MailManager mailManager = MailManager.getInstance();

    @EventHandler
    public void joinPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerMail playerMail = mailManager.getPlayerMail(player.getUniqueId());
        mailManager.addPlayerHead(player);
        playerMail.viewOfflineReceivedItemAmount(player);
        playerMail.setLastJoinDate(new Date());
    }

    @EventHandler
    public void quitPlayer(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        mailManager.removePlayerHead(player);
    }

}
