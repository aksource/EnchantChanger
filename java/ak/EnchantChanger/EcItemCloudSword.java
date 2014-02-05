package ak.EnchantChanger;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EcItemCloudSword extends EcItemSword
{
	private int SlotNum = 5;
	private EcCloudSwordData SwordData;

	public EcItemCloudSword()
	{
		super(ToolMaterial.EMERALD);
		this.setTextureName(EnchantChanger.EcTextureDomain + "CloudSword");
		this.setMaxDamage(ToolMaterial.IRON.getMaxUses() * 14);
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
				//				if (this.SlotNum == 5)
				//					this.SlotNum = 0;
				//				else
				//					this.SlotNum++;
				//				PacketDispatcher.sendPacketToPlayer(Packet_EnchantChanger.getPacketCS(this), (Player) par3EntityPlayer);
				increaseSlotNum(par1ItemStack);
				EnchantChanger.packetPipeline.sendTo(new CloudSwordPacket(this.getSlotNum(par1ItemStack)), (EntityPlayerMP) par3EntityPlayer);
//				PacketDispatcher.sendPacketToPlayer(Packet_EnchantChanger.getPacketCS(this, par1ItemStack),
//						(Player) par3EntityPlayer);
				if (this.SwordData != null && getSlotNum(par1ItemStack) != 5 && this.SwordData
						.getStackInSlot(getSlotNum(par1ItemStack)) != null) {
					par3EntityPlayer.addChatMessage(new ChatComponentTranslation(this.SwordData
							.getStackInSlot(getSlotNum(par1ItemStack))
							.getDisplayName(), new Object[0]));
				}
			}
			return par1ItemStack;
		}
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
		if (!par2World.isRemote && par3Entity instanceof EntityPlayer) {
			this.SwordData = this.getSwordData(par1ItemStack, par2World);
			this.SwordData.onUpdate(par2World, (EntityPlayer) par3Entity);
		}
	}

	private EcCloudSwordData getSwordData(ItemStack var1, World var2)
	{
		int uId = (var1.hasTagCompound()) ? var1.getTagCompound().getInteger("CloudSwordStrage") : 0;
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
		this.SwordData.setInventorySlotContents(getSlotNum(orig), (ItemStack) null);
		MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, orig));
		this.doCastOffSwords(player.worldObj, player);
		player.inventory.setInventorySlotContents(player.inventory.currentItem,
				this.makeCloudSwordCore(player.getCurrentEquippedItem()));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		List slotItem;
		if (getSlotNum(par1ItemStack) != 5
				&& this.getSwordData(par1ItemStack, par2EntityPlayer.worldObj)
						.getStackInSlot(getSlotNum(par1ItemStack)) != null)
		{
			slotItem = this.getSwordData(par1ItemStack, par2EntityPlayer.worldObj)
					.getStackInSlot(getSlotNum(par1ItemStack)).getTooltip(par2EntityPlayer, par4);
			par3List.addAll(slotItem);
		}
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving)
	{
		par2EntityLiving.hurtResistantTime = 0;
		return super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);
	}

	private int getSlotNum(ItemStack item)
	{
		if (item.hasTagCompound()) {
			if (!item.getTagCompound().hasKey("slot")) {
				item.getTagCompound().setInteger("slot", 5);
			}
		} else {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("slot", 5);
			item.setTagCompound(nbt);
		}
		return item.getTagCompound().getInteger("slot");
	}

	private void increaseSlotNum(ItemStack item)
	{
		if (item.hasTagCompound()) {
			if (!item.getTagCompound().hasKey("slot")) {
				item.getTagCompound().setInteger("slot", 5);
			}
		} else {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("slot", 5);
			item.setTagCompound(nbt);
		}
		int newSlot = item.getTagCompound().getInteger("slot") + 1;
		if (newSlot > 5)
			newSlot = 0;
		item.getTagCompound().setInteger("slot", newSlot);
	}

	private void setSlotNum(ItemStack item, int slotNum)
	{
		if (item.hasTagCompound()) {
			if (!item.getTagCompound().hasKey("slot")) {
				item.getTagCompound().setInteger("slot", slotNum);
			}
		} else {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("slot", slotNum);
			item.setTagCompound(nbt);
		}
	}
	//サーバー側で変更したスロットインデックスをクライアントに伝達。表示用。
	public void readSlotNumData(int data, ItemStack stack)
	{
		try {
			this.setSlotNum(stack, data);
			;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}