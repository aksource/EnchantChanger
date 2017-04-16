package ak.EnchantChanger.eventhandler;

import ak.EnchantChanger.ExtendedPlayerData;
import ak.EnchantChanger.network.MessagePlayerJoinInAnnouncement;
import ak.EnchantChanger.network.MessagePlayerProperties;
import ak.EnchantChanger.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by A.K. on 14/10/12.
 */
public class PlayerCustomDataHandler {

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            ExtendedPlayerData.register((EntityPlayer) event.entity);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            PacketHandler.INSTANCE.sendToServer(new MessagePlayerJoinInAnnouncement(player));
        }
    }

    @SubscribeEvent
    //Dimension移動時や、リスポーン時に呼ばれるイベント。古いインスタンスと新しいインスタンスの両方を参照できる。
    public void onClonePlayer(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        //死亡時に呼ばれてるかどうか
        if (event.wasDeath) {
            //古いカスタムデータ
            IExtendedEntityProperties oldEntityProperties = event.original.getExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME);
            //新しいカスタムデータ
            IExtendedEntityProperties newEntityProperties = event.entityPlayer.getExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME);
            NBTTagCompound playerData = new NBTTagCompound();
            //データの吸い出し
            oldEntityProperties.saveNBTData(playerData);
            //データの書き込み
            newEntityProperties.loadNBTData(playerData);

        }
    }

    @SubscribeEvent
    public void loggedIn(PlayerEvent.PlayerLoggedInEvent event) {

    }

    @SubscribeEvent
    public void loggedOut(PlayerEvent.PlayerLoggedOutEvent event) {

    }

    @SubscribeEvent
    public void respawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        if (!event.player.worldObj.isRemote) {
            PacketHandler.INSTANCE.sendTo(new MessagePlayerProperties(event.player), (EntityPlayerMP) event.player);
        }
    }
}
