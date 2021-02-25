package us.pandamc.npc.utils.cooldown;

import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

@Getter
public class Cooldown {

    @Getter
    private static Map<String, Cooldown> cooldownMap = Maps.newHashMap();
    private Map<UUID, Long> longMap;
    private String name, displayName, expiredMessage;
    private long duration;

    public Cooldown(String name, long duration) {
        this(name, duration, null, null);
    }

    public Cooldown(String name, long duration, String displayName, String expiredMessage) {
        this.longMap = Maps.newHashMap();
        this.name = name;
        this.duration = duration;
        this.displayName = ((displayName == null) ? name : displayName);
        if (expiredMessage != null) {
            this.expiredMessage = expiredMessage;
        }
        cooldownMap.put(name, this);
    }

    public void setCooldown(Player player) {
        this.setCooldown(player, false);
    }

    public void setCooldown(Player player, boolean announce) {
        this.longMap.put(player.getUniqueId(), System.currentTimeMillis() + this.duration);
    }

    public void setCooldown(Player player, boolean announce, long duration) {
        this.longMap.put(player.getUniqueId(), System.currentTimeMillis() + duration);
    }

    public long getDuration(Player player) {
        return longMap.getOrDefault(player.getUniqueId(), 0L) - System.currentTimeMillis();
    }

    public boolean isOnCooldown(Player player) {
        return this.getDuration(player) > 0L;
    }

    public boolean remove(Player player) {
        if (isOnCooldown(player)) {
            this.longMap.remove(player.getUniqueId());
            //new CooldownExpiredEvent(player, this).setCancelled(true).call();
        }
        return isOnCooldown(player);
    }
}
