package ak.EnchantChanger.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import ak.EnchantChanger.EcItemSephirothSword;
import ak.EnchantChanger.EnchantChanger;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ClientTickHandler
{
	@SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event)
    {
    	EntityPlayer player = event.player;
    	World world = player.worldObj;
    	if(player == null || !world.isRemote) return;
    	Minecraft mc = Minecraft.getMinecraft();
    	ItemStack ep = player.getCurrentEquippedItem();
    	int reach = 10;
    	MovingObjectPosition MOP;
    	if(ep != null && ep.getItem() instanceof EcItemSephirothSword){
    		mc.objectMouseOver = EnchantChanger.getMouseOverCustom(player, world, reach);
    		if(mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectType.ENTITY && mc.objectMouseOver.entityHit instanceof EntityLivingBase){
    			mc.pointedEntity = (EntityLivingBase) mc.objectMouseOver.entityHit;
    		}
    	}
    }

}