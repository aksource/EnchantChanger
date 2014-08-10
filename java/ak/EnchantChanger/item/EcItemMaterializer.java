package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.ExtendedPlayerData;
import ak.EnchantChanger.StatCheckUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class EcItemMaterializer extends EcItem
{

	public EcItemMaterializer(String name) {
		super(name);
		maxStackSize = 1;
		setMaxDamage(0);
		this.setTextureName(EnchantChanger.EcTextureDomain + "PortableEnchantChanger");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
//		par3EntityPlayer.openGui(EnchantChanger.instance, EnchantChanger.guiIdMaterializer,par2World,0,0,0);
        if (par2World.isRemote) return par1ItemStack;
//        NBTTagCompound nbt = par3EntityPlayer.getEntityData();
        boolean flag = !ExtendedPlayerData.get(par3EntityPlayer).getSoldierMode();
        ExtendedPlayerData.get(par3EntityPlayer).setSoldierMode(flag);
        par3EntityPlayer.addChatComponentMessage(new ChatComponentText("Materia Setting Mode : " + flag));
        //For MCE2
        ExtendedPlayerData.get(par3EntityPlayer).setSoldierWorkStartTime(par2World.getWorldTime());
        ExtendedPlayerData.get(par3EntityPlayer).setMobKillCount(StatCheckUtils.getTotalMobKillCount(par3EntityPlayer));

        par1ItemStack.stackSize--;
		return par1ItemStack;
	}
}