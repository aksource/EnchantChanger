package ak.EnchantChanger;
import java.util.ArrayList;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class EcTileEntityHugeMateria extends TileEntity implements IInventory {
	private static int[][] EnchArray;
	private static ItemStack[] MaterialArray;
	private static ArrayList<Integer> magicArray;
	private ItemStack result = null;
	private ItemStack[] Hugeitemstacks = new ItemStack[5];
	public int MaterializingTime = 0;
	public float angle = 0;

	@Override
	public int getSizeInventory()
	{
		return Hugeitemstacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return Hugeitemstacks[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		Hugeitemstacks[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
	}
	@Override
	public ItemStack decrStackSize(int slot, int amt)
	{
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}


	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this &&
				player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) < 64;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}
	@SideOnly(Side.CLIENT)
	 public int getMaterializingProgressScaled(int par1)
	{
		return this.MaterializingTime * par1/200;
	}
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
		this.Hugeitemstacks = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			int var5 = var4.getByte("Slot") & 255;

			if (var5 >= 0 && var5 < this.Hugeitemstacks.length)
			{
				this.Hugeitemstacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
		this.MaterializingTime = par1NBTTagCompound.getShort("MaterializingTime");
	}
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setShort("MaterializingTime", (short)this.MaterializingTime);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.Hugeitemstacks.length; ++var3)
		{
			if (this.Hugeitemstacks[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.Hugeitemstacks[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);
	}

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeToNBT(nbtTagCompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    @Override
	public void updateEntity()
	{
		//回転させようとしたけど、面倒だった。
//		if(this.angle >360F)
//		{
//			this.angle = 0;
//		}
//		else
//		{
//			this.angle +=1.0F;
//		}
		boolean var2 = false;

		if (!this.worldObj.isRemote)
		{
			if (this.canMake())
			{
				++this.MaterializingTime;

				if (this.MaterializingTime == 200)
				{
					this.MaterializingTime = 0;
					this.makeMateria();
					var2 = true;
				}
			}
			else
			{
				this.MaterializingTime = 0;
			}
		}

		if (var2)
		{
			this.markDirty();
		}
	}
	@Override
	public String getInventoryName() {
		return "container.hugeMateria";
	}
//	public boolean isMaterializing()
//	{
//		return this.MaterializingTime > 0;
//	}
	public boolean canMake()
	{
		ItemStack hMateria = this.getStackInSlot(0);
		ItemStack base = this.getStackInSlot(1);
		ItemStack diamond = this.getStackInSlot(2);
		ItemStack material = this.getStackInSlot(3);
		ItemStack resultItem = this.getStackInSlot(4);
		int lvPlus = 0;
		if(diamond != null && diamond.getItem() == Items.diamond)
			lvPlus = 9;
		if(base == null || !(base .getItem() instanceof EcItemMateria) || resultItem != null || material == null || (diamond!= null && diamond.getItem() != Items.diamond))
			return false;
		else
		{
			if(hMateria != null && hMateria.getItem() instanceof EcItemMasterMateria)
			{
				int dmg = hMateria.getItemDamage();
                return dmg >= 0 && dmg < 6 && makeResult(material, dmg, lvPlus);
			}
			return materiaLvUp(material,lvPlus);
		}
	}
	private boolean makeResult(ItemStack material, int dmg, int lvPlus)
	{
		for(int i= 0;i<EnchArray[dmg].length;i++)
		{
			if(EnchArray[dmg][i] != -1 && isValidItem(MaterialArray[EnchArray[dmg][i]], material))
			{
				if(magicArray.contains(EnchArray[dmg][i]))
					result = new ItemStack(EnchantChanger.itemMateria,1,magicArray.indexOf(EnchArray[dmg][i]) + 1);
				else
					result = new ItemStack(EnchantChanger.itemMateria,1,0);
				if(EnchArray[dmg][i] >= 0 && EnchArray[dmg][i] < Enchantment.enchantmentsList.length){
					int lv;
					if(Enchantment.enchantmentsList[EnchArray[dmg][i]].getMaxLevel() == 1)
						lv = 1;
					else
						lv = 1 + lvPlus;
					EnchantChanger.addEnchantmentToItem(result, Enchantment.enchantmentsList[EnchArray[dmg][i]], lv);
				}
				return true;
			}
		}
		return false;
	}
	private boolean isValidItem(ItemStack item1, ItemStack item2)
	{
		if(item1.getItemDamage() == OreDictionary.WILDCARD_VALUE)
			return item1.getItem() == item2.getItem();
		else
			return item1.isItemEqual(item2);
	}
	private boolean materiaLvUp(ItemStack materia, int lvPlus)
	{
		if(materia.getItem() instanceof EcItemMateria && materia.getItemDamage() > 0 && materia.isItemEnchanted())
		{
			result = materia.copy();
			NBTTagCompound nbt = result.getTagCompound();
			nbt.removeTag("ench");
			EnchantChanger.addEnchantmentToItem(materia, Enchantment.enchantmentsList[EnchantChanger.getMateriaEnchKind(materia)], EnchantChanger.getMateriaEnchLv(materia) + 1 + lvPlus);
			return true;
		}
		else
			return false;
	}
	public boolean makeMateria()
	{
		for(int i=1;i < 4;i++)
		{
			if(this.getStackInSlot(i) != null)
				this.decrStackSize(i, 1);
		}
		this.setInventorySlotContents(4, result);
		return true;
	}
	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}
	static{
		EnchArray = new int[EcItemMasterMateria.MasterMateriaNum - 1][10];
		MaterialArray = new ItemStack[Enchantment.enchantmentsList.length + 3];
		magicArray = new ArrayList<>(EcItemMateria.MagicMateriaNum);
		int i,j;
		for(i = 0;i<EnchArray.length;i++){
			for(j = 0;j<EnchArray[i].length;j++){
				EnchArray[i][j] = -1;
			}
		}
		for(i=0;i<256 + 3;i++){
			MaterialArray[i]=null;
		}
		EnchArray[0][0] = EnchantChanger.EnchantmentMeteoId;
		EnchArray[0][1] = EnchantChanger.EndhantmentHolyId;
		EnchArray[0][2] = EnchantChanger.EnchantmentTelepoId;
		EnchArray[0][3] = EnchantChanger.EnchantmentFloatId;
		EnchArray[0][4] = EnchantChanger.EnchantmentThunderId;
		EnchArray[0][5] = Enchantment.enchantmentsList.length;
		EnchArray[0][6] = Enchantment.enchantmentsList.length + 1;
		EnchArray[0][7] = Enchantment.enchantmentsList.length + 2;
		EnchArray[1][0] = Enchantment.protection.effectId;
		EnchArray[1][1] = Enchantment.fireProtection.effectId;
		EnchArray[1][2] = Enchantment.featherFalling.effectId;
		EnchArray[1][3] = Enchantment.blastProtection.effectId;
		EnchArray[1][4] = Enchantment.projectileProtection.effectId;
		EnchArray[1][5] = Enchantment.thorns.effectId;
		EnchArray[2][0] = Enchantment.respiration.effectId;
		EnchArray[2][1] = Enchantment.aquaAffinity.effectId;
		EnchArray[2][2] = Enchantment.field_151370_z.effectId;
		EnchArray[2][3] = Enchantment.field_151369_A.effectId;
		EnchArray[3][0] = Enchantment.sharpness.effectId;
		EnchArray[3][1] = Enchantment.smite.effectId;
		EnchArray[3][2] = Enchantment.baneOfArthropods.effectId;
		EnchArray[3][3] = Enchantment.knockback.effectId;
		EnchArray[3][4] = Enchantment.fireAspect.effectId;
		EnchArray[3][5] = Enchantment.looting.effectId;
		EnchArray[4][0] = Enchantment.efficiency.effectId;
		EnchArray[4][1] = Enchantment.silkTouch.effectId;
		EnchArray[4][2] = Enchantment.unbreaking.effectId;
		EnchArray[4][3] = Enchantment.fortune.effectId;
		EnchArray[5][0] = Enchantment.power.effectId;
		EnchArray[5][1] = Enchantment.punch.effectId;
		EnchArray[5][2] = Enchantment.flame.effectId;
		EnchArray[5][3] = Enchantment.infinity.effectId;
		MaterialArray[0] = new ItemStack(Items.iron_ingot);
		MaterialArray[1] = new ItemStack(Items.blaze_powder);
		MaterialArray[2] = new ItemStack(Items.feather);
		MaterialArray[3] = new ItemStack(Items.gunpowder);
		MaterialArray[4] = new ItemStack(Items.arrow);
		MaterialArray[5] = new ItemStack(Items.reeds);
		MaterialArray[6] = new ItemStack(Items.golden_pickaxe);
		MaterialArray[7] = new ItemStack(Blocks.cactus);
		MaterialArray[16] = new ItemStack(Items.fire_charge);
		MaterialArray[17] = new ItemStack(Items.flint_and_steel);
		MaterialArray[18] = new ItemStack(Items.spider_eye);
		MaterialArray[19] = new ItemStack(Items.slime_ball);
		MaterialArray[20] = new ItemStack(Items.blaze_rod);
		MaterialArray[21] = new ItemStack(Items.golden_apple, 1, OreDictionary.WILDCARD_VALUE);
		MaterialArray[32] = new ItemStack(Items.golden_pickaxe);
		MaterialArray[33] = new ItemStack(Items.string);
		MaterialArray[34] = new ItemStack(Items.iron_ingot);
		MaterialArray[35] = new ItemStack(Items.golden_apple, 1, OreDictionary.WILDCARD_VALUE);
		MaterialArray[48] = new ItemStack(Items.fire_charge);
		MaterialArray[49] = new ItemStack(Items.slime_ball);
		MaterialArray[50] = new ItemStack(Items.blaze_rod);
		MaterialArray[51] = new ItemStack(Items.bow);
		MaterialArray[61] = new ItemStack(Items.fishing_rod);
		MaterialArray[62] = new ItemStack(Items.carrot_on_a_stick);
		MaterialArray[EnchantChanger.EnchantmentMeteoId] = new ItemStack(Blocks.dragon_egg);
		MaterialArray[EnchantChanger.EndhantmentHolyId] = new ItemStack(Items.golden_apple, 1, 1);
		MaterialArray[EnchantChanger.EnchantmentTelepoId] = new ItemStack(Items.ender_pearl);
		MaterialArray[EnchantChanger.EnchantmentFloatId] = new ItemStack(Items.ender_eye);
		MaterialArray[EnchantChanger.EnchantmentThunderId] = new ItemStack(Blocks.gold_block);
		MaterialArray[Enchantment.enchantmentsList.length] = new ItemStack(Items.milk_bucket);
		MaterialArray[Enchantment.enchantmentsList.length + 1] = new ItemStack(Items.golden_boots);
		MaterialArray[Enchantment.enchantmentsList.length + 2] = new ItemStack(Items.nether_wart);
		magicArray.add(EnchantChanger.EnchantmentMeteoId);
		magicArray.add(EnchantChanger.EndhantmentHolyId);
		magicArray.add(EnchantChanger.EnchantmentTelepoId);
		magicArray.add(EnchantChanger.EnchantmentFloatId);
		magicArray.add(EnchantChanger.EnchantmentThunderId);
		magicArray.add(Enchantment.enchantmentsList.length);
		magicArray.add(Enchantment.enchantmentsList.length + 1);
		magicArray.add(Enchantment.enchantmentsList.length + 2);
	}
}