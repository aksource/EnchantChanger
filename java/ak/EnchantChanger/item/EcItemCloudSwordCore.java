package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;


public class EcItemCloudSwordCore extends EcItemSword {
    public static Entity Attackentity = null;
    private int nowAttackingSwordSlot;

    public EcItemCloudSwordCore(String name) {
        super(ToolMaterial.IRON, name);
    }

    public static ItemStack makeCloudSword(ItemStack stack) {
        ItemStack ChangeSword = new ItemStack(EnchantChanger.itemCloudSword, 1, stack.getItemDamage());
        ChangeSword.setTagCompound(stack.getTagCompound());
        return ChangeSword;
    }

    public static boolean isActive(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        return itemStack.getTagCompound().getBoolean("EnchantChanger|activemode");
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advToolTip) {
        boolean mode = isActive(itemStack);
        String s = mode ? "enchantchanger.cloudswordcore.mode.active" : "enchantchanger.cloudswordcore.mode.inactive";
        list.add(StatCollector.translateToLocal(s));
        super.addInformation(itemStack, player, list, advToolTip);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            if (!world.isRemote) {
                invertActive(par1ItemStack);
                ObfuscationReflectionHelper.setPrivateValue(ItemSword.class, (ItemSword) par1ItemStack.getItem(), (float) ((isActive(par1ItemStack)) ? 7 : 6), 0);
            }
            return par1ItemStack;
        } else {
            if (!isActive(par1ItemStack) && canUnion(player)) {
                IInventory swordData = EcItemCloudSword.getInventoryFromItemStack(par1ItemStack);
                unionSword(player, swordData);
                return makeCloudSword(par1ItemStack);
            } else {
                player.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
                return par1ItemStack;
            }
        }
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
        super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        Attackentity = entity;
        if (isActive(stack)) {
            this.attackTargetEntityWithInventoryItem(entity, player);
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public Item setNoRepair() {
        canRepair = false;
        return this;
    }

    private void invertActive(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        NBTTagCompound nbt = itemStack.getTagCompound();
        nbt.setBoolean("EnchantChanger|activemode", !nbt.getBoolean("EnchantChanger|activemode"));
    }

    public boolean canUnion(EntityPlayer player) {
        int Index = 0;
        int CurrentSlot = player.inventory.currentItem;
        ItemStack sword;
        for (int i = 0; i < 9; i++) {
            if (i == CurrentSlot)
                continue;
            sword = player.inventory.getStackInSlot(i);
            if (sword != null && sword.getItem() instanceof ItemSword) {
                Index++;
            }
        }
        return Index >= 5;
    }

    public void attackTargetEntityWithInventoryItem(Entity par1Entity, EntityPlayer player) {
        ItemStack sword;
        int CurrentSlot = player.inventory.currentItem;
        for (int i = 0; i < 9; i++) {
            if (i == CurrentSlot)
                continue;
            sword = player.inventory.getStackInSlot(i);
            if (sword != null && sword.getItem() instanceof ItemSword && !(sword.getItem() instanceof EcItemSword)) {
                this.nowAttackingSwordSlot = i;
                this.attackTargetEntityWithTheItem(par1Entity, player, sword, true);
            }
        }
//        par1Entity.hurtResistantTime = 20;
    }

    public void destroyTheItem(EntityPlayer player, ItemStack orig) {
        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, orig));
        player.inventory.setInventorySlotContents(this.nowAttackingSwordSlot, null);
    }

    public void unionSword(EntityPlayer player, IInventory swordData) {
        int Index = 0;
        int CurrentSlot = player.inventory.currentItem;
        ItemStack sword;
        for (int i = 0; i < 9; i++) {
            if (i == CurrentSlot)
                continue;
            sword = player.inventory.getStackInSlot(i);
            if (sword != null && sword.getItem() instanceof ItemSword && !(sword.getItem() instanceof EcItemSword) && Index < 5) {
                swordData.setInventorySlotContents(Index, sword);
                player.inventory.setInventorySlotContents(i, null);
                Index++;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IPerspectiveAwareModel getPresentModel(ItemStack itemStack, List<IPerspectiveAwareModel> modelList) {
        return (isActive(itemStack) ? modelList.get(1) : modelList.get(0));
    }
}