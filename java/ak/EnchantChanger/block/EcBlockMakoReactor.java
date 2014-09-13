package ak.EnchantChanger.block;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.tileentity.EcTileEntityMakoReactor;
import ak.EnchantChanger.tileentity.EcTileMultiPass;
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
    public static String[][] baseBlocks = new String[][]{
            {GameRegistry.findUniqueIdentifierFor(Blocks.iron_block).modId, GameRegistry.findUniqueIdentifierFor(Blocks.iron_block).name},
            {GameRegistry.findUniqueIdentifierFor(Blocks.gold_block).modId, GameRegistry.findUniqueIdentifierFor(Blocks.gold_block).name}};
    @SideOnly(Side.CLIENT)
    private IIcon iconFront;

    public EcBlockMakoReactor() {
        super(Material.iron);
    }

    @Override
    public IIcon getIcon(int side  , int meta) {
        if (meta == 0) {
            return (side == 2) ? this.iconFront : this.blockIcon;
        }
        return this.blockIcon;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        if (world.getBlockMetadata(x, y, z) == 0) {
            EcTileEntityMakoReactor tile = (EcTileEntityMakoReactor) world.getTileEntity(x, y, z);
            if (side == tile.face) {
                return this.iconFront;
            }
            return this.blockIcon;
        }
        return this.blockIcon;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        super.registerBlockIcons(iconRegister);
        this.iconFront = iconRegister.registerIcon(EnchantChanger.EcTextureDomain + "makoreactor-front");
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase setter, ItemStack item) {
        super.onBlockPlacedBy(world, x, y, z, setter, item);
        int l = MathHelper.floor_double((double) (setter.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        EcTileMultiPass tile = (EcTileMultiPass) world.getTileEntity(x, y, z);
        if (tile instanceof EcTileEntityMakoReactor) {
            ((EcTileEntityMakoReactor)tile).face = (byte) sides[l];
        }
        if (item.hasTagCompound() && item.getTagCompound().hasKey("EnchantChanger|baseBlock")) {
            tile.baseBlock = item.getTagCompound().getString("EnchantChanger|baseBlock");
        }
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int meta) {
        return (meta == 0) ? new EcTileEntityMakoReactor(): super.createNewTileEntity(var1, meta);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item itemblock, CreativeTabs tab, List list) {
        ItemStack makoReactorController;
        for (String[] baseUID:baseBlocks) {
            makoReactorController = new ItemStack(this, 1, 0);
            makoReactorController.setTagCompound(new NBTTagCompound());
            String baseBlockName = String.format("%s:%s", baseUID[0], baseUID[1]);
            makoReactorController.getTagCompound().setString("EnchantChanger|baseBlock", baseBlockName);;
            list.add(makoReactorController);
        }
    }
}
