package gaya.pe.kr.mail.option;

import gaya.pe.kr.core.util.method.ItemModifier;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum FrameIcon {

    ENABLE_PREVIOUS_PAGE(ItemModifier.createItemStack(Material.BONE, 1034, "&7[ &6&l이전 &7]", null)),
    DISABLE_PREVIOUS_PAGE(ItemModifier.createItemStack(Material.BONE, 1035, "&7[ &x&9&3&8&d&9&4&l이전 &7]", null)),
    ENABLE_NEXT_PAGE(ItemModifier.createItemStack(Material.BONE, 1032, "&7[ &6&l다음 &7]", null)),
    DISABLE_NEXT_PAGE(ItemModifier.createItemStack(Material.BONE, 1033, "&7[ &x&9&3&8&d&9&4&l다음 &7]", null)),
    NOW_PAGE(ItemModifier.createItemStack(Material.BONE, 1023, "", null)),
    BLANK(ItemModifier.createItemStack(Material.BONE, 1023, " ", null)),
    ONLINE_BUTTON(ItemModifier.createItemStack(Material.LIME_STAINED_GLASS_PANE, 0, "&a온라인 플레이어", null)),
    OFFLINE_BUTTON(ItemModifier.createItemStack(Material.RED_STAINED_GLASS_PANE, 0, "&7오프라인 플레이어", null)),
    EXIT(ItemModifier.createItemStack(Material.BARRIER, 0, "&c나가기", null)),
    MAIL_SEND(ItemModifier.createItemStack(Material.BONE, 1023, "&a&l보내기", null)),
    SEND_MAIL_BUTTON(ItemModifier.createItemStack(Material.PAPER, 0, "&a&l우편 보내기", null)),
    EXIT_MAIL_SEND_GUI(ItemModifier.createItemStack(Material.BONE, 1023, "&c&l취소", null));

    final ItemStack itemStack;

    FrameIcon(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }
}
