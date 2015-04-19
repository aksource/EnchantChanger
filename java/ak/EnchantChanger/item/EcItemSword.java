package ak.EnchantChanger.item;

import ak.EnchantChanger.ExtendedPlayerData;
import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.api.ICustomReachItem;
import ak.EnchantChanger.network.MessageKeyPressed;
import ak.EnchantChanger.network.PacketHandler;
import ak.EnchantChanger.utils.ConfigurationUtils;
import ak.EnchantChanger.utils.EnchantmentUtils;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class EcItemSword extends ItemSword implements ICustomReachItem {

	public EcItemSword(ToolMaterial toolMaterial, String name) {
		super(toolMaterial);
        String s = String.format("%s%s", Constants.EcTextureDomain, name);
        this.setUnlocalizedName(s);
        this.setTextureName(s);
        this.setNoRepair();
        this.setCreativeTab(Constants.TAB_ENCHANT_CHANGER);
	}

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        ExtendedPlayerData data = ExtendedPlayerData.get(player);
        if (data.isLimitBreaking() && data.getLimitBreakId() == Constants.LIMIT_BREAK_OMNISLASH_FIRST) {
            entity.hurtResistantTime = 0;
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(Constants.CtrlKEY));
        }
        return super.onItemRightClick(itemStack, world, player);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advToolTip) {
        if (EnchantmentUtils.hasMagic(itemStack)) {
            for (byte b : EnchantmentUtils.getMagic(itemStack)) {
                list.add(EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal(EcItemMateria.MAGIC_NAME[b - 1]));
            }
        }
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("pickaxe");
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        return toolClass.equals("pickaxe") ? 2 : 0;
    }

    public static void doMagic(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
        byte b = getMagicEnchantId(par1ItemStack);
		if (EnchantmentHelper.getEnchantmentLevel(
				ConfigurationUtils.idEnchantmentMeteor, par1ItemStack) > 0 || b == Constants.MAGIC_ID_METEOR) {
			EcItemMateria.doMeteor(par2World, par3EntityPlayer);
		}
		if (EnchantmentHelper.getEnchantmentLevel(
				ConfigurationUtils.idEnchantmentHoly, par1ItemStack) > 0 || b == Constants.MAGIC_ID_HOLY) {
			EcItemMateria.doHoly(par2World, par3EntityPlayer);
		}
		if (EnchantmentHelper.getEnchantmentLevel(
				ConfigurationUtils.idEnchantmentTelepo, par1ItemStack) > 0 || b == Constants.MAGIC_ID_TELEPO) {
			EcItemMateria.teleportPlayer(par2World, par3EntityPlayer);
		}
		if (EnchantmentHelper.getEnchantmentLevel(
				ConfigurationUtils.idEnchantmentThunder, par1ItemStack) > 0 || b == Constants.MAGIC_ID_THUNDER) {
			EcItemMateria.doThunder(par2World, par3EntityPlayer);
		}
	}

    private static byte getMagicEnchantId(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) return -1;
        //Deprecated
        if (EnchantmentHelper.getEnchantmentLevel(
                ConfigurationUtils.idEnchantmentMeteor, itemStack) > 0) {
            return Constants.MAGIC_ID_METEOR;
        }
        //Deprecated
        if (EnchantmentHelper.getEnchantmentLevel(
                ConfigurationUtils.idEnchantmentHoly, itemStack) > 0) {
            return Constants.MAGIC_ID_HOLY;
        }
        //Deprecated
        if (EnchantmentHelper.getEnchantmentLevel(
                ConfigurationUtils.idEnchantmentTelepo, itemStack) > 0) {
            return Constants.MAGIC_ID_TELEPO;
        }
        //Deprecated
        if (EnchantmentHelper.getEnchantmentLevel(
                ConfigurationUtils.idEnchantmentThunder, itemStack) > 0) {
            return Constants.MAGIC_ID_THUNDER;
        }

        for (byte b : EnchantmentUtils.getMagic(itemStack)) {
            if (!isPassiveMagic(b)) {
                return b;
            }
        }
        return -1;
    }

    public static boolean isPassiveMagic(byte b) {
        return b == Constants.MAGIC_ID_FLOAT;
    }

	public static boolean hasFloat(ItemStack itemstack) {
		return EnchantmentHelper.getEnchantmentLevel(
				ConfigurationUtils.idEnchantmentFloat, itemstack) > 0 || hasFloatNBT(itemstack);
	}

    private static boolean hasFloatNBT(ItemStack itemStack) {
        for (byte b : EnchantmentUtils.getMagic(itemStack)) {
            if (b == Constants.MAGIC_ID_FLOAT) {
                return true;
            }
        }
        return false;
    }

    public void doLimitBreak(ItemStack itemStack, EntityPlayer player) {
        byte limitBreakId = ExtendedPlayerData.get(player).getLimitBreakId();
        if (limitBreakId == Constants.LIMIT_BREAK_OMNISLASH_FIRST) {
            doOmniSlashFirst(player);
        }
        if (limitBreakId == Constants.LIMIT_BREAK_POWER_UP) {
            doPowerUp(player);
        }
        player.addChatMessage(new ChatComponentText("LIMIT BREAK!!"));
    }

    private void doOmniSlashFirst(EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), Constants.LIMIT_BREAK_TIME, 3));
    }

    private void doPowerUp(EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), Constants.LIMIT_BREAK_TIME, 3));
//        player.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 1200, 3));
        player.addPotionEffect(new PotionEffect(Potion.damageBoost.getId(), Constants.LIMIT_BREAK_TIME, 3));
        player.addPotionEffect(new PotionEffect(Potion.heal.getId(), Constants.LIMIT_BREAK_TIME, 3));
        player.addPotionEffect(new PotionEffect(Potion.resistance.getId(), Constants.LIMIT_BREAK_TIME, 3));
        player.addPotionEffect(new PotionEffect(Potion.fireResistance.getId(), Constants.LIMIT_BREAK_TIME, 3));
        player.addPotionEffect(new PotionEffect(Potion.waterBreathing.getId(), Constants.LIMIT_BREAK_TIME, 3));
        player.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), Constants.LIMIT_BREAK_TIME, 3));
    }

	// 内蔵武器切り替え用攻撃メソッドの移植
	public void attackTargetEntityWithTheItem(Entity par1Entity,
			EntityPlayer player, ItemStack stack, boolean cancelHurt) {
		if (MinecraftForge.EVENT_BUS.post(new AttackEntityEvent(player,
				par1Entity))) {
			return;
		}
		if (stack != null
				&& stack.getItem().onLeftClickEntity(stack, player, par1Entity)) {
			return;
		}
		if (par1Entity.canAttackWithItem()) {
			if (!par1Entity.hitByEntity(player)) {
				float var2 = (float) this.getItemStrength(stack);
				if (player.isPotionActive(Potion.damageBoost)) {
					var2 += 3 << player.getActivePotionEffect(
							Potion.damageBoost).getAmplifier();
				}

				if (player.isPotionActive(Potion.weakness)) {
					var2 -= 2 << player.getActivePotionEffect(Potion.weakness)
							.getAmplifier();
				}

				int var3 = 0;
				int var4 = 0;

				if (par1Entity instanceof EntityLivingBase) {
					var4 = this.getEnchantmentModifierLiving(stack, player,
							(EntityLivingBase) par1Entity);
					var3 += EnchantmentHelper.getEnchantmentLevel(
							Enchantment.knockback.effectId, stack);
				}

				if (player.isSprinting()) {
					++var3;
				}

				if (var2 > 0 || var4 > 0) {
					boolean var5 = player.fallDistance > 0.0F
							&& !player.onGround && !player.isOnLadder()
							&& !player.isInWater()
							&& !player.isPotionActive(Potion.blindness)
							&& player.ridingEntity == null
							&& par1Entity instanceof EntityLivingBase;

					if (var5 && var2 > 0) {
						var2 *= 1.5F;
					}

					var2 += var4;
					boolean var6 = false;
					int var7 = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);

					if (par1Entity instanceof EntityLivingBase && var7 > 0
							&& !par1Entity.isBurning()) {
						var6 = true;
						par1Entity.setFire(1);
					}

					boolean var8 = par1Entity.attackEntityFrom(
							DamageSource.causePlayerDamage(player), var2);

					if (var8) {
						if (var3 > 0) {
							par1Entity.addVelocity(
									(double) (-MathHelper
											.sin(player.rotationYaw
													* (float) Math.PI / 180.0F)
											* (float) var3 * 0.5F),
									0.1D,
									(double) (MathHelper.cos(player.rotationYaw
											* (float) Math.PI / 180.0F)
											* (float) var3 * 0.5F));
							player.motionX *= 0.6D;
							player.motionZ *= 0.6D;
							player.setSprinting(false);
						}

						if (var5) {
							player.onCriticalHit(par1Entity);
						}

						if (var4 > 0) {
							player.onEnchantmentCritical(par1Entity);
						}

						if (var2 >= 18) {
							player.triggerAchievement(AchievementList.overkill);
						}

						player.setLastAttacker(par1Entity);

						EnchantmentHelper.func_151385_b(player, par1Entity);
						Object object = par1Entity;

						if (par1Entity instanceof EntityDragonPart)
						{
							IEntityMultiPart ientitymultipart = ((EntityDragonPart)par1Entity).entityDragonObj;

							if (ientitymultipart != null && ientitymultipart instanceof EntityLivingBase)
							{
								object = ientitymultipart;
							}
						}

						if (stack != null && object instanceof EntityLivingBase) {
                            stack.hitEntity((EntityLivingBase) object, player);
							if (cancelHurt)
								par1Entity.hurtResistantTime = 0;
							if (stack.stackSize <= 0) {
								this.destroyTheItem(player, stack);
							}
						}
					}

					if (par1Entity instanceof EntityLivingBase) {
						player.addStat(StatList.damageDealtStat,
								Math.round(var2 * 10.0F));

						if (var7 > 0 && var8) {
							par1Entity.setFire(var7 * 4);
						} else if (var6) {
							par1Entity.extinguish();
						}
					}

					player.addExhaustion(0.3F);
				}
			}
		}
	}

	public double getItemStrength(ItemStack item) {
		Multimap multimap = item.getAttributeModifiers();
		double d1 = 0;
		if (!multimap.isEmpty()) {

            for (Object object : multimap.entries()) {
                Entry entry = (Entry) object;
                AttributeModifier attributemodifier = (AttributeModifier) entry
                        .getValue();
                if (attributemodifier.getOperation() != 1
                        && attributemodifier.getOperation() != 2) {
                    d1 = attributemodifier.getAmount();
                } else {
                    d1 = attributemodifier.getAmount() * 100.0D;
                }
            }
		}
		return d1;
	}

	public int getEnchantmentModifierLiving(ItemStack stack,
			EntityLivingBase attacker, EntityLivingBase enemy) {
		int calc = 0;
		if (stack != null) {
			NBTTagList nbttaglist = stack.getEnchantmentTagList();

			if (nbttaglist != null) {
				for (int i = 0; i < nbttaglist.tagCount(); ++i) {
					short short1 =  nbttaglist.getCompoundTagAt(i)
							.getShort("id");
					short short2 = nbttaglist.getCompoundTagAt(i)
							.getShort("lvl");

					if (Enchantment.enchantmentsList[short1] != null) {
						calc += Enchantment.enchantmentsList[short1]
								.func_152376_a(short2, enemy.getCreatureAttribute());
					}
				}
			}
		}
		return calc > 0 ? 1 + attacker.worldObj.rand.nextInt(calc) : 0;
	}

	public void destroyTheItem(EntityPlayer player, ItemStack orig) {
	}

    @Override
    public double getReach(ItemStack itemStack) {
        return 4.0D;
    }

    //ServerOnly
    public void doCtrlKeyAction(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        ExtendedPlayerData data = ExtendedPlayerData.get(entityPlayer);
        if (entityPlayer.isSneaking() && data.canLimitBreak()) {
            data.setLimitGaugeValue(0);
            data.setLimitBreakCount(Constants.LIMIT_BREAK_TIME);
            doLimitBreak(itemStack, entityPlayer);
        } else {
            byte id = (byte)(data.getLimitBreakId() + 1);
            data.setLimitBreakId(id);
        }
    }
}