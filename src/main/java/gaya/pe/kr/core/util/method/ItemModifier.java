package gaya.pe.kr.core.util.method;

import gaya.pe.kr.core.util.filter.Filter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemModifier {

    public static ItemStack createItemStack(Material material, int customModelData, String displayName, List<String> lore) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName.replace("&","§"));
        itemMeta.setCustomModelData(customModelData);
        if ( lore != null ) {
            itemMeta.setLore(lore);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack setDisplayName(ItemStack itemStack, String displayName) {
        if ( !Filter.isNullOrAirItem(itemStack) ) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(displayName.replace("&", "§"));
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        return null;
    }

    public static void setHeadData(ItemStack itemStack, String displayName, int customModelData, NamespacedKey namespacedKey, String ownerUUID, String... loreStr) {
        if ( !Filter.isNullOrAirItem(itemStack) ) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(displayName.replace("&", "§"));
            itemMeta.setCustomModelData(customModelData);
            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            persistentDataContainer.set(namespacedKey, PersistentDataType.STRING, ownerUUID);
            List<String> lore = (itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>());
            for (String s : loreStr) {
                lore.add(s.replace("&", "§"));
            }
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
        }
    }

}
