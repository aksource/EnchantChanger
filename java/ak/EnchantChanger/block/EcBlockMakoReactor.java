package ak.EnchantChanger.block;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.block.property.UnlistedPropertyInteger;
import ak.EnchantChanger.tileentity.EcTileEntityMakoReactor;
import ak.EnchantChanger.tileentity.EcTileMultiPass;
import com.google.common.base.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * 魔晄炉のブロッククラス
 * Created by A.K. on 14/03/08.
 */
public class EcBlockMakoReactor extends EcBlockMultiPass {
    public static final IUnlistedProperty PROPERTY_POS_X = new UnlistedPropertyInteger("posX");
    public static final IUnlistedProperty PROPERTY_POS_Y = new UnlistedPropertyInteger("posY");
    public static final IUnlistedProperty PROPERTY_POS_Z = new UnlistedPropertyInteger("posZ");
    private static int[] sides = new int[]{2, 5, 3, 4};
    public static List<String> baseBlocksOreName = new ArrayList<>();
//    @SideOnly(Side.CLIENT)
//    private IIcon iconFront;

    static {
        baseBlocksOreName.add("blockIron");
        baseBlocksOreName.add("blockGold");
        baseBlocksOreName.add("blockCopper");
        baseBlocksOreName.add("blockTin");
        baseBlocksOreName.add("blockBronze");
        baseBlocksOreName.add("blockBrass");
        baseBlocksOreName.add("blockSteel");
        baseBlocksOreName.add("blockUranium");
        baseBlocksOreName.add("blockOsmium");
        baseBlocksOreName.add("blockIridium");
        baseBlocksOreName.add("blockSilver");
        baseBlocksOreName.add("blockZinc");
        baseBlocksOreName.add("blockTungsten");
        baseBlocksOreName.add("blockLead");
        baseBlocksOreName.add("blockAluminium");
        baseBlocksOreName.add("blockNickel");
        baseBlocksOreName.add("blockPlatinum");
        baseBlocksOreName.add("blockMithril");
        baseBlocksOreName.add("blockLumium");
        baseBlocksOreName.add("blockInvar");
        baseBlocksOreName.add("blockElectrum");
        baseBlocksOreName.add("blockEnderium");
        baseBlocksOreName.add("blockNickel");
    }

    public EcBlockMakoReactor() {
        super(Material.iron);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return super.getMetaFromState(state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        return extendedBlockState.withProperty(PROPERTY_POS_X, pos.getX()).withProperty(PROPERTY_POS_Y, pos.getY()).withProperty(PROPERTY_POS_Z, pos.getZ());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState state, EntityLivingBase setter, ItemStack item) {
        super.onBlockPlacedBy(world, blockPos, state, setter, item);
        int l = MathHelper.floor_double((double) (setter.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        EcTileMultiPass tile = (EcTileMultiPass) world.getTileEntity(blockPos);
        if (tile instanceof EcTileEntityMakoReactor) {
            ((EcTileEntityMakoReactor) tile).setFace((byte) sides[l]);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        EcTileEntityMakoReactor tile = (EcTileEntityMakoReactor) world.getTileEntity(blockPos);
        if (tile != null && tile.isActivated()) {
            player.openGui(EnchantChanger.instance, Constants.GUI_ID_MAKO_REACTOR, world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int meta) {
        return (meta == 0) ? new EcTileEntityMakoReactor() : super.createNewTileEntity(var1, meta);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item itemblock, CreativeTabs tab, List list) {
        ItemStack makoReactorController;
        List<ItemStack> ores;
        Block block;
        int blockMeta;
        for (String baseOreName : baseBlocksOreName) {
            ores = OreDictionary.getOres(baseOreName);
            for (ItemStack itemStack : ores) {
                makoReactorController = new ItemStack(this, 1, 0);
                makoReactorController.setTagCompound(new NBTTagCompound());
                String uidStr = Optional.fromNullable(itemStack.getItem().getRegistryName()).or("dummy:dummy");
                blockMeta = (itemStack.getItem()).getMetadata(itemStack.getItemDamage());
                makoReactorController.getTagCompound().setString("EnchantChanger|baseBlock", uidStr);
                makoReactorController.getTagCompound().setInteger("EnchantChanger|baseMeta", blockMeta);
                list.add(makoReactorController);
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState state) {
        EcTileEntityMakoReactor tile = (EcTileEntityMakoReactor) world.getTileEntity(blockPos);
        if (tile != null) {
            for (int i1 = 0; i1 < tile.getSizeInventory(); ++i1) {
                ItemStack itemstack = tile.getStackInSlot(i1);

                if (itemstack != null) {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0) {
                        int j1 = world.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize) {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, blockPos.getX() + f, blockPos.getY() + f1, blockPos.getZ() + f2, new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) world.rand.nextGaussian() * f3);
                        entityitem.motionY = (double) ((float) world.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double) ((float) world.rand.nextGaussian() * f3);
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }
        }
        super.breakBlock(world, blockPos, state);
    }

    @Override
    protected BlockState createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{}, new IUnlistedProperty[]{PROPERTY_POS_X, PROPERTY_POS_Y, PROPERTY_POS_Z});
    }
}
