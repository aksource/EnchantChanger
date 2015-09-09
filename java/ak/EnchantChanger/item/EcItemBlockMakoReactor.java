package ak.EnchantChanger.item;

import ak.EnchantChanger.utils.StringUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 魔晄炉のItemBlockクラス
 * Created by A.K. on 14/09/10.
 */
public class EcItemBlockMakoReactor extends ItemBlock {
    private static String[] nameArray = {"tile.makoreactor-machine", "tile.makoreactor-case"};

    public EcItemBlockMakoReactor(Block block) {
        super(block);
    }

    @Override
    public int getMetadata(int itemDamage) {
        return itemDamage % 16;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        String baseName = super.getItemStackDisplayName(itemStack);
        if (!itemStack.hasTagCompound()) return baseName;
        String baseBlockUniqueName = itemStack.getTagCompound().getString("EnchantChanger|baseBlock");
        if (!baseBlockUniqueName.equals("")) {
            String[] strings = baseBlockUniqueName.split(":");
            Block base = GameRegistry.findBlock(strings[0], strings[1]);
            if (base == null) return "";
            int meta = itemStack.getTagCompound().getInteger("EnchantChanger|baseMeta");
            String baseBlockDisplayName = new ItemStack(base, 1, meta).getDisplayName();
            return String.format("%s (%s)", baseName, baseBlockDisplayName);
        }
        return baseName;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        int meta = itemStack.getItemDamage() % 16;
        if (meta < nameArray.length) {
            return nameArray[meta];
        }
        return "";
    }

    /**
     * ItemStackのNBTから基底ブロックのItemStackを取得するメソッド
     * @param itemStack 魔晄炉ブロックのItemStack
     * @return 基底ブロックのItemStack
     */
    public ItemStack getBaseBlockItemStack(ItemStack itemStack) {
        String baseBlockStr = itemStack.getTagCompound().getString("EnchantChanger|baseBlock");
        int baseBlockMetaInt = itemStack.getTagCompound().getInteger("EnchantChanger|baseMeta");
        Block block = StringUtils.getBlockFromString(baseBlockStr);
        return new ItemStack((Blocks.air != block) ? block : Blocks.iron_block, 1, baseBlockMetaInt);
    }
}
