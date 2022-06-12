package gaya.pe.kr.mail.command;

import gaya.pe.kr.core.util.method.PlayerUtil;
import gaya.pe.kr.mail.data.PlayerMail;
import gaya.pe.kr.mail.manager.MailManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MailCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if ( sender instanceof Player player ) {

            MailManager mailManager = MailManager.getInstance();

            if ( args.length > 0 ) {
                if ( player.isOp() ) {
                    String targetPlayerName = args[0];
                    UUID targetPlayerUUID = PlayerUtil.getPlayerUUID(targetPlayerName);
                    if ( targetPlayerUUID != null ) {
                        PlayerMail targetPlayerMail = mailManager.getPlayerMail(targetPlayerUUID);
                        targetPlayerMail.openMail(player);
                    } else {
                        player.sendMessage("§c존재하지 않는 플레이어 입니다");
                    }
                }
            } else {
                PlayerMail playerMail = mailManager.getPlayerMail(player.getUniqueId());
                playerMail.openMail(player);
            }




        }

        return false;
    }
}
