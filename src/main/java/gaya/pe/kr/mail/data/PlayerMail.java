package gaya.pe.kr.mail.data;

import gaya.pe.kr.core.util.method.PlayerUtil;
import gaya.pe.kr.mail.reactor.MailBox;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerMail {

    UUID ownerUUID;
    List<ItemStack> mailItemList = new ArrayList<>();
    int offlineReceivedItemAmount; // 오프라인동안 받은 아이템 목록
    Date lastJoinDate = new Date();

    public PlayerMail(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public void openMail(Player player) {
        MailBox mailBox = new MailBox(player, this);
        mailBox.open();
    }

    public void addMail(Player sender, List<ItemStack> itemStacks) {

        Player player = Bukkit.getPlayer(getOwnerUUID());

        mailItemList.addAll(itemStacks);
        int itemSize = itemStacks.size();

        if ( PlayerUtil.isOnline(player) ) {
            // Online 일 경우
            player.sendMessage(String.format("%s 님으로 부터 %d개의 우편물이 도착했습니다!", sender.getName(), itemSize));
        } else {
            // Offline 일 경우
            offlineReceivedItemAmount += itemSize;
        }

    }

    public void viewOfflineReceivedItemAmount(Player player) {
        if ( offlineReceivedItemAmount > 0 ) {
            player.sendMessage(String.format("%d개의 우편물이 도착했습니다!", offlineReceivedItemAmount));
            offlineReceivedItemAmount = 0;
        }
    }


    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public List<ItemStack> getMailItemList() {
        return mailItemList;
    }

    public int getOfflineReceivedItemAmount() {
        return offlineReceivedItemAmount;
    }

    public void setLastJoinDate(Date lastJoinDate) {
        this.lastJoinDate = lastJoinDate;
    }

    public Date getLastJoinDate() {
        return lastJoinDate;
    }

    public ItemStack getItem(ItemStack itemStack) {
        mailItemList.remove(itemStack);
        ItemStack clone = itemStack.clone();
        itemStack.setAmount(0);
        itemStack.setType(Material.AIR);
        return clone;
    }

    public void setMailItemList(List<ItemStack> mailItemList) {
        this.mailItemList = mailItemList;
    }

    public void setOfflineReceivedItemAmount(int offlineReceivedItemAmount) {
        this.offlineReceivedItemAmount = offlineReceivedItemAmount;
    }
}
