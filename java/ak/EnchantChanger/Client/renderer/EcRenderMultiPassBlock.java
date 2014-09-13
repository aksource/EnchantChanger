package ak.EnchantChanger.Client.renderer;

import ak.EnchantChanger.Client.ClientProxy;
import ak.EnchantChanger.tileentity.EcTileMultiPass;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by A.K. on 14/03/08.
 */
public class EcRenderMultiPassBlock implements ISimpleBlockRenderingHandler, IItemRenderer {
    private static final Map<ItemRenderType, ItemRendererHelper> renderMap = new HashMap<>();
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
        if (item.getItem() instanceof ItemBlock && item.hasTagCompound() && item.getTagCompound().hasKey("EnchantChanger|baseBlock")) {
            RenderBlocks renderBlocks = (RenderBlocks)data[0];
            for (int pass = 0; pass < 2 ; pass++) {
                if (pass == 0) {
                    String[] strings = item.getTagCompound().getString("EnchantChanger|baseBlock").split(":");
                    Block base = GameRegistry.findBlock(strings[0], strings[1]);
                    renderBlocks.renderBlockAsItem(base, 0, 1.0F);
                } else {
                    Block multiPassBlock = Block.getBlockFromItem(item.getItem());
                    renderBlocks.renderBlockAsItem(multiPassBlock, item.getItemDamage(), 1.0F);
                }
            }
        }
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();
        float f2;
        float f3;
        int k;
        if (renderer.useInventoryTint)
        {
            k = block.getRenderColor(metadata);
            f2 = (float)(k >> 16 & 255) / 255.0F;
            f3 = (float)(k >> 8 & 255) / 255.0F;
            float f4 = (float)(k & 255) / 255.0F;
            GL11.glColor4f(f2 * 1.0F, f3 * 1.0F, f4 * 1.0F, 1.0F);
        }

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();

        if (renderer.useInventoryTint)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(world.getTileEntity(x, y, z) instanceof EcTileMultiPass)) return false;
        EcTileMultiPass ecTileMultiPass = (EcTileMultiPass)world.getTileEntity(x, y, z);
        if (ecTileMultiPass.baseBlock.equals("")) return false;
        Block block1;
        if (ClientProxy.customRenderPass == 0) {
            String[] strings = ecTileMultiPass.baseBlock.split(":");
            if (strings.length > 1) {
                block1 = GameRegistry.findBlock(strings[0], strings[1]);
            } else {
                block1 = GameRegistry.findBlock("minecraft", strings[0]);
            }
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
