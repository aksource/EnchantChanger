package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.inventory.EcCloudSwordData;
import ak.EnchantChanger.inventory.EcInventoryCloudSword;
import ak.EnchantChanger.network.MessageCloudSword;
import ak.EnchantChanger.network.PacketHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

import java.util.List;

public class EcItemCloudSword extends EcItemSword
{
	private EcInventoryCloudSword SwordData = null;

	public EcItemCloudSword()
	{
		super(ToolMaterial.EMERALD);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if (getSlotNum(stack) == 5)
			return false;
		else if (this.SwordData.getStackInSlot(getSlotNum(stack)) != null) {
			this.attackTargetEntityWithTheItem(entity, player, this.SwordData.getStackInSlot(getSlotNum(stack)), false);
			return true;
		} else
			return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (par3EntityPlayer.isSneaking()) {
			this.doCastOffSwords(par2World, par3EntityPlayer);
			return this.makeCloudSwordCore(par1ItemStack);
		} else {
			par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
			if (!par2World.isRemote) {
    			increaseSlotNum(par1ItemStack);
                PacketHandler.INSTANCE.sendTo(new MessageCloudSword((byte)this.getSlotNum(par1ItemStack)), (EntityPlayerMP) par3EntityPlayer);
				if (this.SwordData != null && getSlotNum(par1ItemStack) != 5 && this.SwordData
						.getStackInSlot(getSlotNum(par1ItemStack)) != null) {
					par3EntityPlayer.addChatMessage(new ChatComponentText(this.SwordData
							.getStackInSlot(getSlotNum(par1ItemStack))
							.getDisplayName()));
				}
			}
			return par1ItemStack;
		}
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
		if (!par2World.isRemote && par3Entity instanceof EntityPlayer) {
            if(this.SwordData == null)
                this.SwordData = new EcInventoryCloudSword(par1ItemStack, par2World);
			this.SwordData.data.onUpdate(par2World, (EntityPlayer) par3Entity);
		}
	}

	public EcCloudSwordData getSwordData(ItemStack var1, World var2)
	{
		int uId = (var1.hasTagCompound()) ? var1.getTagCompound().getInteger("CloudSwordStorage") : 0;
		String var3 = String.format("swords_%s", uId);
		EcCloudSwordData var4 = (EcCloudSwordData) var2.loadItemData(EcCloudSwordData.class, var3);

		if (var4 == null) {
			var4 = new EcCloudSwordData(var3);
			var4.markDirty();
			var2.setItemData(var3, var4);
		}

		return var4;
	}

	public ItemStack makeCloudSwordCore(ItemStack stack)
	{
		ItemStack ChangeSwordCore = new ItemStack(EnchantChanger.ItemCloudSwordCore, 1);
		ChangeSwordCore.setTagCompound(stack.getTagCompound());
		return ChangeSwordCore;
	}

	public void doCastOffSwords(World world, EntityPlayer player)
	{
		if (!world.isRemote) {
			for (int i = 0; i < 5; i++) {
				int j;
				for (j = 0; j < 9; j++) {
					if (player.inventory.getStackInSlot(j) == null) {
						player.inventory.setInventorySlotContents(j, SwordData.getStackInSlot(i));
						break;
					}
				}
				if (j == 9)
					player.dropPlayerItemWithRandomChoice(SwordData.getStackInSlot(i), false);
				SwordData.setInventorySlotContents(i, null);
			}
		}
	}

	public void destroyTheItem(EntityPlayer player, ItemStack orig)
	{
		this.SwordData.setInventorySlotContents(getSlotNum(orig),  null);
		MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, orig));
		this.doCastOffSwords(player.worldObj, player);
		player.inventory.setInventorySlotContents(player.inventory.currentItem,
				this.makeCloudSwordCore(player.getCurrentEquippedItem()));
	}

	@Override
	@SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		List slotItem;
        if(this.SwordData == null)
            this.SwordData = new EcInventoryCloudSword(par1ItemStack, par2EntityPlayer.worldObj);
        int slot = getSlotNum(par1ItemStack);
		if (slot != 5 && this.SwordData.getStackInSlot(slot) != null) {
			slotItem = this.SwordData.getStackInSlot(slot).getTooltip(par2EntityPlayer, par4);
			par3List.addAll(slotItem);
		}
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving)
	{
		par2EntityLiving.hurtResistantTime = 0;
		return super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);
	}

	private int getSlotNum(ItemStack item) {
        if (!item.hasTagCompound()) item.setTagCompound(new NBTTagCompound());
        if (!item.getTagCompound().hasKey("EnchantChanger:CloudSlot")) item.getTagCompound().setInteger("EnchantChanger:CloudSlot", 5);
		return item.getTagCompound().getInteger("EnchantChanger:CloudSlot");
	}

	private void increaseSlotNum(ItemStack item) {
        if (!item.hasTagCompound()) item.setTagCompound(new NBTTagCompound());
        if (!item.getTagCompound().hasKey("EnchantChanger:CloudSlot")) item.getTagCompound().setInteger("EnchantChanger:CloudSlot", 5);

		int newSlot = (item.getTagCompound().getInteger("slot") + 1) % 6;
		item.getTagCompound().setInteger("EnchantChanger:CloudSlot", newSlot);
	}

	public void setSlotNum(ItemStack item, int slotNum) {
        if (!item.hasTagCompound()) item.setTagCompound(new NBTTagCompound());
        item.getTagCompound().setInteger("EnchantChanger:CloudSlot", slotNum % 6);
	}
	//サーバー側で変更したスロットインデックスをクライアントに伝達。表示用。
//	public void readSlotNumData(int data, ItemStack stack)
//	{
//		try {
//			this.setSlotNum(stack, data);
//			;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}