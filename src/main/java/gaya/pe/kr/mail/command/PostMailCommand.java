package gaya.pe.kr.mail.command;

import gaya.pe.kr.mail.option.PostType;
import gaya.pe.kr.mail.reactor.AllPlayerPost;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PostMailCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if ( sender instanceof Player player ) {
            AllPlayerPost allPlayerPost = new AllPlayerPost(player, PostType.ONLINE);
            allPlayerPost.open();
            return true;
        }

        return false;

    }
}
