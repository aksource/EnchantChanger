package ak.EnchantChanger.block;


import ak.EnchantChanger.ExtendedPlayerData;
import ak.EnchantChanger.tileentity.EcTileEntityMaterializer;
import ak.EnchantChanger.EnchantChanger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class EcBlockMaterializer extends BlockContainer
{
    @SideOnly(Side.CLIENT)
	private IIcon top;
    @SideOnly(Side.CLIENT)
	private IIcon side;
    @SideOnly(Side.CLIENT)
	private IIcon bottom;
	public EcBlockMaterializer()
	{
		super(Material.rock);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.top = par1IconRegister.registerIcon(EnchantChanger.EcTextureDomain + "EnchantChanger-top");
		this.side = par1IconRegister.registerIcon(EnchantChanger.EcTextureDomain + "EnchantChanger-side");
		this.bottom = par1IconRegister.registerIcon(EnchantChanger.EcTextureDomain + "EnchantChanger-bottom");
	}
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	@Override
	public IIcon getIcon(int par1, int par2)
	{
		return par1 == 0 ? this.bottom : (par1 == 1 ? this.top : this.side);
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
        if (EnchantChanger.Difficulty < 2 || checkCost(par5EntityPlayer)) {
            par5EntityPlayer.openGui(EnchantChanger.instance, 0, par1World, par2, par3, par4);
        }
        ExtendedPlayerData.get(par5EntityPlayer).setLimitGaugeValue(0);//test
        return true;
	}

    private boolean checkCost(EntityPlayer player) {
        int expLv = player.experienceLevel;
        if (expLv > EnchantChanger.enchantChangerCost) {
            player.addExperienceLevel(-EnchantChanger.enchantChangerCost);
            return true;
        }
        player.addChatComponentMessage(new ChatComponentText(String.format("Need %dLevel to open EnchantChanger", EnchantChanger.enchantChangerCost)));
        return false;
    }

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
	{
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
		par1World.removeTileEntity(par2, par3, par4);
	}
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new EcTileEntityMaterializer();
	}

}