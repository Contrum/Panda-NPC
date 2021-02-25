package us.pandamc.npc.npc.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import us.pandamc.npc.npc.NPC;

@Getter
public class NPCInteractEvent extends PlayerEvent {
    private static HandlerList handlers = new HandlerList();
    private NPC npc;
    private Action action;

    public NPCInteractEvent(Player player , NPC npc , Action action){
        super(player);
        this.npc = npc;
        this.action = action;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public enum Action {
        LEFT_CLICK, RIGHT_CLICK
    }
}
