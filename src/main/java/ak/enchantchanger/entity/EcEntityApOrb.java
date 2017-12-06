package ak.enchantchanger.entity;

import ak.MultiToolHolders.ItemMultiToolHolder;
import ak.MultiToolHolders.inventory.InventoryToolHolder;
import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.utils.ConfigurationUtils;
import ak.enchantchanger.utils.EnchantmentUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EcEntityApOrb extends EntityXPOrb {

    @SuppressWarnings("unused")
    public EcEntityApOrb(World world) {
        super(world);
    }

    public EcEntityApOrb(World world, double x, double y, double z, int xpValue) {
        super(world, x, y, z, xpValue);
    }

    @Override
    public void onCollideWithPlayer(@Nonnull EntityPlayer entityIn) {
        if (!this.worldObj.isRemote) {
            if (this.delayBeforeCanPickup == 0 && entityIn.xpCooldown == 0) {
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
                            ItemStack itemStack1 = tools.getStackInSlot(j);
                            if (itemStack1 != null && itemStack1.isItemEnchanted()) {
                                addApToItem(itemStack1);
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

            if (checkLevelLimit(enchantment, enchantmentLv)) {
                continue;
            }
            int nowAp = prevAp + this.xpValue;
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
        return 10;
    }
}
