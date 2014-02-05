package LilypadBreed;

import net.minecraft.block.BlockLilyPad;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class BlockLilypadBreed extends BlockLilyPad
{
	public BlockLilypadBreed()
	{
		super();
	}
	@Override
	public int getRenderType()
	{
		return 23;
	}
	@Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		if(!par1World.isRemote 
			&& par5EntityPlayer.getCurrentEquippedItem() != null
			&& par5EntityPlayer.getCurrentEquippedItem().getItem() == Items.dye
			&& par5EntityPlayer.getCurrentEquippedItem().getItemDamage() == 15)
		{
			par5EntityPlayer.getCurrentEquippedItem().stackSize--;
			for(int x = -2 + par2; x < 3 + par2; x++){
			for(int z = -2 + par4; z < 3 + par4; z++){
				if(par1World.getBlock(x, par3 - 1 , z) == Blocks.water
					&& par1World.getBlock(x, par3, z) == Blocks.air)
				{
					if(x == par2 && z == par4
						|| par1World.rand.nextInt(100) < LilypadBreed.LilypadRate)
					{
						par1World.setBlock(x, par3, z, this);
					}
				}
			}}
			return true;
		}
		return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
	}
}
