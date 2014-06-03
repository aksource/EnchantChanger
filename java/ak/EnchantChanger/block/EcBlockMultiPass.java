package ak.EnchantChanger.block;

import ak.EnchantChanger.Client.ClientProxy;
import ak.EnchantChanger.tileentity.EcTileMultiPass;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by A.K. on 14/03/08.
 */
public class EcBlockMultiPass extends BlockContainer{
    public EcBlockMultiPass(Material material) {
        super(material);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
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
        String base = (item.getTagCompound() != null)? item.getTagCompound().getString("baseBlock"): GameRegistry.findUniqueIdentifierFor(Blocks.stone).toString();
        EcTileMultiPass tile = (EcTileMultiPass) world.getTileEntity(x, y, z);
        tile.baseBlock = base;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new EcTileMultiPass();
    }
}
