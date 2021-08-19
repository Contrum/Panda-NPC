package dev.panda.npc.npc.event;

import dev.panda.npc.npc.NPC;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class NPCInteractEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final NPC npc;
    private final Action action;

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
