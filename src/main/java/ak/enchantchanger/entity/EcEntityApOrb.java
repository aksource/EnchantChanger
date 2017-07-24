package ak.enchantchanger.entity;

import ak.MultiToolHolders.ItemMultiToolHolder;
import ak.MultiToolHolders.inventory.InventoryToolHolder;
import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.utils.ConfigurationUtils;
import ak.enchantchanger.utils.EnchantmentUtils;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EcEntityApOrb extends Entity {
    /**
     * A constantly increasing value that RenderXPOrb uses to control the colour shifting (Green / yellow)
     */
    public int apColor;

    /**
     * The age of the XP orb in ticks.
     */
    private int apOrbAge = 0;
    private int field_35126_c;

    /**
     * The health of this XP orb.
     */
    private int apOrbHealth = 5;

    /**
     * This is how much XP this orb has.
     */
    private int apValue;

    public EcEntityApOrb(World world) {
        super(world);
    }

    public EcEntityApOrb(World world, double x, double y, double z, int apValue) {
        super(world);
        this.setSize(0.5F, 0.5F);
//		this.yOffset = this.height / 2.0F;
        this.setPosition(x, y, z);
        this.rotationYaw = (float) (Math.random() * 360.0D);
        this.motionX = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.motionY = (double) ((float) (Math.random() * 0.2D) * 2.0F);
        this.motionZ = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.apValue = apValue;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public int getBrightnessForRender(float partialTicks) {
        float var2 = 0.5F;

        if (var2 < 0.0F) {
            var2 = 0.0F;
        }

        if (var2 > 1.0F) {
            var2 = 1.0F;
        }

        int var3 = super.getBrightnessForRender(partialTicks);
        int var4 = var3 & 255;
        int var5 = var3 >> 16 & 255;
        var4 += (int) (var2 * 15.0F * 16.0F);

        if (var4 > 240) {
            var4 = 240;
        }

        return var4 | var5 << 16;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.field_35126_c > 0) {
            --this.field_35126_c;
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.029999999329447746D;

        if (this.worldObj.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).getMaterial() == Material.LAVA) {
            this.motionY = 0.20000000298023224D;
            this.motionX = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.motionZ = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.worldObj.playSound(this.posX, this.posY, this.posZ,
                    SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.NEUTRAL,
                    0.4F, 2.0F + this.rand.nextFloat() * 0.4F, true);
        }

        this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
        double var1 = 8.0D;
        EntityPlayer var3 = this.worldObj.getClosestPlayerToEntity(this, var1);

        if (var3 != null) {
            double var4 = (var3.posX - this.posX) / var1;
            double var6 = (var3.posY + (double) var3.getEyeHeight() - this.posY) / var1;
            double var8 = (var3.posZ - this.posZ) / var1;
            double var10 = Math.sqrt(var4 * var4 + var6 * var6 + var8 * var8);
            double var12 = 1.0D - var10;

            if (var12 > 0.0D) {
                var12 *= var12;
                this.motionX += var4 / var10 * var12 * 0.1D;
                this.motionY += var6 / var10 * var12 * 0.1D;
                this.motionZ += var8 / var10 * var12 * 0.1D;
            }
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        float var14 = 0.98F;

        if (this.onGround) {
            var14 = this.worldObj.getBlockState(new BlockPos(this.posX, this.getEntityBoundingBox().minY - 1, this.posZ))
                    .getBlock().slipperiness * 0.98F;
        }

        this.motionX *= (double) var14;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= (double) var14;

        if (this.onGround) {
            this.motionY *= -0.8999999761581421D;
        }

        ++this.apColor;
        ++this.apOrbAge;

        if (this.apOrbAge >= 6000) {
            this.setDead();
        }
    }

    public boolean handleWaterMovement() {
        return this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, this);
    }

    protected void dealFireDamage(int par1) {
        this.attackEntityFrom(DamageSource.inFire, par1);
    }

    private boolean attackEntityFrom(DamageSource damageSource, int i) {
        this.setBeenAttacked();
        this.apOrbHealth -= i;

        if (this.apOrbHealth <= 0) {
            this.setDead();
        }

        return false;
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setShort("Health", (short) ((byte) this.apOrbHealth));
        nbtTagCompound.setShort("Age", (short) this.apOrbAge);
        nbtTagCompound.setShort("Value", (short) this.apValue);
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound nbtTagCompound) {
        this.apOrbHealth = nbtTagCompound.getShort("Health") & 255;
        this.apOrbAge = nbtTagCompound.getShort("Age");
        this.apValue = nbtTagCompound.getShort("Value");
    }

    @Override
    public void onCollideWithPlayer(@Nonnull EntityPlayer entityIn) {
        if (!this.worldObj.isRemote) {
            if (this.field_35126_c == 0 && entityIn.xpCooldown == 0) {
                entityIn.xpCooldown = 2;
                this.worldObj.playSound(this.posX, this.posY, this.posZ,
                        SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                        SoundCategory.PLAYERS,
                        0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F),
                        false);
                entityIn.onItemPickup(this, 1);
                this.addAp(entityIn);
                this.setDead();
            }
        }
    }

    private void addAp(@Nonnull EntityPlayer player) {
        ItemStack[] items = new ItemStack[13];
        for (int i = 0; i < 9; i++) {
            items[i] = player.inventory.getStackInSlot(i);
        }
        System.arraycopy(player.inventory.armorInventory, 0, items, 9, player.inventory.armorInventory.length);

        for (ItemStack itemStack : items) {
            if (itemStack != null && itemStack.isItemEnchanted()) {
                if (EnchantChanger.loadMTH && itemStack.getItem() instanceof ItemMultiToolHolder) {
                    InventoryToolHolder tools = ((ItemMultiToolHolder) itemStack.getItem()).getInventoryFromItemStack(itemStack);
                    if (tools != null) {
                        for (int j = 0; j < tools.getSizeInventory(); j++) {
                            if (tools.getStackInSlot(j) != null && tools.getStackInSlot(j).isItemEnchanted()) {
                                addApToItem(tools.getStackInSlot(j));
                            }
                        }
                    }
                } else {
                    addApToItem(itemStack);
                }

            }
        }
    }

    private void addApToItem(ItemStack item) {
        NBTTagList enchantList;
        enchantList = item.getEnchantmentTagList();
        for (int j = 0; j < enchantList.tagCount(); j++) {
            int prevAp = enchantList.getCompoundTagAt(j).getInteger("ap");
            short enchantmentId = enchantList.getCompoundTagAt(j).getShort("id");
            short enchantmentLv = enchantList.getCompoundTagAt(j).getShort("lvl");
            Enchantment enchantment = Enchantment.getEnchantmentByID(enchantmentId);

            if (checkLevelLimit(enchantment, enchantmentLv)
                    /*|| EnchantmentUtils.MAGIC_ENCHANTMENT.contains(Integer.valueOf((int) enchantmentId))*/) {
                continue;
            }
            int nowAp = prevAp + this.apValue;
            if (EnchantmentUtils.isApLimit(enchantment.getRegistryName(), enchantmentLv, nowAp)) {
                nowAp -= EnchantmentUtils.getApLimit(enchantment.getRegistryName(), enchantmentLv);
                if (enchantmentLv < Short.MAX_VALUE)
                    enchantList.getCompoundTagAt(j).setShort("lvl", (short) (enchantmentLv + 1));
            }
            enchantList.getCompoundTagAt(j).setInteger("ap", nowAp);
        }
    }

    private boolean checkLevelLimit(Enchantment enchantment, int nowLevel) {
        if (enchantment == null) {
            return true;
        } else if (EnchantmentUtils.LEVEL_LIMIT_MAP.containsKey(enchantment.getRegistryName())) {
            int levelLimit = EnchantmentUtils.LEVEL_LIMIT_MAP.get(enchantment.getRegistryName());
            if (levelLimit == 0) {
                return enchantment.getMaxLevel() <= nowLevel;
            } else {
                return levelLimit <= nowLevel;
            }
        } else
            return enchantment.getMaxLevel() == 1 || ConfigurationUtils.enableLevelCap && enchantment.getMaxLevel() <= nowLevel;
    }

    public int getTextureByXP() {
        return this.apValue >= 2477 ? 10 : (this.apValue >= 1237 ? 9 : (this.apValue >= 617 ? 8
                : (this.apValue >= 307 ? 7 : (this.apValue >= 149 ? 6 : (this.apValue >= 73 ? 5
                : (this.apValue >= 37 ? 4 : (this.apValue >= 17 ? 3 : (this.apValue >= 7 ? 2
                : (this.apValue >= 3 ? 1 : 0)))))))));
    }
}
