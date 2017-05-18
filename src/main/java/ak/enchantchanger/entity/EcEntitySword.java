package ak.enchantchanger.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;


public class EcEntitySword extends Entity {
    public EntityLiving enemyEntity;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile = Blocks.AIR;
    private boolean inGround = false;
    private int ticksAlive;
    private int ticksInAir = 0;
    private float expLimit = 1.0f;
    private float size = 0.5f;

    public EcEntitySword(World world) {
        super(world);
        this.setSize(size, size);
    }

    public EcEntitySword(World world, double x, double y, double z, double motionX, double motionY, double motionZ, float Yaw, float Pitch) {
        super(world);
        this.setSize(size, size);
        this.setLocationAndAngles(x, y, z, Yaw, Pitch);
        this.setPosition(x, y, z);
        double scale = (double) MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
        this.accelerationX = motionX / scale * 0.1D;
        this.accelerationY = motionY / scale * 0.1D;
        this.accelerationZ = motionZ / scale * 0.1D;
    }

    public EcEntitySword(World world, EntityLiving enemyEntity, double motionX, double motionY, double motionZ) {
        super(world);
        this.enemyEntity = enemyEntity;
        this.setSize(size, size);
        this.setLocationAndAngles(enemyEntity.posX, enemyEntity.posY, enemyEntity.posZ, enemyEntity.rotationYaw, enemyEntity.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = this.motionY = this.motionZ = 0.0D;
        motionX += this.rand.nextGaussian() * 0.4D;
        motionY += this.rand.nextGaussian() * 0.4D;
        motionZ += this.rand.nextGaussian() * 0.4D;
        double var9 = (double) MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
        this.accelerationX = motionX / var9 * 0.1D;
        this.accelerationY = motionY / var9 * 0.1D;
        this.accelerationZ = motionZ / var9 * 0.1D;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public boolean isInRangeToRenderDist(double par1) {
        double var3 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
        var3 *= 64.0D;
        return par1 < var3 * var3;
    }

    @Override
    public void onUpdate() {
        if (!this.world.isRemote &&
                (this.enemyEntity != null && this.enemyEntity.isDead
                        || !this.world.isBlockLoaded(new BlockPos(this.posX, this.posY, this.posZ)))) {
            this.setDead();
        } else {
            super.onUpdate();
            this.setFire(1);

            if (this.inGround) {
                if (this.world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).getBlock() == this.inTile) {
                    ++this.ticksAlive;

                    if (this.ticksAlive == 600) {
                        this.setDead();
                    }

                    return;
                }

                this.inGround = false;
                this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
                this.ticksAlive = 0;
                this.ticksInAir = 0;
            } else {
                ++this.ticksInAir;
            }

            Vec3d var15 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d var2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult var3 = this.world.rayTraceBlocks(var15, var2);
            var15 = new Vec3d(this.posX, this.posY, this.posZ);
            var2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (var3 != null) {
                var2 = new Vec3d(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
            }

            Entity var4 = null;
            List<Entity> entityList = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var6 = 0.0D;

            for (Entity entity : entityList) {

                if (entity.canBeCollidedWith() && (!entity.isEntityEqual(this.enemyEntity) || this.ticksInAir >= 25)) {
                    float var10 = 0.3F;
                    AxisAlignedBB aabb = entity.getEntityBoundingBox().expand((double) var10, (double) var10, (double) var10);
                    RayTraceResult traceResult = aabb.calculateIntercept(var15, var2);

                    if (traceResult != null) {
                        double var13 = var15.distanceTo(traceResult.hitVec);

                        if (var13 < var6 || var6 == 0.0D) {
                            var4 = entity;
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

//			for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var16) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
//			{
//				;
//			}

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

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onImpact(RayTraceResult traceResult) {
        if (!this.world.isRemote) {
            /**
             if (traceResult.entityHit != null && traceResult.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 4))
             {
             ;
             }
             */
            this.world.newExplosion(null, this.posX, this.posY, this.posZ, expLimit, true, false);
            this.setDead();
        }
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setShort("xTile", (short) this.xTile);
        nbtTagCompound.setShort("yTile", (short) this.yTile);
        nbtTagCompound.setShort("zTile", (short) this.zTile);
        nbtTagCompound.setByte("inTile", (byte) Block.getIdFromBlock(this.inTile));
        nbtTagCompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound nbtTagCompound) {
        this.xTile = nbtTagCompound.getShort("xTile");
        this.yTile = nbtTagCompound.getShort("yTile");
        this.zTile = nbtTagCompound.getShort("zTile");
        this.inTile = Block.getBlockById(nbtTagCompound.getByte("inTile") & 255);
        this.inGround = nbtTagCompound.getByte("inGround") == 1;
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
    public boolean attackEntityFrom(@Nonnull DamageSource damageSource, float amount) {
        this.setBeenAttacked();

        if (damageSource.getEntity() != null) {
            Vec3d lookVec = damageSource.getEntity().getLookVec();

            if (lookVec != null) {
                this.motionX = lookVec.xCoord;
                this.motionY = lookVec.yCoord;
                this.motionZ = lookVec.zCoord;
                this.accelerationX = this.motionX * 0.1D;
                this.accelerationY = this.motionY * 0.1D;
                this.accelerationZ = this.motionZ * 0.1D;
            }

            if (damageSource.getEntity() instanceof EntityLiving) {
                this.enemyEntity = (EntityLiving) damageSource.getEntity();
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public float getBrightness(float partialTicks) {
        return 1.0F;
    }

    @Override
    public int getBrightnessForRender(float partialTicks) {
        return 15728880;
    }
}