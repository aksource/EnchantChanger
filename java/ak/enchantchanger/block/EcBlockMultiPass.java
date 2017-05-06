package ak.EnchantChanger.block;

import ak.EnchantChanger.Client.ClientProxy;
import ak.EnchantChanger.CommonProxy;
import ak.EnchantChanger.tileentity.EcTileMultiPass;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by A.K. on 14/03/08.
 */
public class EcBlockMultiPass extends BlockContainer {
    private static final Map<ChunkCoordinates, EcTileMultiPass> removingTileMap = new HashMap<>();

    public EcBlockMultiPass(Material material) {
        super(material);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public int getRenderType() {
        return CommonProxy.multiPassRenderType;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        ClientProxy.customRenderPass = pass;
        return true;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        EcTileMultiPass tileMultiPass = (EcTileMultiPass) world.getTileEntity(x, y, z);
        if (tileMultiPass != null && ClientProxy.customRenderPass == 0) {
            Block block = tileMultiPass.getBaseBlock();
            return block.getIcon(side, tileMultiPass.getBaseMeta());
        }
        return this.blockIcon;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase setter, ItemStack item) {
        super.onBlockPlacedBy(world, x, y, z, setter, item);
        EcTileMultiPass tile = (EcTileMultiPass) world.getTileEntity(x, y, z);
        if (item.hasTagCompound() && item.getTagCompound().hasKey("EnchantChanger|baseBlock")) {
            tile.baseBlock = item.getTagCompound().getString("EnchantChanger|baseBlock");
            tile.baseMeta = item.getTagCompound().getInteger("EnchantChanger|baseMeta");
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        removingTileMap.put(new ChunkCoordinates(x, y, z), (EcTileMultiPass) world.getTileEntity(x, y, z));
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        EcTileMultiPass tileMultiPass = removingTileMap.remove(new ChunkCoordinates(x, y, z));
        ArrayList<ItemStack> list = super.getDrops(world, x, y, z, metadata, fortune);
        if (tileMultiPass != null) {
            list.clear();
            ItemStack dropItemBlock = new ItemStack(this, 1, this.damageDropped(metadata));
            dropItemBlock.setTagCompound(new NBTTagCompound());
            dropItemBlock.getTagCompound().setString("EnchantChanger|baseBlock", tileMultiPass.baseBlock);
            dropItemBlock.getTagCompound().setInteger("EnchantChanger|baseMeta", tileMultiPass.baseMeta);
            list.add(dropItemBlock);
            return list;
        }
        return list;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        ItemStack pickItem = super.getPickBlock(target, world, x, y, z);
        EcTileMultiPass tile = (EcTileMultiPass) world.getTileEntity(x, y, z);
        pickItem.setTagCompound(new NBTTagCompound());
        pickItem.getTagCompound().setString("EnchantChanger|baseBlock", tile.baseBlock);
        pickItem.getTagCompound().setInteger("EnchantChanger|baseMeta", tile.baseMeta);
        return pickItem;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new EcTileMultiPass();
    }
}
