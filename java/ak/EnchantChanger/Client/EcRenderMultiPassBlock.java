package ak.EnchantChanger.Client;

import ak.EnchantChanger.EcTileMultiPass;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by A.K. on 14/03/08.
 */
public class EcRenderMultiPassBlock implements ISimpleBlockRenderingHandler, IItemRenderer {
    private static Map renderMap = new HashMap();
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return renderMap.get(type) == helper;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (item.getItem() instanceof ItemBlock && item.hasTagCompound() && item.getTagCompound().hasKey("baseBlock")) {
            RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item.getItem()).getRenderType());
        }
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (! (world.getTileEntity(x, y, z) instanceof EcTileMultiPass)) return false;
        EcTileMultiPass ecTileMultiPass = (EcTileMultiPass)world.getTileEntity(x, y, z);
        Block block1 = Blocks.air;
        if (ClientProxy.customRenderPass == 0) {
            String[] strings = ecTileMultiPass.baseBlock.split(":");
            block1 = GameRegistry.findBlock(strings[0], strings[1]);
            renderer.renderStandardBlock(block1, x, y, z);
        } else {
            renderer.renderStandardBlock(block, x, y, z);
        }
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return ClientProxy.multiPassRenderType;
    }
    static {
        renderMap.put(ItemRenderType.ENTITY, ItemRendererHelper.ENTITY_ROTATION);
        renderMap.put(ItemRenderType.EQUIPPED, ItemRendererHelper.EQUIPPED_BLOCK);
        renderMap.put(ItemRenderType.EQUIPPED_FIRST_PERSON, ItemRendererHelper.EQUIPPED_BLOCK);
        renderMap.put(ItemRenderType.INVENTORY, ItemRendererHelper.INVENTORY_BLOCK);
    }
}
