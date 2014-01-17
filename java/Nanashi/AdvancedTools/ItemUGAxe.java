package Nanashi.AdvancedTools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public class ItemUGAxe extends ItemUGTool
{
    public static final Set<Block> blocksEffectiveAgainst = new HashSet<Block>();
	private ToolMaterial material;
	protected ItemUGAxe(ToolMaterial var2, float var3)
	{
		super(3, var2, blocksEffectiveAgainst, var3);
		this.material = var2;
	}

	protected ItemUGAxe(ToolMaterial var2)
	{
		super(3, var2, blocksEffectiveAgainst, 1.0F);
	}
//	@Override
//	@SideOnly(Side.CLIENT)
//	public void registerIcons(IIconRegister par1IconRegister)
//	{
//		if(this.getUnlocalizedName().equals("item.UpgradedWoodenAxe")){
//	    	this.itemIcon = par1IconRegister.registerIcon(AdvancedTools.textureDomain + "UGWoodaxe");
//		}else if(this.getUnlocalizedName().equals("item.UpgradedStoneAxe")){
//	    	this.itemIcon = par1IconRegister.registerIcon(AdvancedTools.textureDomain + "UGStoneaxe");
//		}else if(this.getUnlocalizedName().equals("item.UpgradedIronAxe")){
//	    	this.itemIcon = par1IconRegister.registerIcon(AdvancedTools.textureDomain + "UGIronaxe");
//		}else if(this.getUnlocalizedName().equals("item.UpgradedGoldenAxe")){
//	    	this.itemIcon = par1IconRegister.registerIcon(AdvancedTools.textureDomain + "UGGoldaxe");
//		}else if(this.getUnlocalizedName().equals("item.UpgradedDiamondAxe")){
//	    	this.itemIcon = par1IconRegister.registerIcon(AdvancedTools.textureDomain + "UGDiamondaxe");
//		}else if(this.getUnlocalizedName().equals("item.InfinityAxe")){
//	    	this.itemIcon = par1IconRegister.registerIcon(AdvancedTools.textureDomain + "Infinityaxe");
//		}
//	}
	@Override
	public boolean func_150897_b(Block var1)
	{
		return var1.func_149688_o() == Material.field_151575_d;
	}
	@Override
    public float getDigSpeed(ItemStack par1ItemStack, Block par2Block, int meta)
    {
        return par2Block != null && (par2Block.func_149688_o() == Material.field_151575_d || par2Block.func_149688_o() == Material.field_151581_o || par2Block.func_149688_o() == Material.field_151595_p) ? this.efficiencyOnProperMaterial : super.getDigSpeed(par1ItemStack, par2Block, meta);
    }
	public boolean doChainDestraction(Block var1)
	{
		return checkArrays(var1, AdvancedTools.addBlockForAxe) && this.func_150897_b(var1);
	}
	@Override
	protected ArrayList searchAroundBlock(World world, ChunkPosition var1, ChunkPosition var2, ChunkPosition var3, Block var4, ItemStack var5, EntityPlayer var6)
	{
		ArrayList var7 = new ArrayList();
		ChunkPosition[] var8 = new ChunkPosition[17];

		if (var1.field_151328_c < var3.field_151328_c){
			var8[0] = new ChunkPosition(var1.field_151327_b, var1.field_151328_c + 1, var1.field_151329_a);
		}

		if (var1.field_151329_a > var2.field_151329_a){
			var8[1] = new ChunkPosition(var1.field_151327_b, var1.field_151328_c, var1.field_151329_a - 1);
		}

		if (var1.field_151329_a < var3.field_151329_a){
			var8[2] = new ChunkPosition(var1.field_151327_b, var1.field_151328_c, var1.field_151329_a + 1);
		}

		if (var1.field_151327_b > var2.field_151327_b){
			var8[3] = new ChunkPosition(var1.field_151327_b - 1, var1.field_151328_c, var1.field_151329_a);
		}

		if (var1.field_151327_b < var3.field_151327_b){
			var8[4] = new ChunkPosition(var1.field_151327_b + 1, var1.field_151328_c, var1.field_151329_a);
		}

		if (checkArrays(var4, AdvancedTools.addBlockForAxe)){
			if (var1.field_151329_a > var2.field_151329_a && var1.field_151327_b > var2.field_151327_b){
				var8[5] = new ChunkPosition(var1.field_151327_b - 1, var1.field_151328_c, var1.field_151329_a - 1);
			}

			if (var1.field_151329_a < var3.field_151329_a && var1.field_151327_b > var2.field_151327_b){
				var8[6] = new ChunkPosition(var1.field_151327_b - 1, var1.field_151328_c, var1.field_151329_a + 1);
			}

			if (var1.field_151329_a > var2.field_151329_a && var1.field_151327_b < var3.field_151327_b){
				var8[7] = new ChunkPosition(var1.field_151327_b + 1, var1.field_151328_c, var1.field_151329_a - 1);
			}

			if (var1.field_151329_a < var3.field_151329_a && var1.field_151327_b < var3.field_151327_b){
				var8[8] = new ChunkPosition(var1.field_151327_b + 1, var1.field_151328_c, var1.field_151329_a + 1);
			}

			if (var1.field_151328_c < var3.field_151328_c){
				if (var1.field_151329_a > var2.field_151329_a){
					var8[13] = new ChunkPosition(var1.field_151327_b, var1.field_151328_c + 1, var1.field_151329_a - 1);
				}

				if (var1.field_151329_a < var3.field_151329_a){
					var8[14] = new ChunkPosition(var1.field_151327_b, var1.field_151328_c + 1, var1.field_151329_a + 1);
				}

				if (var1.field_151327_b > var2.field_151327_b){
					var8[15] = new ChunkPosition(var1.field_151327_b - 1, var1.field_151328_c + 1, var1.field_151329_a);
				}

				if (var1.field_151327_b < var3.field_151327_b){
					var8[16] = new ChunkPosition(var1.field_151327_b + 1, var1.field_151328_c + 1, var1.field_151329_a);
				}

				if (var1.field_151329_a > var2.field_151329_a && var1.field_151327_b > var2.field_151327_b){
					var8[9] = new ChunkPosition(var1.field_151327_b - 1, var1.field_151328_c + 1, var1.field_151329_a - 1);
				}

				if (var1.field_151329_a < var3.field_151329_a && var1.field_151327_b > var2.field_151327_b){
					var8[10] = new ChunkPosition(var1.field_151327_b - 1, var1.field_151328_c + 1, var1.field_151329_a + 1);
				}

				if (var1.field_151329_a > var2.field_151329_a && var1.field_151327_b < var3.field_151327_b){
					var8[11] = new ChunkPosition(var1.field_151327_b + 1, var1.field_151328_c + 1, var1.field_151329_a - 1);
				}

				if (var1.field_151329_a < var3.field_151329_a && var1.field_151327_b < var3.field_151327_b){
					var8[12] = new ChunkPosition(var1.field_151327_b + 1, var1.field_151328_c + 1, var1.field_151329_a + 1);
				}
			}
		}else if (var1.field_151328_c > var2.field_151328_c){
			var8[5] = new ChunkPosition(var1.field_151327_b, var1.field_151328_c - 1, var1.field_151329_a);
		}

		for (int var9 = 0; var9 < 17; ++var9){
			if (var8[var9] != null && this.destroyBlock(world,var8[var9], var4, var5, var6)){
				var7.add(var8[var9]);
			}
		}

		return var7;
	}
	static{
		blocksEffectiveAgainst.add(Blocks.planks);
		blocksEffectiveAgainst.add(Blocks.bookshelf);
		blocksEffectiveAgainst.add(Blocks.log);
		blocksEffectiveAgainst.add(Blocks.log2);
		blocksEffectiveAgainst.add(Blocks.chest);
		blocksEffectiveAgainst.add(Blocks.double_wooden_slab);
		blocksEffectiveAgainst.add(Blocks.wooden_slab);
		blocksEffectiveAgainst.add(Blocks.pumpkin);
		blocksEffectiveAgainst.add(Blocks.lit_pumpkin);
	}
}
