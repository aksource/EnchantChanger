package ak.EnchantChanger.block;

import ak.EnchantChanger.Client.ClientProxy;
import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.tileentity.EcTileEntityMakoReactor;
import ak.EnchantChanger.tileentity.EcTileMultiPass;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A.K. on 14/03/08.
 */
public class EcBlockMakoReactor extends EcBlockMultiPass {
    public static List<String> baseBlocksOreName = new ArrayList<>();
    private static int[] sides = new int[]{2, 5, 3, 4};

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

    @SideOnly(Side.CLIENT)
    private IIcon iconFront;

    public EcBlockMakoReactor() {
        super(Material.iron);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (meta == 0) {
            return (side == 4) ? this.iconFront : this.blockIcon;
        }
        return this.blockIcon;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        if (ClientProxy.customRenderPass == 1 && world.getBlockMetadata(x, y, z) == 0) {
            EcTileEntityMakoReactor tile = (EcTileEntityMakoReactor) world.getTileEntity(x, y, z);
            if (side == tile.face) {
                return this.iconFront;
            }
            return this.blockIcon;
        }
        return super.getIcon(world, x, y, z, side);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        super.registerBlockIcons(iconRegister);
        this.iconFront = iconRegister.registerIcon(Constants.EcTextureDomain + "makoreactor-front");
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase setter, ItemStack item) {
        super.onBlockPlacedBy(world, x, y, z, setter, item);
        int l = MathHelper.floor_double((double) (setter.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        EcTileMultiPass tile = (EcTileMultiPass) world.getTileEntity(x, y, z);
        if (tile instanceof EcTileEntityMakoReactor) {
            ((EcTileEntityMakoReactor) tile).setFace((byte) sides[l]);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        EcTileEntityMakoReactor tile = (EcTileEntityMakoReactor) world.getTileEntity(x, y, z);
        if (tile != null && tile.isActivated()) {
            player.openGui(EnchantChanger.instance, Constants.GUI_ID_MAKO_REACTOR, world, x, y, z);
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
        ArrayList<ItemStack> ores;
        Block block;
        int blockMeta;
        for (String baseOreName : baseBlocksOreName) {
            ores = OreDictionary.getOres(baseOreName);
            for (ItemStack itemStack : ores) {
                makoReactorController = new ItemStack(this, 1, 0);
                makoReactorController.setTagCompound(new NBTTagCompound());
                GameRegistry.UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(itemStack.getItem());
                blockMeta = ((ItemBlock) itemStack.getItem()).getMetadata(itemStack.getItemDamage());
                makoReactorController.getTagCompound().setString("EnchantChanger|baseBlock", uid.toString());
                makoReactorController.getTagCompound().setInteger("EnchantChanger|baseMeta", blockMeta);
                list.add(makoReactorController);
            }
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        EcTileEntityMakoReactor tile = (EcTileEntityMakoReactor) world.getTileEntity(x, y, z);
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
                        EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

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
        super.breakBlock(world, x, y, z, block, meta);
    }
}
