package StackSizeChange;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;

public class ItemBucketStack extends ItemBucket
{
	private Block isFull;

	public ItemBucketStack(Block block)
	{
		super(block);
		this.isFull = block;
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		float var4 = 1.0F;
		double var5 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double)var4;
		double var7 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double)var4 + 1.62D - (double)par3EntityPlayer.yOffset;
		double var9 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double)var4;
		boolean var11 = this.isFull == Blocks.air;
		MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, var11);

		if (var12 == null)
		{
			return par1ItemStack;
		}
		else
		{
			FillBucketEvent event = new FillBucketEvent(par3EntityPlayer, par1ItemStack, par2World, var12);
			if (MinecraftForge.EVENT_BUS.post(event))
			{
				return par1ItemStack;
			}

			if (event.getResult() == Result.ALLOW)
			{
				return StackSizeChange.addropItems(par1ItemStack, par3EntityPlayer, event.result);
			}

			if (var12.typeOfHit == MovingObjectType.BLOCK)
			{
				int var13 = var12.blockX;
				int var14 = var12.blockY;
				int var15 = var12.blockZ;

				if (!par2World.canMineBlock(par3EntityPlayer, var13, var14, var15))
				{
					return par1ItemStack;
				}

				if (this.isFull == Blocks.air)
				{
					if (!par3EntityPlayer.canPlayerEdit(var13, var14, var15, var12.sideHit, par1ItemStack))
					{
						return par1ItemStack;
					}

					if (par2World.func_147439_a(var13, var14, var15).func_149688_o() == Material.field_151586_h && par2World.getBlockMetadata(var13, var14, var15) == 0)
					{
//						par2World.setBlockWithNotify(var13, var14, var15, 0);
						par2World.func_147468_f(var13, var14, var15);
						return StackSizeChange.addropItems(par1ItemStack, par3EntityPlayer, new ItemStack(StackSizeChange.bucketWater));
					}

					if (par2World.func_147439_a(var13, var14, var15).func_149688_o() == Material.field_151587_i && par2World.getBlockMetadata(var13, var14, var15) == 0)
					{
//						par2World.setBlockWithNotify(var13, var14, var15, 0);
						par2World.func_147468_f(var13, var14, var15);
						return StackSizeChange.addropItems(par1ItemStack, par3EntityPlayer, new ItemStack(StackSizeChange.bucketLava));
					}
				}
				else
				{
//					if (this.isFull < 0)
//					{
//						return StackSizeChange.addropItems(par1ItemStack, par3EntityPlayer, new ItemStack(StackSizeChange.bucketEmpty));
//					}

					if (var12.sideHit == 0)
					{
						--var14;
					}

					if (var12.sideHit == 1)
					{
						++var14;
					}

					if (var12.sideHit == 2)
					{
						--var15;
					}

					if (var12.sideHit == 3)
					{
						++var15;
					}

					if (var12.sideHit == 4)
					{
						--var13;
					}

					if (var12.sideHit == 5)
					{
						++var13;
					}

					if (!par3EntityPlayer.canPlayerEdit(var13, var14, var15, var12.sideHit, par1ItemStack))
					{
						return par1ItemStack;
					}

					if (this.tryPlaceContainedLiquid(par2World, var5, var7, var9, var13, var14, var15))
					{
						return StackSizeChange.addropItems(par1ItemStack, par3EntityPlayer, new ItemStack(StackSizeChange.bucketEmpty));
					}
				}
			}

			return par1ItemStack;
		}
	}

	public boolean tryPlaceContainedLiquid(World par1World, double par2, double par4, double par6, int par8, int par9, int par10)
	{
		if (this.isFull == Blocks.air)
		{
			return false;
		}
		else if (!par1World.func_147437_c(par8, par9, par10) && par1World.func_147439_a(par8, par9, par10).func_149688_o().isSolid())
		{
			return false;
		}
		else
		{
			if (par1World.provider.isHellWorld && this.isFull == Blocks.water && StackSizeChange.isNetherSetWater)
			{
				par1World.playSoundEffect(par2 + 0.5D, par4 + 0.5D, par6 + 0.5D, "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);

				for (int var11 = 0; var11 < 8; ++var11)
				{
					par1World.spawnParticle("largesmoke", (double)par8 + Math.random(), (double)par9 + Math.random(), (double)par10 + Math.random(), 0.0D, 0.0D, 0.0D);
				}
			}
			else
			{
				par1World.func_147449_b(par8, par9, par10, this.isFull);
			}

			return true;
		}
	}
	@Override
    public IIcon getIconFromDamage(int par1)
    {
		if(!StackSizeChange.BucketReplace && StackSizeChange.addStackableBucket)
		{
			if(isFull == Blocks.water)
			{
				return Items.water_bucket.getIconFromDamage(par1);
			}
			else if(isFull == Blocks.lava)
			{
				return Items.lava_bucket.getIconFromDamage(par1);
			}
			else
			{
				return Items.bucket.getIconFromDamage(par1);
			}
		}
		else
		{
	        return super.getIconFromDamage(par1);
		}
    }

}
