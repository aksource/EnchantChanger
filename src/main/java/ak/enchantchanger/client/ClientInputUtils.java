package ak.enchantchanger.client;

import ak.MultiToolHolders.ItemMultiToolHolder;
import ak.MultiToolHolders.inventory.InventoryToolHolder;
import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.api.ICustomReachItem;
import ak.enchantchanger.item.EcItemSword;
import ak.enchantchanger.network.MessageExtendedReachAttack;
import ak.enchantchanger.network.PacketHandler;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.Timer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * クライアント側のキーボード、マウスの処理をまとめたクラス
 * Created by A.K. on 2016/05/29.
 */
public class ClientInputUtils {

    private static Timer timer = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, ClientProxy.mc, "timer", "field_71428_T");

    private ClientInputUtils() {}

    static void movePlayerY(EntityPlayer player) {
        EntityPlayerSP playerSP = (EntityPlayerSP) player;

        player.motionY = 0.0D;

        if (playerSP.movementInput.sneak) {
            player.motionY -= ClientProxy.MOVE_FACTOR;
        }

        if (playerSP.movementInput.jump) {
            player.motionY += ClientProxy.MOVE_FACTOR;
        }
    }

    static void movePlayerXZ(EntityPlayer player) {
        EntityPlayerSP playerSP = (EntityPlayerSP) player;
        float moveForward = playerSP.movementInput.moveForward;
        float moveStrafe = playerSP.movementInput.moveStrafe;

        if (moveForward != 0 || moveStrafe != 0) {
            player.motionX = player.motionZ = 0;
        }
        player.moveRelative(moveStrafe, moveForward, ClientProxy.MOVE_FACTOR * 1.2F);
    }

    static void doMagic(ItemStack itemStack, EntityPlayer player) {
        if (itemStack.getItem() instanceof EcItemSword) {
            EcItemSword.doMagic(itemStack, player.getEntityWorld(), player);
        } else if (EnchantChanger.loadMTH && itemStack.getItem() instanceof ItemMultiToolHolder) {
            //ツールホルダーとの連携処理。
            ItemMultiToolHolder mth = (ItemMultiToolHolder) player.getHeldItemMainhand().getItem();
            InventoryToolHolder inventoryToolHolder = mth.getInventoryFromItemStack(player.getHeldItemMainhand());
            ItemStack innerItemStack = inventoryToolHolder.getStackInSlot(ItemMultiToolHolder.getSlotNumFromItemStack(itemStack));
            if (innerItemStack != null && innerItemStack.getItem() instanceof EcItemSword) {
                EcItemSword.doMagic(innerItemStack, player.getEntityWorld(), player);
            }
        }
    }

    static void changeObjectMouseOver(@Nonnull EntityPlayer player) {
        ItemStack heldItem = player.getHeldItemMainhand();
        if (heldItem.getItem() instanceof ICustomReachItem) {
            double extendedReach = ((ICustomReachItem) heldItem.getItem()).getReach(heldItem);
            RayTraceResult traceResult = getMouseOverSpecialReach(player, extendedReach, timer.renderPartialTicks);
            if (traceResult != null && traceResult.typeOfHit == RayTraceResult.Type.ENTITY) {
                ClientProxy.mc.objectMouseOver = traceResult;
                Entity pointedEntity = traceResult.entityHit;
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    PacketHandler.INSTANCE.sendToServer(new MessageExtendedReachAttack(pointedEntity));
                }
            }
        }
    }

    public static RayTraceResult getMouseOverSpecialReach(@Nonnull EntityLivingBase viewingEntity, double reach, float partialTicks) {
        double vanillaExtendedReach = 6.0d;
        double vanillaBlockDistance = Minecraft.getMinecraft().playerController.getBlockReachDistance();
        RayTraceResult traceResult = viewingEntity.rayTrace(reach < vanillaBlockDistance ? vanillaBlockDistance : reach, partialTicks);
        Vec3d viewPosition = viewingEntity.getPositionEyes(partialTicks);
        double d0 = reach < vanillaExtendedReach ? vanillaExtendedReach : reach;
        double d1 = 0;

        if (traceResult != null) {
            d1 = traceResult.hitVec.distanceTo(viewPosition);
            Block block = viewingEntity.getEntityWorld().getBlockState(traceResult.getBlockPos()).getBlock();
            if (Blocks.AIR == block) {
                d1++;
            }
        }

        Vec3d lookVector = viewingEntity.getLook(partialTicks);
        Vec3d reachVector = viewPosition.addVector(lookVector.xCoord * d0, lookVector.yCoord * d0, lookVector.zCoord * d0);
        Vec3d vec33 = null;
        float f1 = 1.0F;
        List<Entity> list = viewingEntity.getEntityWorld().getEntitiesInAABBexcluding(
                viewingEntity,
                viewingEntity.getEntityBoundingBox()
                        .addCoord(lookVector.xCoord * d0, lookVector.yCoord * d0, lookVector.zCoord * d0)
                        .expand(f1, f1, f1), Predicates.and(EntitySelectors.NOT_SPECTATING, (entity) -> entity != null && entity.canBeCollidedWith()));
        double d2 = d1;
        Entity pointedEntity = null;
        for (Entity entity : list) {
            float collisionSize = entity.getCollisionBorderSize();
            AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expandXyz(collisionSize);
            RayTraceResult rayTraceResult = axisalignedbb.calculateIntercept(viewPosition, reachVector);

            if (axisalignedbb.isVecInside(viewPosition)) {
                if (d2 >= 0.0D) {
                    pointedEntity = entity;
                    vec33 = rayTraceResult == null ? viewPosition : rayTraceResult.hitVec;
                    d2 = 0.0D;
                }
            } else if (rayTraceResult != null) {
                double d3 = viewPosition.distanceTo(rayTraceResult.hitVec);

                if (d3 < d2 || d2 == 0.0D) {
                    if (entity.getLowestRidingEntity() == viewingEntity.getLowestRidingEntity() && !entity.canRiderInteract()) {
                        if (d2 == 0.0D) {
                            pointedEntity = entity;
                            vec33 = rayTraceResult.hitVec;
                        }
                    } else {
                        pointedEntity = entity;
                        vec33 = rayTraceResult.hitVec;
                        d2 = d3;
                    }
                }
            }
        }

        if (pointedEntity != null && (d2 < d1 || traceResult == null)) {
            traceResult = new RayTraceResult(pointedEntity, vec33);
        }
        return traceResult;
    }
}
