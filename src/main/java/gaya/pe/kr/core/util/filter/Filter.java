package gaya.pe.kr.core.util.filter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Filter {

    public static boolean isNullOrAirItem(ItemStack itemStack) {
        if ( itemStack == null ) {
            return true;
        }
        else {
            return itemStack.getType().equals(Material.AIR);
        }
    }


}
