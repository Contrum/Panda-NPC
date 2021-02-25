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
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.pandamc.npc.PandaNPC;
import us.pandamc.npc.npc.NPC;
import us.pandamc.npc.utils.CC;
import us.pandamc.npc.utils.NPCUtils;
import us.pandamc.npc.utils.nms.NPCPackets;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPCPackets1_16 implements NPCPackets {

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

        entityPlayer.setPosition(npc.getLocation().getX(), npc.getLocation().getY(), npc.getLocation().getZ());
        entityPlayer.getDataWatcher().set(new DataWatcherObject<>(12, DataWatcherRegistry.a), (byte) 0xFF);

        PacketPlayOutPlayerInfo packetPlayOutPlayerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
        playerEp.playerConnection.sendPacket(packetPlayOutPlayerInfo);

        PacketPlayOutNamedEntitySpawn namedEntitySpawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        playerEp.playerConnection.sendPacket(namedEntitySpawn);

        final List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R3.ItemStack>> equipmentList = new ArrayList<>();
        if(npc.getHand() != null) equipmentList.add(new Pair<>(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(npc.getHand())));
        if(npc.getHelmet() != null) equipmentList.add(new Pair<>(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(npc.getHelmet())));
        if(npc.getChest() != null) equipmentList.add(new Pair<>(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(npc.getChest())));
        if(npc.getLegs() != null) equipmentList.add(new Pair<>(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(npc.getLegs())));
        if(npc.getBoots() != null) equipmentList.add(new Pair<>(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(npc.getBoots())));

        if(!equipmentList.isEmpty()) playerEp.playerConnection.sendPacket(new PacketPlayOutEntityEquipment(entityPlayer.getId(), equipmentList));

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