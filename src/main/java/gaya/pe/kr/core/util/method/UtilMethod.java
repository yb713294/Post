package gaya.pe.kr.core.util.method;

import gaya.pe.kr.core.util.filter.Filter;
import gaya.pe.kr.mail.data.PlayerMail;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

public class UtilMethod {

    public static int getPlayerRemainInventory(ItemStack[] itemStacks) {
        int count = 0;
        for (ItemStack content : itemStacks) {
            if ( Filter.isNullOrAirItem(content) ) {
                count++;
            }
        }
        return count;
    }

    public static void addPlayerItem(Player player, ItemStack... itemStacks) {
        PlayerInventory playerInventory = player.getInventory();
        for (ItemStack itemStack : itemStacks) {
            if ( !Filter.isNullOrAirItem(itemStack) ) {
                if ( getPlayerRemainInventory(playerInventory.getStorageContents()) > 0 ) {
                    playerInventory.addItem(itemStacks);
                } else {
                    // 칸이 없음
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                }
            }
        }

    }

    public static ItemStack getPlayerHead(Player player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwnerProfile(player.getPlayerProfile());
        head.setItemMeta(meta);
        return head;
    }

}
