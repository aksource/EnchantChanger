package ak.enchantchanger.item;


import ak.enchantchanger.EnchantChanger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class EcItemSephirothSwordImit extends EcItemSword {
    public EcItemSephirothSwordImit(String name) {
        super(ToolMaterial.IRON, name);
    }

    @SideOnly(Side.CLIENT)
    @Override
    @Nonnull
    public IPerspectiveAwareModel getPresentModel(@Nonnull ItemStack itemStack, @Nonnull List<IPerspectiveAwareModel> modelList) {
        EntityPlayer player = EnchantChanger.proxy.getPlayer();
        if (player != null) {
            ItemStack handHeldItem = player.getHeldItemMainhand();
            return (itemStack.isItemEqual(handHeldItem)) ? modelList.get(0): modelList.get(1);
        }
        return super.getPresentModel(itemStack, modelList);
    }
}