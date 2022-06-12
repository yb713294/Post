package gaya.pe.kr.mail.reactor;

import gaya.pe.kr.core.util.SchedulerUtil;
import gaya.pe.kr.core.util.abstaract.MinecraftInventoryListener;
import gaya.pe.kr.core.util.filter.Filter;
import gaya.pe.kr.core.util.method.ItemModifier;
import gaya.pe.kr.core.util.method.UtilMethod;
import gaya.pe.kr.mail.data.PlayerMail;
import gaya.pe.kr.mail.option.FrameIcon;
import gaya.pe.kr.mail.option.PostType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MailBox extends MinecraftInventoryListener {

    PlayerMail playerMail;
    int page = 1;

    public MailBox(Player player, PlayerMail playerMail) {
        super(player);
        this.playerMail = playerMail;
    }

    public boolean open() {

        int startIndex = (page-1) * 27;
        int lastIndex = (page * 27);
        List<ItemStack> mailBox = playerMail.getMailItemList();
        int size = mailBox.size();

        if ( startIndex > lastIndex || page < 1 || size < startIndex ) {
            getPlayer().sendMessage("§c접근할 수 없는 페이지 입니다");
            return false;
        }


        Inventory inventory = Bukkit.createInventory(null, 36, "메일 보관함");
        initInventory(inventory);

        int inventoryIndex = 0;
        for ( int index = startIndex; index < lastIndex; index++ ) {
            if ( index < size ) {
                getInventory().setItem(inventoryIndex, mailBox.get(index));
                inventoryIndex++;
            } else {
                break;
            }
        }

        for ( int index = 27; index < 36; index++ ) {
            getInventory().setItem(index, FrameIcon.BLANK.getItemStack());
        }

        if ( page <= 1 ) {
            getInventory().setItem(29, FrameIcon.DISABLE_PREVIOUS_PAGE.getItemStack()); // 이전 페이지 비활성화
        } else {
            getInventory().setItem(29, FrameIcon.ENABLE_PREVIOUS_PAGE.getItemStack()); // 이전 페이지 활성화
        }

        getInventory().setItem(31, FrameIcon.NOW_PAGE.getItemStack()); // 나가기

        if ( size >= lastIndex ) {
            getInventory().setItem(33, FrameIcon.ENABLE_NEXT_PAGE.getItemStack()); // 이후 페이지 활성화
        } else {
            getInventory().setItem(33, FrameIcon.DISABLE_NEXT_PAGE.getItemStack()); // 이후 페이지 비활성화
        }

        getPlayer().openInventory(getInventory());

        return true;

    }

    @Override
    @EventHandler
    public void clickInventory(InventoryClickEvent event ) {

        if (isAccessible(event, true)) {
            ItemStack clickedItem = event.getCurrentItem();
            if (!Filter.isNullOrAirItem(clickedItem)) {

                int clickedSlot = event.getSlot();

                if (clickedSlot > 26) {

                    // 각종 옵션들을 클릭함
                    switch (clickedSlot) {
                        case 29 -> {
                            page--;
                            if (!open()) {
                                page++;
                            }
                        }
                        case 31 -> {
                            getPlayer().closeInventory();
                        }
                        case 33 -> {
                            page++;
                            if (!open()) {
                                page--;
                            }
                        }
                    }
                } else {
                    // 아이템을 뺄 수 있도록함
                    ItemStack result = playerMail.getItem(clickedItem);
                    UtilMethod.addPlayerItem(getPlayer(), result);
                }


            }
        }


    }

    @Override
    public void initInventory(Inventory inventory) {
        setInventory(inventory);
    }

}
