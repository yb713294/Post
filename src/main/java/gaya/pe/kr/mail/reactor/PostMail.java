package gaya.pe.kr.mail.reactor;

import gaya.pe.kr.core.util.abstaract.MinecraftInventoryListener;
import gaya.pe.kr.core.util.filter.Filter;
import gaya.pe.kr.core.util.method.UtilMethod;
import gaya.pe.kr.mail.data.PlayerMail;
import gaya.pe.kr.mail.option.FrameIcon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PostMail extends MinecraftInventoryListener {

    PlayerMail targetPlayerMail;
    boolean sendData;

    public PostMail(Player player, PlayerMail targetPlayerMail) {
        super(player);
        this.targetPlayerMail = targetPlayerMail;
    }

    public void open() {

        Inventory inventory = Bukkit.createInventory(null, 36, "메일 보내기");
        initInventory(inventory);

        for ( int index = 27; index < 36; index++ ) {
            inventory.setItem(index, FrameIcon.BLANK.getItemStack());
        }
        inventory.setItem(30, FrameIcon.EXIT_MAIL_SEND_GUI.getItemStack()); // 나가기
        inventory.setItem(32, FrameIcon.MAIL_SEND.getItemStack()); // 전송하기

        getPlayer().openInventory(getInventory());


    }

    @Override
    public void initInventory(Inventory inventory) {
        setInventory(inventory);
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory closedInventory = event.getInventory();

        if ( isPlayerInteraction(player, closedInventory) ) {
            unRegister();
            if ( !sendData ) {
                for (ItemStack inventoryItem : getInventoryItems()) {
                    UtilMethod.addPlayerItem(getPlayer(), inventoryItem);
                }
            }
        }
    }

    @Override
    @EventHandler
    public void clickInventory(InventoryClickEvent event) {

        if ( isAccessible(event, false) ) {
            int clickedIndex = event.getSlot();
            if ( clickedIndex > 26 ) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                switch ( clickedIndex ) {
                    case 30 -> getPlayer().closeInventory();
                    case 32 -> {
                        List<ItemStack> mailItemList = getInventoryItems();
                        if ( mailItemList.isEmpty() ) {
                            getPlayer().sendMessage("§c보낼 우편물이 없습니다");
                        } else {
                            getTargetPlayerMail().addMail(getPlayer(), mailItemList);
                            getPlayer().sendMessage("§6성공적으로 메일을 보냈습니다");
                            sendData = true;
                            getPlayer().closeInventory();
                        }
                    }
                }
            }


        }

    }

    public PlayerMail getTargetPlayerMail() {
        return targetPlayerMail;
    }
    
    private List<ItemStack> getInventoryItems() {
        List<ItemStack> itemStacks = new ArrayList<>();
        ItemStack[] contents = getInventory().getContents();
        for (int i = 0; i < getInventory().getContents().length; i++) {
            ItemStack content = contents[i];
            if ( i < 27 ) {
                if (!Filter.isNullOrAirItem(content)) {
                    itemStacks.add(content);
                }
            }
        }
        return itemStacks;
    }
    
}
