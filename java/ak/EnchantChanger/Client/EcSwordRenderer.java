package ak.EnchantChanger.Client;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import ak.EnchantChanger.EcItemCloudSword;
import ak.EnchantChanger.EcItemCloudSwordCore;
import ak.EnchantChanger.EcItemSephirothSword;
import ak.EnchantChanger.EcItemSephirothSwordImit;
import ak.EnchantChanger.EcItemUltimateWeapon;
import ak.EnchantChanger.EcItemZackSword;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EcSwordRenderer implements IItemRenderer
{
	@SideOnly(Side.CLIENT)
	private static final EcModelUltimateWeapon UModel = new EcModelUltimateWeapon();
	@SideOnly(Side.CLIENT)
	private static final EcModelCloudSwordCore2 CCModel = new EcModelCloudSwordCore2();
	@SideOnly(Side.CLIENT)
	private static final EcModelCloudSword2 CModel = new EcModelCloudSword2();
	@SideOnly(Side.CLIENT)
	private static final EcModelSephirothSword SModel = new EcModelSephirothSword();
	@SideOnly(Side.CLIENT)
	private static final EcModelZackSword ZModel = new EcModelZackSword();

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == ItemRenderType.EQUIPPED
				|| type == ItemRenderType.EQUIPPED_FIRST_PERSON;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (item.getItem() instanceof EcItemZackSword)
			ZModel.renderItem(item, (EntityLivingBase) data[1]);
		else if (item.getItem() instanceof EcItemCloudSword)
			CModel.renderItem(item, (EntityLivingBase) data[1]);
		else if (item.getItem() instanceof EcItemCloudSwordCore)
			CCModel.renderItem(item, (EntityLivingBase) data[1],
					((EcItemCloudSwordCore) item.getItem()).isActive(item));
		else if (item.getItem() instanceof EcItemSephirothSword
				|| item.getItem() instanceof EcItemSephirothSwordImit)
			SModel.renderItem(item, (EntityLivingBase) data[1]);
		else if (item.getItem() instanceof EcItemUltimateWeapon)
			UModel.renderItem(item, (EntityLivingBase) data[1]);
	}
}