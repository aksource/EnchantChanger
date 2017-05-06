package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.inventory.EcInventoryCloudSword;
import ak.EnchantChanger.network.MessageCloudSword;
import ak.EnchantChanger.network.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class EcItemCloudSword extends EcItemSword {
//    @Deprecated
//	private EcInventoryCloudSword SwordData = null;
//    private static final Map<Integer, EcInventoryCloudSword> swordInventoryMap = new HashMap<>();

    public EcItemCloudSword(String name) {
        super(ToolMaterial.EMERALD, name);
    }

    public static int getSlotNumFromItemStack(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        return itemStack.getTagCompound().getInteger("EnchantChanger|slot");
//        if (!itemStack.getTagCompound().hasKey("EnchantChanger")) {
//            NBTTagCompound nbtTagCompound = new NBTTagCompound();
//            itemStack.getTagCompound().setTag("EnchantChanger", nbtTagCompound);
//        }
//        NBTTagCompound nbt = (NBTTagCompound)itemStack.getTagCompound().getTag("EnchantChanger");
//        return nbt.getInteger("slot");
    }

    public static void setSlotNumToItemStack(ItemStack itemStack, int slotNum) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        itemStack.getTagCompound().setInteger("EnchantChanger|slot", slotNum);
//        if (!itemStack.getTagCompound().hasKey("EnchantChanger")) {
//            NBTTagCompound nbtTagCompound = new NBTTagCompound();
//            itemStack.getTagCompound().setTag("EnchantChanger", nbtTagCompound);
//        }
//        NBTTagCompound nbt = (NBTTagCompound)itemStack.getTagCompound().getTag("EnchantChanger");
//        nbt.setInteger("slot", slotNum);
    }

    public static IInventory getInventoryFromItemStack(ItemStack itemStack) {
//        int uid = getCloudSwordStorageUID(itemStack);
//        return swordInventoryMap.get(uid);
        return new EcInventoryCloudSword(itemStack);
    }

//    @Override
//	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
//		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
//		if (!par2World.isRemote && par3Entity instanceof EntityPlayer) {
//            EcInventoryCloudSword swordData = getInventoryFromItemStack(par1ItemStack);
//            if(swordData == null)
//                addInventoryFromItemStack(par1ItemStack, par2World);
//                swordData = getInventoryFromItemStack(par1ItemStack);
//            swordData.data.onUpdate(par2World, (EntityPlayer) par3Entity);
//		}
//	}

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        int slot = getSlotNumFromItemStack(stack);
        IInventory swordData = getInventoryFromItemStack(stack);
        if (slot == 5 || swordData == null)
            return super.onLeftClickEntity(stack, player, entity);
        if (swordData.getStackInSlot(slot) != null) {
            this.attackTargetEntityWithTheItem(entity, player, swordData.getStackInSlot(slot), false);
            swordData.markDirty();
            return true;
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            this.doCastOffSwords(par1ItemStack, world, player);
            return this.makeCloudSwordCore(par1ItemStack);
        } else {
            return super.onItemRightClick(par1ItemStack, world, player);
        }
    }

    @Override
    public void doCtrlKeyAction(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        increaseSlotNum(itemStack);
        int slot = getSlotNumFromItemStack(itemStack);
        PacketHandler.INSTANCE.sendTo(new MessageCloudSword((byte) slot), (EntityPlayerMP) entityPlayer);
    }

//    public static void addInventoryFromItemStack(ItemStack itemStack, World world) {
//        int uid = getCloudSwordStorageUID(itemStack);
//        swordInventoryMap.put(uid, new EcInventoryCloudSword(itemStack, world));
//    }

//	public static EcCloudSwordData getSwordData(ItemStack var1, World var2)
//	{
//		int uId = getCloudSwordStorageUID(var1);
//		String var3 = String.format("swords_%s", uId);
//		EcCloudSwordData var4 = (EcCloudSwordData) var2.loadItemData(EcCloudSwordData.class, var3);
//
//		if (var4 == null) {
//			var4 = new EcCloudSwordData(var3);
//			var4.markDirty();
//			var2.setItemData(var3, var4);
//		}
//
//		return var4;
//	}

//    public static int getCloudSwordStorageUID(ItemStack itemStack) {
//        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
//        if (!itemStack.getTagCompound().hasKey("EnchantChanger")) {
//            NBTTagCompound nbtTagCompound = new NBTTagCompound();
//            itemStack.getTagCompound().setTag("EnchantChanger", nbtTagCompound);
//        }
//        NBTTagCompound nbt = (NBTTagCompound)itemStack.getTagCompound().getTag("EnchantChanger");
//        return nbt.getInteger("CloudSwordStorage");
//    }

//    public static void setCloudSwordStorageUID(ItemStack itemStack, int uid) {
//        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
//        if (!itemStack.getTagCompound().hasKey("EnchantChanger")) {
//            NBTTagCompound nbtTagCompound = new NBTTagCompound();
//            itemStack.getTagCompound().setTag("EnchantChanger", nbtTagCompound);
//        }
//        NBTTagCompound nbt = (NBTTagCompound)itemStack.getTagCompound().getTag("EnchantChanger");
//        nbt.setInteger("CloudSwordStorage", uid);
//    }

    public ItemStack makeCloudSwordCore(ItemStack stack) {
        ItemStack ChangeSwordCore = new ItemStack(EnchantChanger.ItemCloudSwordCore, 1, stack.getItemDamage());
        ChangeSwordCore.setTagCompound(stack.getTagCompound());
        return ChangeSwordCore;
    }

    public void doCastOffSwords(ItemStack ItemStack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            IInventory swordData = getInventoryFromItemStack(ItemStack);
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

    public void destroyTheItem(EntityPlayer player, ItemStack orig) {
        IInventory swordData = getInventoryFromItemStack(orig);
        swordData.setInventorySlotContents(getSlotNumFromItemStack(orig), null);
        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, orig));
        this.doCastOffSwords(orig, player.worldObj, player);
        player.inventory.setInventorySlotContents(player.inventory.currentItem,
                this.makeCloudSwordCore(player.getCurrentEquippedItem()));
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving) {
        par2EntityLiving.hurtResistantTime = 0;
        return super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);
    }

    private void increaseSlotNum(ItemStack item) {
        int newSlot = (getSlotNumFromItemStack(item) + 1) % 6;
        setSlotNumToItemStack(item, newSlot);
    }
}