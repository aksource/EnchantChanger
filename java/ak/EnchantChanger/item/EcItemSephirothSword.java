package ak.EnchantChanger.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class EcItemSephirothSword extends EcItemSword {
    public static final double BoxSize = 5D;
    public boolean SephirothSprintAttack;
    public Entity SephirothSprintAttackEntity = null;

    public EcItemSephirothSword(String name) {
        super(ToolMaterial.EMERALD, name);
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
    public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer player, Entity entity) {
        if (player.isSprinting() && !SephirothSprintAttack) {
            SephirothSprintAttack = true;
            SephirothSprintAttackEntity = entity;
        }
        return super.onLeftClickEntity(itemstack, player, entity);
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
        super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
        if (SephirothSprintAttack && par3Entity instanceof EntityPlayer) {
            this.SephirothSprintAttack(par2World, (EntityPlayer) par3Entity);
            SephirothSprintAttack = false;
        }
    }

    public void SephirothSprintAttack(World world, EntityPlayer player) {
        Entity entity = SephirothSprintAttackEntity;

        if (entity instanceof EntityLiving) {
            List EntityList = world.getEntitiesWithinAABB(EntityLiving.class,
                    entity.boundingBox.expand(BoxSize, BoxSize, BoxSize));
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
    public double getReach(ItemStack itemStack) {
        return 5.0D;
    }
}
