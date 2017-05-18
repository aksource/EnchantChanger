package ak.enchantchanger.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IThrowableEntity;

import javax.annotation.Nonnull;

public class EcEntityExExpBottle extends EntityThrowable implements IThrowableEntity {
    public EcEntityExExpBottle(World world) {
        super(world);
    }

    public EcEntityExExpBottle(World world, EntityLivingBase throwerIn) {
        super(world, throwerIn);
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    @Override
    protected float getGravityVelocity() {
        return 0.07F;
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    @Override
    protected void onImpact(@Nonnull RayTraceResult rayTraceResult) {
        if (!this.world.isRemote) {
            this.world.playEvent(2002, new BlockPos(this.posX, this.posY, this.posZ), 0);
            int var2 = 30 + this.world.rand.nextInt(5) + this.world.rand.nextInt(5);

            while (var2 > 0) {
                int var3 = EntityXPOrb.getXPSplit(var2);
                var2 -= var3;
                this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, var3));
            }

            this.setDead();
        }
    }

    @Override
    public void setThrower(Entity entity) {

    }
}