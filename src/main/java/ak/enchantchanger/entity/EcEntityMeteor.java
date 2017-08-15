package ak.enchantchanger.entity;

import ak.enchantchanger.utils.ConfigurationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;


public class EcEntityMeteor extends Entity {
    private EntityLivingBase shootingEntity;
    private double accelerationX;
    private double accelerationY;
    private double accelerationZ;
    private float expLimit = ConfigurationUtils.powerMeteor;
    private String throwerName;
    private float size = ConfigurationUtils.sizeMeteor;

    public EcEntityMeteor(World world) {
        super(world);
        this.setSize(size, size);
    }

    public EcEntityMeteor(World world, double x, double y, double z, double motionX, double motionY, double motionZ, float Yaw, float Pitch) {
        super(world);
        this.setSize(size, size);
        this.setLocationAndAngles(x, y, z, Yaw, Pitch);
        this.setPosition(x, y, z);
        double var14 = (double) MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
        this.accelerationX = motionX / var14 * 0.1D;
        this.accelerationY = motionY / var14 * 0.1D;
        this.accelerationZ = motionZ / var14 * 0.1D;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        double var3 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
        var3 *= 64.0D;
        return distance < var3 * var3;
    }

    @Override
    public void onUpdate() {
        if (!this.world.isRemote &&
                (this.shootingEntity != null
                        && this.shootingEntity.isDead || !this.world.isBlockLoaded(new BlockPos(this.posX, this.posY, this.posZ)))) {
            this.setDead();
        } else {
            super.onUpdate();
            this.setFire(1);

            Vec3d var15 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d var2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult var3 = this.world.rayTraceBlocks(var15, var2);
            var15 = new Vec3d(this.posX, this.posY, this.posZ);
            var2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (var3 != null) {
                var2 = new Vec3d(var3.hitVec.x, var3.hitVec.y, var3.hitVec.z);
            }

            Entity var4 = null;
            List<Entity> var5 = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var6 = 0.0D;
            EntityLivingBase entitylivingbase = this.getThrower();

            for (Entity var9 : var5) {

                if (var9.canBeCollidedWith() && (!var9.isEntityEqual(entitylivingbase)/* || this.ticksInAir >= 25*/)) {
                    float var10 = 0.3F;
                    AxisAlignedBB var11 = var9.getEntityBoundingBox().expand((double) var10, (double) var10, (double) var10);
                    RayTraceResult var12 = var11.calculateIntercept(var15, var2);

                    if (var12 != null) {
                        double var13 = var15.distanceTo(var12.hitVec);

                        if (var13 < var6 || var6 == 0.0D) {
                            var4 = var9;
                            var6 = var13;
                        }
                    }
                }
            }

            if (var4 != null) {
                var3 = new RayTraceResult(var4);
            }

            if (var3 != null) {
                this.onImpact(var3);
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float var16 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) var16) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
                ;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float var17 = 0.95F;

            if (this.isInWater()) {
                for (int var19 = 0; var19 < 4; ++var19) {
                    float var18 = 0.25F;
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double) var18, this.posY - this.motionY * (double) var18, this.posZ - this.motionZ * (double) var18, this.motionX, this.motionY, this.motionZ);
                }

                var17 = 0.8F;
            }

            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
            this.motionX *= (double) var17;
            this.motionY *= (double) var17;
            this.motionZ *= (double) var17;
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    private void onImpact(RayTraceResult rayTraceResult) {
        if (!this.world.isRemote) {
            /**
             if (par1MovingObjectPosition.entityHit != null && par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 4))
             {
             ;
             }
             */
            this.world.newExplosion(null, this.posX, this.posY, this.posZ, expLimit, true, true);
            this.setDead();
        }
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound nbtTagCompound) {
        if ((this.throwerName == null || this.throwerName.length() == 0) && this.shootingEntity != null && this.shootingEntity instanceof EntityPlayer) {
            this.throwerName = this.shootingEntity.getName();
        }

        nbtTagCompound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound nbtTagCompound) {
        this.throwerName = nbtTagCompound.getString("ownerName");

        if (this.throwerName.length() == 0) {
            this.throwerName = null;
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public float getCollisionBorderSize() {
        return 1.0F;
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        this.setBeenAttacked();

        if (source.getTrueSource() != null) {
            Vec3d lookVec = source.getTrueSource().getLookVec();

            this.motionX = lookVec.x;
            this.motionY = lookVec.y;
            this.motionZ = lookVec.z;
            this.accelerationX = this.motionX * 0.1D;
            this.accelerationY = this.motionY * 0.1D;
            this.accelerationZ = this.motionZ * 0.1D;

            if (source.getTrueSource() instanceof EntityLiving) {
                this.shootingEntity = (EntityLiving) source.getTrueSource();
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    @Override
    public int getBrightnessForRender() {
        return 15728880;
    }

    public EntityLivingBase getThrower() {
        if (this.shootingEntity == null && this.throwerName != null && this.throwerName.length() > 0) {
            this.shootingEntity = this.world.getPlayerEntityByName(this.throwerName);
        }

        return this.shootingEntity;
    }
}