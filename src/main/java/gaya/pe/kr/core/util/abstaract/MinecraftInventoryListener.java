package gaya.pe.kr.core.util.abstaract;

import gaya.pe.kr.core.util.method.EventUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public abstract class MinecraftInventoryListener implements Listener {

    Player player;
    Inventory inventory;

    public MinecraftInventoryListener(Player player) {
        this.player = player;
        register();
    }

    protected void register() {
        EventUtil.register(this);
    }

    protected void unRegister() {
        HandlerList.unregisterAll(this);
    }

    protected boolean isPlayerInteraction(Player targetPlayer, Inventory targetInventory) {
        return getPlayer().equals(targetPlayer) && getInventory().equals(targetInventory);
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public abstract void initInventory(Inventory inventory);

    @EventHandler
    public abstract void clickInventory(InventoryClickEvent event);

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent event) {

        Player player = (Player) event.getPlayer();
        Inventory closedInventory = event.getInventory();

        if ( isPlayerInteraction(player, closedInventory) ) {
            unRegister();
        }

    }

    protected boolean isAccessible(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        if ( clickedInventory == null) return true;
        return isPlayerInteraction(player, clickedInventory);
    }

}
