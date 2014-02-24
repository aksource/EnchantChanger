package ak.EnchantChanger;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



public class EcItemCloudSwordCore extends EcItemSword
{
	public static boolean ActiveMode=false;
	public static Entity Attackentity = null;
	private ItemStack[] swords = new ItemStack[5];
	private EcCloudSwordData SwordData;
	private int nowAttackingSwordSlot;
	@SideOnly(Side.CLIENT)
	private IIcon open;
	@SideOnly(Side.CLIENT)
	private IIcon close;

	public EcItemCloudSwordCore()
	{
		super(ToolMaterial.IRON);
		this.setMaxDamage(ToolMaterial.IRON.getMaxUses() * 14);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon(EnchantChanger.EcTextureDomain + "FirstSword-close");;
		this.open = par1IconRegister.registerIcon(EnchantChanger.EcTextureDomain + "FirstSword-open");
		this.close = par1IconRegister.registerIcon(EnchantChanger.EcTextureDomain + "FirstSword-close");
	}
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if(par3EntityPlayer.isSneaking()){
			if(!par2World.isRemote){
				invertActive(par1ItemStack);
				ObfuscationReflectionHelper.setPrivateValue(ItemSword.class, (ItemSword)par1ItemStack.getItem(), (float)((isActive(par1ItemStack))?7:6), 0);
			}
			return par1ItemStack;
		}else{
			if(!isActive(par1ItemStack) && canUnion2(par3EntityPlayer)){
				UnionSword2(par3EntityPlayer);
				EcCloudSwordData data = this.getSwordData(par1ItemStack, par2World);
				makeSwordData(data, swords);
				return this.makeCloudSword(par1ItemStack);
			}else{
				par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
				return par1ItemStack;
			}
		}
	}
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5){
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
		if(par2World.isRemote){
			if(isActive(par1ItemStack)){
				this.itemIcon = this.open;
			}else{
				this.itemIcon = this.close;
			}
		}
	}
//	@SideOnly(Side.CLIENT)
//	public Icon getIconIndex(ItemStack stack)
//	{
//		return (isActive(stack))? this.open: this.close;
//	}
	public ItemStack makeCloudSword(ItemStack stack)
	{
		ItemStack ChangeSword = new ItemStack(EnchantChanger.ItemCloudSword, 1);
		ChangeSword.setTagCompound(stack.getTagCompound());
		return ChangeSword;
	}
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		Attackentity = entity;
		if(isActive(stack))
			this.attackTargetEntityWithInventoryItem(entity, player);
		return false;
	}
	@Override
	public Item setNoRepair()
	{
		canRepair = false;
		return this;
	}
	@Override
	public void onCreated(ItemStack item, World world, EntityPlayer player)
	{
		int uId = world.getUniqueDataId("CloudSwordStrage");
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("CloudSwordStrage", uId);
		item.setTagCompound(nbt);
	}
	public boolean isActive(ItemStack item)
	{
		if(item.hasTagCompound()){
			return item.getTagCompound().getBoolean("activemode");
		}else{
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setBoolean("activemode", false);
			item.setTagCompound(nbt1);
			return item.getTagCompound().getBoolean("activemode");
		}
	}
	private void invertActive(ItemStack item)
	{
		if(item.hasTagCompound()){
			item.getTagCompound().setBoolean("activemode", !item.getTagCompound().getBoolean("activemode"));
		}else{
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setBoolean("activemode", false);
			item.setTagCompound(nbt1);
			item.getTagCompound().setBoolean("activemode", !item.getTagCompound().getBoolean("activemode"));
		}
	}
	public boolean canUnion2(EntityPlayer player)
	{
		int Index = 0;
		int CurrentSlot = player.inventory.currentItem;
		ItemStack sword;
		for(int i = 0; i<9;i++){
			if(i == CurrentSlot)
				continue;
			sword = player.inventory.getStackInSlot(i);
			if(sword != null && sword.getItem() instanceof ItemSword){
				Index++;
			}
		}
		return Index >= 5;
	}
	public void attackTargetEntityWithInventoryItem(Entity par1Entity, EntityPlayer player)
	{
		ItemStack sword;
		int CurrentSlot = player.inventory.currentItem;
		for(int i=0;i<9;i++){
			if(i == CurrentSlot)
				continue;
			sword = player.inventory.getStackInSlot(i);
			if(sword != null && sword.getItem() instanceof ItemSword && !(sword.getItem() instanceof EcItemSword)){
				this.nowAttackingSwordSlot = i;
				this.attackTargetEntityWithTheItem(par1Entity, player, sword, true);
			}
		}
	}
	public void destroyTheItem(EntityPlayer player, ItemStack orig)
	{
		MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, orig));
		player.inventory.setInventorySlotContents(this.nowAttackingSwordSlot, null);
	}
	
	public void UnionSword2(EntityPlayer player)
	{
		int Index = 0;
		int CurrentSlot = player.inventory.currentItem;
		ItemStack sword;
		for(int i = 0; i<9;i++){
			if(i == CurrentSlot)
				continue;
			sword = player.inventory.getStackInSlot(i);
			if(sword != null && sword.getItem() instanceof ItemSword && !(sword.getItem() instanceof EcItemSword) && Index < 5){
				this.swords[Index] = sword;
				this.swords[Index].setTagCompound(sword.getTagCompound());
				player.inventory.setInventorySlotContents(i, null);
				Index++;
			}
		}
	}
	public void makeSwordData(EcCloudSwordData data, ItemStack[] items)
	{
		data.swords = items;
	}
	public EcCloudSwordData getSwordData(ItemStack var1, World var2)
	{
		int uId = (var1.hasTagCompound())?var1.getTagCompound().getInteger("CloudSwordStrage"):0;
		String var3 = String.format("swords_%s", uId);
		EcCloudSwordData var4 = (EcCloudSwordData)var2.loadItemData(EcCloudSwordData.class, var3);

		if (var4 == null){
			var4 = new EcCloudSwordData(var3);
			var4.markDirty();
			var2.setItemData(var3, var4);
		}

		return var4;
	}
}