package ak.enchantchanger.item;

import ak.enchantchanger.EnchantChanger;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class EcItemSephirothSword extends EcItemSword {
    public static final double BoxSize = 5D;
    public boolean SephirothSprintAttack;
    public Entity SephirothSprintAttackEntity = null;

    public EcItemSephirothSword(String name) {
        super(ToolMaterial.DIAMOND, name);
    }

//	@Override
//	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer player)
//	{
//		player.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
//		if (player.isSneaking()) {
//			if (!player.capabilities.isCreativeMode)
//				player.setHealth(1);
//			player.addPotionEffect(new PotionEffect(1, 1200, 3));
//			player.addPotionEffect(new PotionEffect(3, 1200, 3));
//			player.addPotionEffect(new PotionEffect(5, 1200, 3));
//			player.addPotionEffect(new PotionEffect(8, 1200, 3));
//			player.addPotionEffect(new PotionEffect(11, 1200, 3));
//			player.addPotionEffect(new PotionEffect(12, 1200, 3));
//			player.addPotionEffect(new PotionEffect(13, 1200, 3));
//			player.addPotionEffect(new PotionEffect(16, 1200, 3));
//		}
//		return par1ItemStack;
//	}

    @Override
    public boolean onLeftClickEntity(@Nonnull ItemStack itemstack, @Nonnull EntityPlayer player, @Nonnull Entity entity) {
        if (player.isSprinting() && !SephirothSprintAttack) {
            SephirothSprintAttack = true;
            SephirothSprintAttackEntity = entity;
        }
        return super.onLeftClickEntity(itemstack, player, entity);
    }

    @Override
    public void onUpdate(@Nonnull ItemStack itemStack, @Nonnull World worldIn, @Nonnull Entity entityIn,
                         int itemSlot, boolean isSelected) {
        super.onUpdate(itemStack, worldIn, entityIn, itemSlot, isSelected);
        if (SephirothSprintAttack && entityIn instanceof EntityPlayer) {
            this.SephirothSprintAttack(worldIn, (EntityPlayer) entityIn);
            SephirothSprintAttack = false;
        }
    }

    public void SephirothSprintAttack(World world, EntityPlayer player) {
        Entity entity = SephirothSprintAttackEntity;

        if (entity instanceof EntityLiving) {
            List EntityList = world.getEntitiesWithinAABB(EntityLiving.class,
                    entity.getEntityBoundingBox().grow(BoxSize));
            for (Object object : EntityList) {
                Entity entity1 = (Entity) object;
                if (entity1 != entity && entity1 != player) {
                    player.attackTargetEntityWithCurrentItem(entity1);
                }
            }
        }
        this.SephirothSprintAttack = false;
    }

    @Override
    public double getReach(@Nonnull ItemStack itemStack) {
        return 5.0D;
    }

    @SideOnly(Side.CLIENT)
    @Override
    @Nonnull
    public IBakedModel getPresentModel(@Nonnull ItemStack itemStack, @Nonnull List<IBakedModel> modelList) {
        EntityPlayer player = EnchantChanger.proxy.getPlayer();
        if (player != null) {
            ItemStack handHeldItem = player.getHeldItemMainhand();
            return (itemStack.isItemEqual(handHeldItem)) ? modelList.get(0) : modelList.get(1);
        }
        return super.getPresentModel(itemStack, modelList);
    }
}
