package us.pandamc.npc.utils.nms.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_14_R1.*;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.pandamc.npc.PandaNPC;
import us.pandamc.npc.npc.NPC;
import us.pandamc.npc.utils.CC;
import us.pandamc.npc.utils.NPCUtils;
import us.pandamc.npc.utils.nms.NPCPackets;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NPCPackets1_14 implements NPCPackets {

    @Override
    public void spawn(NPC npc, Player player) {
        CraftPlayer playerCp = (CraftPlayer)player;
        EntityPlayer playerEp = playerCp.getHandle();

        WrappedGameProfile profile = WrappedGameProfile.fromPlayer(player);

        WrappedSignedProperty current = Iterables.getFirst(profile.getProperties().get("textures"), null);

        String texture = "";
        String signature = "";
        if(current  != null){
            texture = current.getValue();
            signature = current.getSignature();
        }

        if(npc.getSignature() != null && npc.getTexture() != null){
            texture = npc.getTexture();
            signature = npc.getSignature();
        }

        if(npc.getDisplayName() == null || npc.getLocation() == null ) return;

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), CC.translate(npc.getDisplayName()));

        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        EntityPlayer entityPlayer = new EntityPlayer(MinecraftServer.getServer(),
            ((CraftWorld)npc.getLocation().getWorld()).getHandle(),
            gameProfile,
            new PlayerInteractManager(((CraftWorld)npc.getLocation().getWorld()).getHandle()));

        entityPlayer.getDataWatcher().set(new DataWatcherObject<>(12, DataWatcherRegistry.a), (byte) 0xFF);
        entityPlayer.setPosition(npc.getLocation().getX(), npc.getLocation().getY(), npc.getLocation().getZ());

        PacketPlayOutPlayerInfo packetPlayOutPlayerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
        playerEp.playerConnection.sendPacket(packetPlayOutPlayerInfo);

        PacketPlayOutNamedEntitySpawn namedEntitySpawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        playerEp.playerConnection.sendPacket(namedEntitySpawn);

        List<PacketPlayOutEntityEquipment> entityEquipments = Lists.newArrayList();

        if(npc.getHand() != null) entityEquipments.add(new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(npc.getHand())));
        if(npc.getHelmet() != null) entityEquipments.add(new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(npc.getHelmet())));
        if(npc.getChest() != null) entityEquipments.add(new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(npc.getChest())));
        if(npc.getLegs() != null) entityEquipments.add(new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(npc.getLegs())));
        if(npc.getBoots() != null) entityEquipments.add(new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.FEET, CraftItemStack.asNMSCopy(npc.getBoots())));

        entityEquipments.forEach(packet -> playerEp.playerConnection.sendPacket(packet));

        new BukkitRunnable(){
            @Override
            public void run(){
                PacketPlayOutPlayerInfo packetPlayOutPlayerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
                playerEp.playerConnection.sendPacket(packetPlayOutPlayerInfo);
            }
        }.runTaskLaterAsynchronously(PandaNPC.get(), 20L * 5);

        npc.getEntitys().put(playerEp.getUniqueID(),entityPlayer.getId());

        PacketPlayOutEntity.PacketPlayOutEntityLook look = new PacketPlayOutEntity.PacketPlayOutEntityLook(entityPlayer.getId(),
            (byte) NPCUtils.getCompressedAngle(npc.getYaw()),
            (byte) NPCUtils.getCompressedAngle(npc.getPitch()),
            true);
        playerEp.playerConnection.sendPacket(look);

        PacketPlayOutEntityHeadRotation headRotation = new PacketPlayOutEntityHeadRotation(entityPlayer,
            (byte) NPCUtils.getCompressedAngle(npc.getHeadYaw()));
        playerEp.playerConnection.sendPacket(headRotation);
    }

    @Override
    public void destroy(NPC npc, Player player) {
        if (npc.getEntitys().containsKey(player.getUniqueId())) {
            CraftPlayer playerCp = (CraftPlayer)player;
            EntityPlayer playerEp = playerCp.getHandle();

            PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(npc.getEntitys().get(player.getUniqueId()));

            playerEp.playerConnection.sendPacket(destroy);
        }
    }
}