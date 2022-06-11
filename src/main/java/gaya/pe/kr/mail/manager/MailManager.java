package gaya.pe.kr.mail.manager;

import gaya.pe.kr.core.ItemMail;
import gaya.pe.kr.core.manager.ConfigurationManager;
import gaya.pe.kr.core.util.SchedulerUtil;
import gaya.pe.kr.core.util.filter.Filter;
import gaya.pe.kr.core.util.method.*;
import gaya.pe.kr.mail.command.MailCommand;
import gaya.pe.kr.mail.data.PlayerMail;
import gaya.pe.kr.mail.listener.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MailManager {

    private static class SingleTon {
        private static final MailManager MAIL_MANAGER = new MailManager();
    }

    public static MailManager getInstance() {
        return SingleTon.MAIL_MANAGER;
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    ConfigurationManager configurationManager; // Configuration Manager
    FileConfiguration configuration; // Configuration Data
    final String RELATIVE_PATH = "data/data.yml";

    HashMap<UUID, ItemStack> playerHeadHashMap = new HashMap<>();
    HashMap<UUID, PlayerMail> playerMailHashMap = new HashMap<>();

    NamespacedKey namespacedKey;

    public void init() {
        namespacedKey = new NamespacedKey(ItemMail.getPlugin(), "item_owner_uuid");
        ItemMail.registerCommand("우편함", new MailCommand());
        EventUtil.register(new PlayerConnection());
        loadConfiguration();
    }

    public void close() {

        configuration.set("player_mail_box", null);

        playerMailHashMap.forEach( (uuid, playerMail) -> {

            String path = "player_mail_box."+uuid.toString();

            int offlineReceivedItemAmount = playerMail.getOfflineReceivedItemAmount();
            String lastJoinDate = simpleDateFormat.format(playerMail.getLastJoinDate());

            configuration.set(path+".offline_received_item_amount", offlineReceivedItemAmount);
            configuration.set(path+".last_join_date", lastJoinDate);

            List<String> mailBoxItemData = playerMail.getMailItemList().stream().map(ObjectConverter::getObjectAsString).toList();

            configuration.set(path+".mail_item_data", mailBoxItemData); // 메일 아이템 데이터
        });
        configurationManager.saveConfiguration(configuration, RELATIVE_PATH);


    }

    private void loadConfiguration() {
        configurationManager = ConfigurationManager.getInstance();
        configuration = configurationManager.getConfiguration(RELATIVE_PATH, RELATIVE_PATH); // Core Configuration
        try {
            int count = 0;
            for (String uuid : configuration.getConfigurationSection("player_mail_box").getKeys(false)) {

                String path = "player_mail_box."+uuid;
                UUID playerUUID = UUID.fromString(uuid);
                PlayerMail playerMail = new PlayerMail(playerUUID);
                int offlineReceivedItemAmount = configuration.getInt(path+".offline_received_item_amount");
                String lastJoinDateStr = configuration.getString(path+".last_join_date");
                Date lastJoinDate = simpleDateFormat.parse(lastJoinDateStr);

                List<ItemStack> mailData = new ArrayList<>(configuration.getStringList(path + ".mail_item_data").stream().map(itemData -> (ItemStack) ObjectConverter.getObject(itemData)).toList());

                playerMail.setMailItemList(mailData);
                playerMail.setLastJoinDate(lastJoinDate);
                playerMail.setOfflineReceivedItemAmount(offlineReceivedItemAmount);
                playerMailHashMap.put(playerUUID, playerMail);
                count++;
            }

            ItemMail.log(String.format("%d 명의 우편함 데이터가 로딩되었습니다", count));

        } catch ( NullPointerException e ) {
            ItemMail.log("플레이어 메일 데이터가 존재하지 않습니다ㅏ");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void addPlayerHead(Player player) {
        SchedulerUtil.runAsync( ()-> playerHeadHashMap.put(player.getUniqueId(), UtilMethod.getPlayerHead(player)) );
    }

    public void removePlayerHead(Player player) {
        playerHeadHashMap.remove(player.getUniqueId());
    }

    public ItemStack getPlayerHead(UUID targetPlayerUUID) {
        ItemStack head;
        PlayerMail playerMail = getPlayerMail(targetPlayerUUID);
        String playerName = PlayerUtil.getPlayerName(targetPlayerUUID);
        String ownerUUID = targetPlayerUUID.toString();
        if ( playerHeadHashMap.containsKey(targetPlayerUUID) ) {
            head = playerHeadHashMap.get(targetPlayerUUID).clone();
            ItemModifier.setHeadData(head, ChatColor.GREEN+playerName,0, namespacedKey, ownerUUID,"&6접속 정보 : &aOnline");
        } else {
            head = new ItemStack(Material.PLAYER_HEAD);
            ItemModifier.setHeadData(head, ChatColor.GRAY+playerName,0, namespacedKey, ownerUUID, String.format("&6접속 정보 : &7%s", simpleDateFormat.format(playerMail.getLastJoinDate())));
        }

        return head;

    }

    public PlayerMail getPlayerMail(UUID targetPlayerUUID) {
        PlayerMail playerMail;
        if ( playerMailHashMap.containsKey(targetPlayerUUID) ) {
            playerMail = playerMailHashMap.get(targetPlayerUUID);
        } else {
            playerMail = new PlayerMail(targetPlayerUUID);
            playerMailHashMap.put(targetPlayerUUID, playerMail);
        }
        return playerMail;
    }

    public String getItemOwnerUUID(ItemStack itemStack) {
        if ( !Filter.isNullOrAirItem(itemStack) ) {
            if ( itemStack.hasItemMeta() ) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
                if ( persistentDataContainer.has(namespacedKey, PersistentDataType.STRING) ) {
                    return persistentDataContainer.get(namespacedKey, PersistentDataType.STRING);
                }
            }
        }
        return null;
    }

}
