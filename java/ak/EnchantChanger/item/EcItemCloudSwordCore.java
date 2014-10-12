package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.inventory.EcInventoryCloudSword;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

import java.util.List;


public class EcItemCloudSwordCore extends EcItemSword
{
	public static Entity Attackentity = null;
//	private ItemStack[] swords = new ItemStack[5];
	private int nowAttackingSwordSlot;
	@SideOnly(Side.CLIENT)
	private IIcon open;
	@SideOnly(Side.CLIENT)
	private IIcon close;

	public EcItemCloudSwordCore(String name)
	{
		super(ToolMaterial.IRON, name);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon(Constants.EcTextureDomain + "FirstSword-close");
		this.open = par1IconRegister.registerIcon(Constants.EcTextureDomain + "FirstSword-open");
		this.close = par1IconRegister.registerIcon(Constants.EcTextureDomain + "FirstSword-close");
	}

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        boolean mode = isActive(par1ItemStack);
        String s = mode ? "enchantchanger.cloudswordcore.mode.active":"enchantchanger.cloudswordcore.mode.inactive";
        par3List.add(StatCollector.translateToLocal(s));
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
			if(!isActive(par1ItemStack) && canUnion(par3EntityPlayer)){
                EcInventoryCloudSword swordData = EcItemCloudSword.getInventoryFromItemStack(par1ItemStack);
				unionSword(par3EntityPlayer, swordData);
				return makeCloudSword(par1ItemStack);
			}else{
				par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
				return par1ItemStack;
			}
		}
	}
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5){
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
        if (!par2World.isRemote && par3Entity instanceof EntityPlayer) {
            EcInventoryCloudSword swordData = EcItemCloudSword.getInventoryFromItemStack(par1ItemStack);
            if(swordData == null)
                EcItemCloudSword.addInventoryFromItemStack(par1ItemStack, par2World);
            swordData = EcItemCloudSword.getInventoryFromItemStack(par1ItemStack);
            swordData.data.onUpdate(par2World, (EntityPlayer) par3Entity);
        }
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
	public static ItemStack makeCloudSword(ItemStack stack)
	{
		ItemStack ChangeSword = new ItemStack(EnchantChanger.itemCloudSword);
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
		int uId = world.getUniqueDataId("CloudSwordStorage");
        EcItemCloudSword.setCloudSwordStorageUID(item, uId);
        EcItemCloudSword.addInventoryFromItemStack(item, world);
	}
	public boolean isActive(ItemStack itemStack)
	{
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        if (!itemStack.getTagCompound().hasKey("EnchantChanger")) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            itemStack.getTagCompound().setTag("EnchantChanger", nbtTagCompound);
        }
        NBTTagCompound nbt = (NBTTagCompound)itemStack.getTagCompound().getTag("EnchantChanger");
        return nbt.getBoolean("activemode");
	}
	private void invertActive(ItemStack itemStack)
	{
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        if (!itemStack.getTagCompound().hasKey("EnchantChanger")) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            itemStack.getTagCompound().setTag("EnchantChanger", nbtTagCompound);
        }
        NBTTagCompound nbt = (NBTTagCompound)itemStack.getTagCompound().getTag("EnchantChanger");
        nbt.setBoolean("activemode", !nbt.getBoolean("activemode"));
	}
	public boolean canUnion(EntityPlayer player)
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
	
	public void unionSword(EntityPlayer player, EcInventoryCloudSword swordData)
	{
		int Index = 0;
		int CurrentSlot = player.inventory.currentItem;
		ItemStack sword;
		for(int i = 0; i<9;i++){
			if(i == CurrentSlot)
				continue;
			sword = player.inventory.getStackInSlot(i);
			if(sword != null && sword.getItem() instanceof ItemSword && !(sword.getItem() instanceof EcItemSword) && Index < 5){
                swordData.setInventorySlotContents(Index, sword);
				player.inventory.setInventorySlotContents(i, null);
				Index++;
			}
		}
	}
/*	public void makeSwordData(EcCloudSwordData data, ItemStack[] items)
	{
		data.swords = items;
	}*/
/*	public EcCloudSwordData getSwordData(ItemStack var1, World var2)
	{
		int uId = (var1.hasTagCompound())?var1.getTagCompound().getInteger("CloudSwordStorage"):0;
		String var3 = String.format("swords_%s", uId);
		EcCloudSwordData var4 = (EcCloudSwordData)var2.loadItemData(EcCloudSwordData.class, var3);

		if (var4 == null){
			var4 = new EcCloudSwordData(var3);
			var4.markDirty();
			var2.setItemData(var3, var4);
		}

		return var4;
	}*/
}