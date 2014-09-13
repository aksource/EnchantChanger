package ak.EnchantChanger.block;

import ak.EnchantChanger.Client.ClientProxy;
import ak.EnchantChanger.tileentity.EcTileMultiPass;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
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
        return ClientProxy.multiPassRenderType;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        ClientProxy.customRenderPass = pass;
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase setter, ItemStack item) {
        super.onBlockPlacedBy(world, x, y, z, setter, item);
        String base = (item.getTagCompound() != null)? item.getTagCompound().getString("EnchantChanger|baseBlock"): GameRegistry.findUniqueIdentifierFor(Blocks.stone).toString();
        EcTileMultiPass tile = (EcTileMultiPass) world.getTileEntity(x, y, z);
        tile.baseBlock = base;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        removingTileMap.put(new ChunkCoordinates(x, y, z), (EcTileMultiPass)world.getTileEntity(x, y, z));
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
            list.add(dropItemBlock);
            return list;
        }
        return list;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new EcTileMultiPass();
    }
}
