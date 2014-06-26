package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.inventory.EcCloudSwordData;
import ak.EnchantChanger.inventory.EcInventoryCloudSword;
import ak.EnchantChanger.network.MessageCloudSword;
import ak.EnchantChanger.network.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

import java.util.HashMap;
import java.util.Map;

public class EcItemCloudSword extends EcItemSword
{
    @Deprecated
	private EcInventoryCloudSword SwordData = null;
    private static final Map<Integer, EcInventoryCloudSword> swordInventoryMap = new HashMap<>();

	public EcItemCloudSword()
	{
		super(ToolMaterial.EMERALD);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
        int slot = getSlotNumFromItemStack(stack);
        EcInventoryCloudSword swordData = getInventoryFromItemStack(stack);
		if (slot == 5 || swordData == null)
			return false;
		else if (swordData.getStackInSlot(slot) != null) {
			this.attackTargetEntityWithTheItem(entity, player, swordData.getStackInSlot(slot), false);
			return true;
		} else
			return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (par3EntityPlayer.isSneaking()) {
			this.doCastOffSwords(par1ItemStack, par2World, par3EntityPlayer);
			return this.makeCloudSwordCore(par1ItemStack);
		} else {
			par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
			if (!par2World.isRemote) {
    			increaseSlotNum(par1ItemStack);
                int slot = getSlotNumFromItemStack(par1ItemStack);
//                EcInventoryCloudSword swordData = getInventoryFromItemStack(par1ItemStack);
                PacketHandler.INSTANCE.sendTo(new MessageCloudSword((byte)slot), (EntityPlayerMP) par3EntityPlayer);
//				if (swordData != null && getSlotNumFromItemStack(par1ItemStack) != 5 && swordData
//						.getStackInSlot(slot) != null) {
//					par3EntityPlayer.addChatMessage(new ChatComponentText(swordData
//							.getStackInSlot(slot)
//							.getDisplayName()));
//				}
			}
			return par1ItemStack;
		}
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
		if (!par2World.isRemote && par3Entity instanceof EntityPlayer) {
            EcInventoryCloudSword swordData = getInventoryFromItemStack(par1ItemStack);
            if(swordData == null)
                addInventoryFromItemStack(par1ItemStack, par2World);
                swordData = getInventoryFromItemStack(par1ItemStack);
            swordData.data.onUpdate(par2World, (EntityPlayer) par3Entity);
		}
	}

    public static int getSlotNumFromItemStack(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        if (!itemStack.getTagCompound().hasKey("EnchantChanger")) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            itemStack.getTagCompound().setTag("EnchantChanger", nbtTagCompound);
        }
        NBTTagCompound nbt = (NBTTagCompound)itemStack.getTagCompound().getTag("EnchantChanger");
        return nbt.getInteger("slot");
    }

    public static void setSlotNumToItemStack(ItemStack itemStack, int slotNum) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        if (!itemStack.getTagCompound().hasKey("EnchantChanger")) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            itemStack.getTagCompound().setTag("EnchantChanger", nbtTagCompound);
        }
        NBTTagCompound nbt = (NBTTagCompound)itemStack.getTagCompound().getTag("EnchantChanger");
        nbt.setInteger("slot", slotNum);
    }

    public static EcInventoryCloudSword getInventoryFromItemStack(ItemStack itemStack) {
        int uid = getCloudSwordStorageUID(itemStack);
        return swordInventoryMap.get(uid);
    }

    public static void addInventoryFromItemStack(ItemStack itemStack, World world) {
        int uid = getCloudSwordStorageUID(itemStack);
        swordInventoryMap.put(uid, new EcInventoryCloudSword(itemStack, world));
    }

	public static EcCloudSwordData getSwordData(ItemStack var1, World var2)
	{
		int uId = getCloudSwordStorageUID(var1);
		String var3 = String.format("swords_%s", uId);
		EcCloudSwordData var4 = (EcCloudSwordData) var2.loadItemData(EcCloudSwordData.class, var3);

		if (var4 == null) {
			var4 = new EcCloudSwordData(var3);
			var4.markDirty();
			var2.setItemData(var3, var4);
		}

		return var4;
	}

    public static int getCloudSwordStorageUID(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        if (!itemStack.getTagCompound().hasKey("EnchantChanger")) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            itemStack.getTagCompound().setTag("EnchantChanger", nbtTagCompound);
        }
        NBTTagCompound nbt = (NBTTagCompound)itemStack.getTagCompound().getTag("EnchantChanger");
        return nbt.getInteger("CloudSwordStorage");
    }

    public static void setCloudSwordStorageUID(ItemStack itemStack, int uid) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        if (!itemStack.getTagCompound().hasKey("EnchantChanger")) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            itemStack.getTagCompound().setTag("EnchantChanger", nbtTagCompound);
        }
        NBTTagCompound nbt = (NBTTagCompound)itemStack.getTagCompound().getTag("EnchantChanger");
        nbt.setInteger("CloudSwordStorage", uid);
    }

	public ItemStack makeCloudSwordCore(ItemStack stack)
	{
		ItemStack ChangeSwordCore = new ItemStack(EnchantChanger.ItemCloudSwordCore);
		ChangeSwordCore.setTagCompound(stack.getTagCompound());
		return ChangeSwordCore;
	}

	public void doCastOffSwords(ItemStack ItemStack, World world, EntityPlayer player)
	{
		if (!world.isRemote) {
            EcInventoryCloudSword swordData = getInventoryFromItemStack(ItemStack);
			for (int i = 0; i < 5; i++) {
				int j;
				for (j = 0; j < 9; j++) {
					if (player.inventory.getStackInSlot(j) == null) {
						player.inventory.setInventorySlotContents(j, swordData.getStackInSlot(i));
						break;
					}
				}
				if (j == 9)
					player.dropPlayerItemWithRandomChoice(swordData.getStackInSlot(i), false);
				swordData.setInventorySlotContents(i, null);
			}
		}
	}

	public void destroyTheItem(EntityPlayer player, ItemStack orig)
	{
        EcInventoryCloudSword swordData = getInventoryFromItemStack(orig);
		swordData.setInventorySlotContents(getSlotNumFromItemStack(orig),  null);
		MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, orig));
		this.doCastOffSwords(orig, player.worldObj, player);
		player.inventory.setInventorySlotContents(player.inventory.currentItem,
				this.makeCloudSwordCore(player.getCurrentEquippedItem()));
	}

/*	@Override
	@SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		List slotItem;
        EcInventoryCloudSword swordData = getInventoryFromItemStack(par1ItemStack);
        int slot = getSlotNumFromItemStack(par1ItemStack);
		if (slot != 5 && swordData != null && swordData.getStackInSlot(slot) != null) {
			slotItem = swordData.getStackInSlot(slot).getTooltip(par2EntityPlayer, par4);
			par3List.addAll(slotItem);
		}
	}*/

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving)
	{
		par2EntityLiving.hurtResistantTime = 0;
		return super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);
	}

	private void increaseSlotNum(ItemStack item) {
		int newSlot = (getSlotNumFromItemStack(item) + 1) % 6;
		setSlotNumToItemStack(item, newSlot);
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