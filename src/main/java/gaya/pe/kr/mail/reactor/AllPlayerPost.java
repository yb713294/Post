package gaya.pe.kr.mail.reactor;

import gaya.pe.kr.core.util.SchedulerUtil;
import gaya.pe.kr.core.util.abstaract.MinecraftInventoryListener;
import gaya.pe.kr.core.util.filter.Filter;
import gaya.pe.kr.core.util.method.ItemModifier;
import gaya.pe.kr.core.util.method.UtilMethod;
import gaya.pe.kr.mail.manager.MailManager;
import gaya.pe.kr.mail.option.FrameIcon;
import gaya.pe.kr.mail.option.PostType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AllPlayerPost extends MinecraftInventoryListener {

    int page = 1;
    PostType postType;
    public AllPlayerPost(Player player, PostType postType) {
        super(player);
        this.postType = postType;
    }

    public boolean open() {

        MailManager mailManager = MailManager.getInstance();
        int startIndex = (page-1) * 27;
        int lastIndex = (page * 27);
        List<UUID> allPlayerUUIDList = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getUniqueId).collect(Collectors.toList());
        List<UUID> onlinePlayerUUIDList = Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).collect(Collectors.toList());
        allPlayerUUIDList.removeAll(onlinePlayerUUIDList);
        allPlayerUUIDList.remove(getPlayer().getUniqueId()); // 자기 자신의 머리 제거
        List<UUID> allPlayerUUID = postType.equals(PostType.ONLINE) ? onlinePlayerUUIDList : allPlayerUUIDList;
        int size = allPlayerUUID.size();

        if ( startIndex > lastIndex || page < 1 || size < startIndex ) {
            getPlayer().sendMessage("§c접근할 수 없는 페이지 입니다");
            return false;
        }

        initInventory(Bukkit.createInventory(null, 54, "목록"));

        int inventoryIndex = 18;
        for ( int index = startIndex; index < lastIndex; index++ ) {
            if ( index < size ) {
                getInventory().setItem(inventoryIndex, mailManager.getPlayerHead(allPlayerUUID.get(index)) );
                inventoryIndex++;
            } else {
                break;
            }
        }

        for ( int index = 45; index < 54; index++ ) {
            getInventory().setItem(index, FrameIcon.BLANK.getItemStack());
        }

        if ( page <= 1 ) {
            getInventory().setItem(45, FrameIcon.DISABLE_PREVIOUS_PAGE.getItemStack()); // 이전 페이지 비활성화
        } else {
            getInventory().setItem(45, FrameIcon.ENABLE_PREVIOUS_PAGE.getItemStack()); // 이전 페이지 활성화
        }

        getInventory().setItem(47, FrameIcon.ONLINE_BUTTON.getItemStack()); // 온라인 버튼
        getInventory().setItem(49, ItemModifier.setDisplayName(FrameIcon.NOW_PAGE.getItemStack(), Integer.toString(page))); // 나가기
        getInventory().setItem(51, FrameIcon.OFFLINE_BUTTON.getItemStack()); // 오프라인 버튼

        if ( size >= lastIndex ) {
            getInventory().setItem(53, FrameIcon.ENABLE_NEXT_PAGE.getItemStack()); // 이후 페이지 활성화
        } else {
            getInventory().setItem(53, FrameIcon.DISABLE_NEXT_PAGE.getItemStack()); // 이후 페이지 비활성화
        }

        getPlayer().openInventory(getInventory());

        return true;


    }


    @Override
    @EventHandler
    public void clickInventory(InventoryClickEvent event ) {

        if ( isAccessible(event) ) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();

            if ( !Filter.isNullOrAirItem(clickedItem) ) {

                int clickedSlot = event.getSlot();
                event.setCancelled(true);

                if ( clickedSlot > 44 ) {
                    // 각종 옵션들을 클릭함
                    switch ( clickedSlot ) {
                        case 45 -> {
                            page--;
                            if ( !open() ) {
                                page++;
                            }
                        }
                        case 47 -> {
                            AllPlayerPost allPlayerPost = new AllPlayerPost(getPlayer(), PostType.ONLINE);
                            allPlayerPost.open();
                        }
                        case 51 -> {
                            AllPlayerPost allPlayerPost = new AllPlayerPost(getPlayer(), PostType.OFFLINE);
                            allPlayerPost.open();
                        }
                        case 53 -> {
                            page++;
                            if ( !open() ) {
                                page--;
                            }
                        }
                    }
                } else {
                    // 아이템을 뺄 수 있도록함
                    MailManager mailManager = MailManager.getInstance();
                    String targetHeadUUID = mailManager.getItemOwnerUUID(clickedItem);
                    if ( !getPlayer().getUniqueId().toString().equalsIgnoreCase(targetHeadUUID) ) {
                        PostMail postMail = new PostMail(getPlayer(), mailManager.getPlayerMail(UUID.fromString(targetHeadUUID)));
                        postMail.open();
                    } else {
                        getPlayer().sendMessage("§c자기 자신에게 메일을 보낼 수 없습니다");
                    }
                }

            }
        }


    }

    @Override
    public void initInventory(Inventory inventory) {
        setInventory(inventory);
    }

}
