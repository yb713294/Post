package gaya.pe.kr.mail.command;

import gaya.pe.kr.mail.data.PlayerMail;
import gaya.pe.kr.mail.manager.MailManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MailCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if ( sender instanceof Player player ) {
            MailManager mailManager = MailManager.getInstance();
            PlayerMail playerMail = mailManager.getPlayerMail(player.getUniqueId());
            playerMail.openMail(player);
        }

        return false;
    }
}
