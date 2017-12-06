package ak.enchantchanger.entity;

import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.utils.ConfigurationUtils;
import ak.enchantchanger.utils.EnchantmentUtils;
import ak.multitoolholders.ItemMultiToolHolder;
import ak.multitoolholders.inventory.InventoryToolHolder;
import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

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
        if (!this.world.isRemote) {
            if (this.delayBeforeCanPickup == 0 && entityIn.xpCooldown == 0) {
                entityIn.xpCooldown = 2;
                this.world.playSound(this.posX, this.posY, this.posZ,
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
        List<ItemStack> items = Lists.newArrayList();
        items.addAll(player.inventory.mainInventory.subList(0, 9));
        items.addAll(player.inventory.armorInventory);
        items.addAll(player.inventory.offHandInventory);

        for (ItemStack itemStack : items) {
            if (!itemStack.isEmpty() && itemStack.isItemEnchanted()) {
                if (EnchantChanger.loadMTH && itemStack.getItem() instanceof ItemMultiToolHolder) {
                    InventoryToolHolder tools = ((ItemMultiToolHolder) itemStack.getItem()).getInventoryFromItemStack(itemStack);
                    for (int j = 0; j < tools.getSizeInventory(); j++) {
                        ItemStack itemStack1 = tools.getStackInSlot(j);
                        if (!itemStack1.isEmpty() && itemStack1.isItemEnchanted()) {
                            addApToItem(itemStack1);
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
        return this.xpValue >= 2477 ? 10 : (this.xpValue >= 1237 ? 9 : (this.xpValue >= 617 ? 8
                : (this.xpValue >= 307 ? 7 : (this.xpValue >= 149 ? 6 : (this.xpValue >= 73 ? 5
                : (this.xpValue >= 37 ? 4 : (this.xpValue >= 17 ? 3 : (this.xpValue >= 7 ? 2
                : (this.xpValue >= 3 ? 1 : 0)))))))));
    }
}
