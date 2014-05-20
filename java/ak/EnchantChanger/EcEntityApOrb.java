package ak.EnchantChanger;

import ak.MultiToolHolders.InventoryToolHolder;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ak.MultiToolHolders.ItemMultiToolHolder;

public class EcEntityApOrb extends Entity
{
	/**
	 * A constantly increasing value that RenderXPOrb uses to control the colour shifting (Green / yellow)
	 */
	public int apColor;

	/** The age of the XP orb in ticks. */
	public int apOrbAge = 0;
	public int field_35126_c;

	/** The health of this XP orb. */
	private int apOrbHealth = 5;

	/** This is how much XP this orb has. */
	private int apValue;

	public EcEntityApOrb(World par1World, double par2, double par4, double par6, int par8)
	{
		super(par1World);
		this.setSize(0.5F, 0.5F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(par2, par4, par6);
		this.rotationYaw = (float) (Math.random() * 360.0D);
		this.motionX = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
		this.motionY = (double) ((float) (Math.random() * 0.2D) * 2.0F);
		this.motionZ = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
		this.apValue = par8;
	}

	protected boolean canTriggerWalking()
	{
		return false;
	}

	protected void entityInit() {
	}

	public int getBrightnessForRender(float par1)
	{
		float var2 = 0.5F;

		if (var2 < 0.0F)
		{
			var2 = 0.0F;
		}

		if (var2 > 1.0F)
		{
			var2 = 1.0F;
		}

		int var3 = super.getBrightnessForRender(par1);
		int var4 = var3 & 255;
		int var5 = var3 >> 16 & 255;
		var4 += (int) (var2 * 15.0F * 16.0F);

		if (var4 > 240)
		{
			var4 = 240;
		}

		return var4 | var5 << 16;
	}

	public void onUpdate()
	{
		super.onUpdate();

		if (this.field_35126_c > 0)
		{
			--this.field_35126_c;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= 0.029999999329447746D;

		if (this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)).getMaterial() == Material.lava)
		{
			this.motionY = 0.20000000298023224D;
			this.motionX = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.motionZ = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}

		this.func_145771_j(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
		double var1 = 8.0D;
		EntityPlayer var3 = this.worldObj.getClosestPlayerToEntity(this, var1);

		if (var3 != null)
		{
			double var4 = (var3.posX - this.posX) / var1;
			double var6 = (var3.posY + (double) var3.getEyeHeight() - this.posY) / var1;
			double var8 = (var3.posZ - this.posZ) / var1;
			double var10 = Math.sqrt(var4 * var4 + var6 * var6 + var8 * var8);
			double var12 = 1.0D - var10;

			if (var12 > 0.0D)
			{
				var12 *= var12;
				this.motionX += var4 / var10 * var12 * 0.1D;
				this.motionY += var6 / var10 * var12 * 0.1D;
				this.motionZ += var8 / var10 * var12 * 0.1D;
			}
		}

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		float var14 = 0.98F;

		if (this.onGround)
		{
			var14 = this.worldObj.getBlock(MathHelper.floor_double(this.posX),
					MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ)).slipperiness * 0.98F;
		}

		this.motionX *= (double) var14;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= (double) var14;

		if (this.onGround)
		{
			this.motionY *= -0.8999999761581421D;
		}

		++this.apColor;
		++this.apOrbAge;

		if (this.apOrbAge >= 6000)
		{
			this.setDead();
		}
	}

	public boolean handleWaterMovement()
	{
		return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this);
	}

	protected void dealFireDamage(int par1)
	{
		this.attackEntityFrom(DamageSource.inFire, par1);
	}

	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
	{
		this.setBeenAttacked();
		this.apOrbHealth -= par2;

		if (this.apOrbHealth <= 0)
		{
			this.setDead();
		}

		return false;
	}

	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setShort("Health", (short) ((byte) this.apOrbHealth));
		par1NBTTagCompound.setShort("Age", (short) this.apOrbAge);
		par1NBTTagCompound.setShort("Value", (short) this.apValue);
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		this.apOrbHealth = par1NBTTagCompound.getShort("Health") & 255;
		this.apOrbAge = par1NBTTagCompound.getShort("Age");
		this.apValue = par1NBTTagCompound.getShort("Value");
	}

	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
	{
		if (!this.worldObj.isRemote)
		{
			if (this.field_35126_c == 0 && par1EntityPlayer.xpCooldown == 0)
			{
				par1EntityPlayer.xpCooldown = 2;
				this.worldObj.playSoundAtEntity(this, "random.orb", 0.1F,
						0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
				par1EntityPlayer.onItemPickup(this, 1);
				this.addAp(par1EntityPlayer);
				this.setDead();
			}
		}
	}

	private void addAp(EntityPlayer player)
	{
		ItemStack[] items = new ItemStack[13];
		for (int i = 0; i < 9; i++)
		{
			items[i] = player.inventory.getStackInSlot(i);
		}
		for (int i = 0; i < player.inventory.armorInventory.length; i++)
		{
			items[i + 9] = player.inventory.armorInventory[i];
		}

		for (ItemStack itemStack : items)
		{
			if (itemStack != null && itemStack.isItemEnchanted())
			{
				if (EnchantChanger.loadMTH && itemStack.getItem() instanceof ItemMultiToolHolder)
				{
					InventoryToolHolder tools = ((ItemMultiToolHolder) itemStack.getItem()).tools;
					if (tools != null)
					{
						for (int j = 0; j < tools.data.tools.length; j++)
						{
							if (tools.data.tools[j] != null && tools.data.tools[j].isItemEnchanted())
							{
								addApToItem(tools.data.tools[j]);
							}
						}
					}
				}
				else
				{
					addApToItem(itemStack);
				}

			}
		}
	}

	private void addApToItem(ItemStack item)
	{
		NBTTagList enchantList;
		int prevAp;
		short enchantmentId;
		short enchantmentLv;
		int nowAp;
		enchantList = item.getEnchantmentTagList();
		for (int j = 0; j < enchantList.tagCount(); j++) {
			prevAp = enchantList.getCompoundTagAt(j).getInteger("ap");
			enchantmentId =  enchantList.getCompoundTagAt(j).getShort("id");
			enchantmentLv = enchantList.getCompoundTagAt(j).getShort("lvl");
			if (checkLevelLimit(Enchantment.enchantmentsList[enchantmentId], enchantmentLv) || EnchantChanger.magicEnchantment.contains(Integer.valueOf((int) enchantmentId))) {
				continue;
			}
			nowAp = prevAp + this.apValue;
			if (EnchantChanger.isApLimit(enchantmentId, enchantmentLv, nowAp)) {
				nowAp -= EnchantChanger.getApLimit(enchantmentId, enchantmentLv);
				if (enchantmentLv < Short.MAX_VALUE)
					enchantList.getCompoundTagAt(j).setShort("lvl", (short) (enchantmentLv + 1));
			}
			enchantList.getCompoundTagAt(j).setInteger("ap", nowAp);
		}
	}

	private boolean checkLevelLimit(Enchantment ench, int nowLevel)
	{
		if (ench == null) {
			return true;
		} else if (EnchantChanger.levelLimitMap.containsKey(Integer.valueOf(ench.effectId))) {
			if (EnchantChanger.levelLimitMap.get(Integer.valueOf(ench.effectId)) == 0) {
				return ench.getMaxLevel() <= nowLevel;
			} else {
				return EnchantChanger.levelLimitMap.get(Integer.valueOf(ench.effectId)) <= nowLevel;
			}
		} else
            return ench.getMaxLevel() == 1 ||  EnchantChanger.enableLevelCap && ench.getMaxLevel() <= nowLevel;
	}

	public int getTextureByXP()
	{
		return this.apValue >= 2477 ? 10 : (this.apValue >= 1237 ? 9 : (this.apValue >= 617 ? 8
				: (this.apValue >= 307 ? 7 : (this.apValue >= 149 ? 6 : (this.apValue >= 73 ? 5
						: (this.apValue >= 37 ? 4 : (this.apValue >= 17 ? 3 : (this.apValue >= 7 ? 2
								: (this.apValue >= 3 ? 1 : 0)))))))));
	}

	public boolean canAttackWithItem()
	{
		return false;
	}
}
