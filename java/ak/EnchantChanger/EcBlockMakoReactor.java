package ak.EnchantChanger;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by A.K. on 14/03/08.
 */
public class EcBlockMakoReactor extends EcBlockMultiPass{
    private static int[] sides = new int[]{2,5,3,4};
    private static String[] baseBlocks = new String[]{GameRegistry.findUniqueIdentifierFor(Blocks.iron_block).toString(),GameRegistry.findUniqueIdentifierFor(Blocks.gold_block).toString()};
    @SideOnly(Side.CLIENT)
    private IIcon iconFront;

    public EcBlockMakoReactor() {
        super(Material.iron);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return super.getIcon(side, meta);
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        if (world.getBlockMetadata(x, y, z) == 0) {
            EcTileEntityMakoReactor tile = (EcTileEntityMakoReactor) world.getTileEntity(x, y, z);
            if (side == tile.face) {
                return this.iconFront;
            } else return this.blockIcon;
        } else return super.getIcon(world, x, y, z, side);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.iconFront = iconRegister.registerIcon(EnchantChanger.EcTextureDomain + "makoreactor-front");
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase setter, ItemStack item) {
        super.onBlockPlacedBy(world, x, y, z, setter, item);
        int l = MathHelper.floor_double((double) (setter.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        EcTileEntityMakoReactor tile = (EcTileEntityMakoReactor) world.getTileEntity(x, y, z);
        tile.face = (byte) sides[l];
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int meta) {
        if (meta == 0) {
            return new EcTileEntityMakoReactor();
        } else return null;
    }

    @Override
    public void getSubBlocks(Item itemblock, CreativeTabs tab, List list) {
        ItemStack makoReactorController = new ItemStack(this, 1, 0);
        NBTTagCompound nbt = new NBTTagCompound();
        for (String base:baseBlocks) {
            nbt.setString("baseBlock", base);
            makoReactorController.setTagCompound(nbt);
            list.add(makoReactorController);
        }
    }
}
