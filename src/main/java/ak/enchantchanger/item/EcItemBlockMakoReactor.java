package ak.enchantchanger.item;

import ak.enchantchanger.utils.StringUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * 魔晄炉のItemBlockクラス
 * Created by A.K. on 14/09/10.
 */
public class EcItemBlockMakoReactor extends ItemBlock {

    public EcItemBlockMakoReactor(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int itemDamage) {
        return itemDamage % 16;
    }

    @Override
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack itemStack) {
        String baseName = super.getItemStackDisplayName(itemStack);
        if (!itemStack.hasTagCompound()) return baseName;
        String baseBlockUniqueName = itemStack.getTagCompound().getString("enchantchanger|baseBlock");
        if (!baseBlockUniqueName.equals("")) {
            Block base = Block.REGISTRY.getObject(new ResourceLocation(baseBlockUniqueName));
            if (base == Blocks.AIR) return "";
            int meta = itemStack.getTagCompound().getInteger("enchantchanger|baseMeta");
            String baseBlockDisplayName = new ItemStack(base, 1, meta).getDisplayName();
            return String.format("%s (%s)", baseName, baseBlockDisplayName);
        }
        return baseName;
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(@Nonnull ItemStack itemStack) {
        return "tile.mako_reactor-machine";
    }

    /**
     * ItemStackのNBTから基底ブロックのItemStackを取得するメソッド
     *
     * @param itemStack 魔晄炉ブロックのItemStack
     * @return 基底ブロックのItemStack
     */
    public ItemStack getBaseBlockItemStack(ItemStack itemStack) {
        String baseBlockStr = itemStack.getTagCompound().getString("enchantchanger|baseBlock");
        int baseBlockMetaInt = itemStack.getTagCompound().getInteger("enchantchanger|baseMeta");
        Block block = StringUtils.getBlockFromString(baseBlockStr);
        return new ItemStack((Blocks.AIR != block) ? block : Blocks.IRON_BLOCK, 1, baseBlockMetaInt);
    }
}
