package Nanashi.AdvancedTools;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Entity_FireZombie extends EntityZombie
{
	private static final ItemStack defaultHeldItem = new ItemStack(Items.stone_axe, 1);

	public Entity_FireZombie(World var1)
	{
		super(var1);
		//		this.texture = AdvancedTools.mobTexture + "fzombie.png";
		this.isImmuneToFire = true;
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.3D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(6.0D);
//		this.getAttributeMap().func_111150_b(field_110186_bp).setAttribute(this.rand.nextDouble() * 0.10000000149011612D);
	}
	protected void entityInit()
	{
		super.entityInit();
	}
    @SideOnly(Side.CLIENT)

    public boolean canRenderOnFire()
    {
        return !this.isWet();
    }
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		if (this.isWet())
		{
			this.attackEntityFrom(DamageSource.drown, 2);
		}
		else
		{
			this.setFire(1);
		}
	}

	public boolean attackEntityAsMob(Entity var1)
	{
		var1.setFire(5);
		return super.attackEntityAsMob(var1);
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
	 */
	protected void attackEntity(Entity var1, float var2)
	{
		if (this.attackTime <= 0 && var2 < 2.0F && var1.boundingBox.maxY > this.boundingBox.minY && var1.boundingBox.minY < this.boundingBox.maxY)
		{
			var1.setFire(5);
			this.attackTime = 20;
			this.attackEntityAsMob(var1);
		}
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource var1)
	{
		super.onDeath(var1);

		if (var1.getEntity() instanceof EntityPlayer && this.rand.nextFloat() <= 0.05F)
		{
			ItemStack var2 = new ItemStack(Items.stone_axe, 1);

			if (this.rand.nextFloat() <= 0.5F)
			{
				var2.addEnchantment(Enchantment.efficiency, 1 + this.rand.nextInt(4));
			}

			if (this.rand.nextFloat() <= 0.5F)
			{
				var2.addEnchantment(Enchantment.unbreaking, 1);
			}

			this.entityDropItem(var2, 1.0F);
		}
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean var1, int var2)
	{
		super.dropFewItems(var1, var2);

		if (this.rand.nextFloat() <= 0.2F + 0.1F * (float)var2)
		{
			this.func_145779_a(AdvancedTools.RedEnhancer, 1);
		}
	}
	/**
	 * Returns the item that this EntityLiving is holding, if any.
	 */
	@Override
	public ItemStack getHeldItem()
	{
		return defaultHeldItem;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	@Override
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEAD;
	}
}
