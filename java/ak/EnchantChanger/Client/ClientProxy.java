package ak.EnchantChanger.Client;

import ak.EnchantChanger.Client.renderer.*;
import ak.EnchantChanger.CommonProxy;
import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.api.ICustomReachItem;
import ak.EnchantChanger.entity.EcEntityApOrb;
import ak.EnchantChanger.entity.EcEntityExExpBottle;
import ak.EnchantChanger.entity.EcEntityMeteor;
import ak.EnchantChanger.eventhandler.LivingEventHooks;
import ak.EnchantChanger.item.EcItemSword;
import ak.EnchantChanger.network.MessageExtendedReachAttack;
import ak.EnchantChanger.network.MessageKeyPressed;
import ak.EnchantChanger.network.MessageLevitation;
import ak.EnchantChanger.network.PacketHandler;
import ak.EnchantChanger.tileentity.EcTileEntityHugeMateria;
import ak.EnchantChanger.utils.ConfigurationUtils;
import ak.MultiToolHolders.ItemMultiToolHolder;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.input.Keyboard;

import java.util.List;

import static ak.EnchantChanger.api.Constants.MagicKEY;

public class ClientProxy extends CommonProxy {
    public static final float moveFactor = 0.4F;
    public static KeyBinding MagicKey = new KeyBinding("Key.EcMagic",
            Keyboard.KEY_V, "EnchantChanger");
    public static KeyBinding MateriaKey = new KeyBinding("Key.EcMateria", Keyboard.KEY_R, "EnchantChanger");
    public static int customRenderPass;
    public static int multiPassRenderType;
    public static Minecraft mc = Minecraft.getMinecraft();
    private static Timer timer = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, mc, 17);
    private int flyToggleTimer = 0;
    private int sprintToggleTimer = 0;

    private static void movePlayerY(EntityPlayer player) {
        EntityPlayerSP playerSP = (EntityPlayerSP) player;

        player.motionY = 0.0D;

        if (playerSP.movementInput.sneak) {
            player.motionY -= moveFactor;
        }

        if (playerSP.movementInput.jump) {
            player.motionY += moveFactor;
        }
    }

    private static void movePlayerXZ(EntityPlayer player) {
        EntityPlayerSP playerSP = (EntityPlayerSP) player;
        float moveForward = playerSP.movementInput.moveForward;
        float moveStrafe = playerSP.movementInput.moveStrafe;

        if (moveForward != 0 || moveStrafe != 0) {
            player.motionX = player.motionZ = 0;
        }
        player.moveFlying(moveStrafe, moveForward, moveFactor * 1.2F);
    }

    @Override
    public void registerRenderInformation() {
        MinecraftForge.EVENT_BUS.register(new RenderingOverlayEvent());
        RenderingRegistry.registerEntityRenderingHandler(
                EcEntityExExpBottle.class, new EcRenderItemThrowable(mc.getRenderManager(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EcEntityMeteor.class,
                new EcRenderItemThrowable(mc.getRenderManager(), ConfigurationUtils.sizeMeteor));
        RenderingRegistry.registerEntityRenderingHandler(EcEntityApOrb.class,
                new EcRenderApOrb(mc.getRenderManager()));
//        multiPassRenderType = RenderingRegistry.getNextAvailableRenderId();
//        RenderingRegistry.registerBlockHandler(ecRenderMultiPassBlock);

        //キー登録
        ClientRegistry.registerKeyBinding(MagicKey);
        ClientRegistry.registerKeyBinding(MateriaKey);

        //1.8からのモデル登録。
        B3DLoader.instance.addDomain(Constants.MOD_ID);
        registerCustomItemModel(EnchantChanger.itemZackSword, 0, "bustersword.b3d");
        registerCustomItemModel(EnchantChanger.ItemCloudSwordCore, 0, "firstsword.b3d");
        registerCustomItemModel(EnchantChanger.itemZackSword, 0, "bustersword.b3d");
        registerCustomItemModel(EnchantChanger.itemCloudSword, 0, "unionsword.b3d");
        registerCustomItemModel(EnchantChanger.itemMateria, 0, "spherelight.b3d");
        registerCustomItemModel(EnchantChanger.itemMasterMateria, 0, "spherelight.b3d");
        registerCustomItemModel(EnchantChanger.itemSephirothSword, 0, "masamune.b3d");
        registerCustomItemModel(EnchantChanger.itemImitateSephirothSword, 0, "masamune.b3d");
        registerCustomItemModel(EnchantChanger.itemUltimateWeapon, 0, "ultimateweapon.b3d");
        registerItemModel(EnchantChanger.itemHugeMateria, 0);
        registerItemModel(EnchantChanger.itemExExpBottle, 0);
        registerItemModel(EnchantChanger.itemBucketLifeStream, 0);
        registerItemModel(EnchantChanger.itemPortableEnchantChanger, 0);
        registerItemModel(EnchantChanger.itemPortableEnchantmentTable, 0);
        registerCustomBlockModel(EnchantChanger.blockHugeMateria, 0, "hugemateria.b3d");
        registerBlockModel(EnchantChanger.blockEnchantChanger, 0);
        registerBlockModel(EnchantChanger.blockMakoReactor, 0);

        EcRenderPlayerBack ecRenderPlayerBack = new EcRenderPlayerBack();
        MinecraftForge.EVENT_BUS.register(ecRenderPlayerBack);
        FMLCommonHandler.instance().bus().register(ecRenderPlayerBack);

        //TextureStitchEvent
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void registerTileEntitySpecialRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(
                EcTileEntityHugeMateria.class, new EcRenderHugeMateria());
    }

    @Override
    public void registerExtraMateriaRendering(NBTTagCompound nbt) {
        if (nbt.hasKey("enchantId") && nbt.hasKey("materiaTexId")) {
            int enchantId = nbt.getInteger("enchantId");
            int texId = nbt.getInteger("materiaTexId");
            EcRenderMateria.registerExtraMateria(enchantId, texId);
        }
    }

    @Override
    public EntityPlayer getPlayer() {
        return mc.thePlayer;
    }

    @Override
    public void doFlightOnSide(EntityPlayer player) {
        boolean jump = ((EntityPlayerSP) player).movementInput.jump;
        float var2 = 0.8F;
        boolean var3 = ((EntityPlayerSP) player).movementInput.moveForward >= var2;
        ((EntityPlayerSP) player).movementInput.updatePlayerMoveState();
        if (!jump && ((EntityPlayerSP) player).movementInput.jump) {
            if (this.flyToggleTimer == 0) {
                this.flyToggleTimer = 7;
            } else {
                LivingEventHooks.setLevitationModeToNBT(player, !LivingEventHooks.getLevitationModeToNBT(player));
                this.flyToggleTimer = 0;
            }
        }
        boolean var4 = (float) player.getFoodStats().getFoodLevel() > 6.0F;

        //Sprint判定。updatePlayerMoveStateしているので、再度判定する必要が有る。
        if (((EntityPlayerSP) player).onGround && !var3
                && ((EntityPlayerSP) player).movementInput.moveForward >= var2
                && !player.isSprinting() && var4 && !player.isUsingItem()
                && !player.isPotionActive(Potion.blindness)) {
            if (this.sprintToggleTimer == 0) {
                this.sprintToggleTimer = 7;
            } else {
                player.setSprinting(true);
                this.sprintToggleTimer = 0;
            }
        }
        if (this.sprintToggleTimer > 0) {
            --this.sprintToggleTimer;
        }
        //Sprint判定ここまで。

        if (this.flyToggleTimer > 0) {
            --this.flyToggleTimer;
        }

        boolean var5 = LivingEventHooks.getLevitationModeToNBT(player);
        if (var5) {
            movePlayerY(player);
            movePlayerXZ(player);
        }

        if (player.onGround && var5) {
            LivingEventHooks.setLevitationModeToNBT(player, false);
        }

        PacketHandler.INSTANCE.sendToServer(new MessageLevitation(LivingEventHooks.getLevitationModeToNBT(player)));
    }

    private byte getKeyIndex() {
        byte key = -1;
        if (MagicKey.isPressed()) {
            key = Constants.MagicKEY;
        } else if (MateriaKey.isPressed()) {
            key = Constants.MateriaKEY;
        }

        return key;
    }

    @SubscribeEvent
    public void KeyHandlingEvent(InputEvent.KeyInputEvent event) {
        if (FMLClientHandler.instance().getClient().inGameHasFocus && FMLClientHandler.instance().getClientPlayerEntity() != null) {
            EntityPlayer entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
            byte keyIndex = getKeyIndex();
            if (keyIndex != -1 && entityPlayer.getCurrentEquippedItem() != null) {
                PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(keyIndex));
                switch (keyIndex) {
                    case MagicKEY:
                        doMagic(entityPlayer.getCurrentEquippedItem(), entityPlayer);
                        break;
                }
            }
        }
    }

    private void doMagic(ItemStack itemStack, EntityPlayer player) {
        if (itemStack.getItem() instanceof EcItemSword) {
            EcItemSword.doMagic(itemStack, player.worldObj, player);
        } else if (EnchantChanger.loadMTH && itemStack.getItem() instanceof ItemMultiToolHolder) {
            //ツールホルダーとの連携処理。
            ItemMultiToolHolder mth = (ItemMultiToolHolder) player.inventory.getCurrentItem().getItem();
            if (mth.getInventoryFromItemStack(itemStack).getStackInSlot(ItemMultiToolHolder.getSlotNumFromItemStack(itemStack)) != null
                    && mth.getInventoryFromItemStack(itemStack).getStackInSlot(ItemMultiToolHolder.getSlotNumFromItemStack(itemStack)).getItem() instanceof EcItemSword) {
                EcItemSword.doMagic(mth.getInventoryFromItemStack(itemStack).getStackInSlot(ItemMultiToolHolder.getSlotNumFromItemStack(itemStack)), player.worldObj, player);
            }
        }
    }

    @SubscribeEvent
    public void mouseHandlingEvent(InputEvent.MouseInputEvent event) {
        if (mc.gameSettings.keyBindAttack.isKeyDown() && FMLClientHandler.instance().getClientPlayerEntity() != null) {
            changeObjectMouseOver(FMLClientHandler.instance().getClientPlayerEntity());
        }
    }

    private void changeObjectMouseOver(EntityPlayer player) {
        ItemStack heldItem = player.getCurrentEquippedItem();
        if (heldItem != null && heldItem.getItem() instanceof ICustomReachItem) {
            double extendedReach = ((ICustomReachItem) heldItem.getItem()).getReach(heldItem);
            MovingObjectPosition MOP = getMouseOverSpecialReach(player, extendedReach, timer.renderPartialTicks);
            if (MOP != null && MOP.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                mc.objectMouseOver = MOP;
                Entity pointedEntity = MOP.entityHit;
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    PacketHandler.INSTANCE.sendToServer(new MessageExtendedReachAttack(pointedEntity));
                }
            }
        }
    }

    private MovingObjectPosition getMouseOverSpecialReach(EntityLivingBase viewingEntity, double reach, float partialTicks) {
        MovingObjectPosition MOP = null;
        if (viewingEntity != null) {
            if (viewingEntity.worldObj != null) {
                MOP = viewingEntity.rayTrace(reach, partialTicks);
                Vec3 viewPosition = viewingEntity.getPositionVector();
                double d1 = 0;

                if (MOP != null) {
                    d1 = MOP.hitVec.distanceTo(viewPosition);
                }

                Vec3 lookVector = viewingEntity.getLook(partialTicks);
                Vec3 reachVector = viewPosition.addVector(lookVector.xCoord * reach, lookVector.yCoord * reach, lookVector.zCoord * reach);
                Vec3 vec33 = null;
                float f1 = 1.0F;
                @SuppressWarnings("unchecked")
                List<Entity> list = viewingEntity.worldObj.getEntitiesWithinAABBExcludingEntity(viewingEntity, viewingEntity.getEntityBoundingBox().addCoord(lookVector.xCoord * reach, lookVector.yCoord * reach, lookVector.zCoord * reach).expand(f1, f1, f1));
                double d2 = d1;
                Entity pointedEntity = null;
                for (Entity entity : list) {
                    if (entity.canBeCollidedWith()) {
                        float collisionSize = entity.getCollisionBorderSize();
                        AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(collisionSize, collisionSize, collisionSize);
                        MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(viewPosition, reachVector);

                        if (axisalignedbb.isVecInside(viewPosition)) {
                            if (0.0D < d2 || d2 == 0.0D) {
                                pointedEntity = entity;
                                vec33 = movingobjectposition == null ? viewPosition : movingobjectposition.hitVec;
                                d2 = 0.0D;
                            }
                        } else if (movingobjectposition != null) {
                            double d3 = viewPosition.distanceTo(movingobjectposition.hitVec);

                            if (d3 < d2 || d2 == 0.0D) {
                                if (entity == viewingEntity.ridingEntity && !entity.canRiderInteract()) {
                                    if (d2 == 0.0D) {
                                        pointedEntity = entity;
                                        vec33 = movingobjectposition.hitVec;
                                    }
                                } else {
                                    pointedEntity = entity;
                                    vec33 = movingobjectposition.hitVec;
                                    d2 = d3;
                                }
                            }
                        }
                    }
                }

                if (pointedEntity != null && (d2 < d1 || MOP == null)) {
                    MOP = new MovingObjectPosition(pointedEntity, vec33);
                }
            }
        }
        return MOP;
    }

    @SubscribeEvent
    public void textureStitch(TextureStitchEvent.Post event) {
        IBakedModel lifeStreamModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(EnchantChanger.blockLifeStream));
        EnchantChanger.fluidLifeStream.setIcons(lifeStreamModel.getTexture());
    }

    private void registerCustomBlockModel(Block block, int meta, String modelLocation) {
        registerCustomItemModel(Item.getItemFromBlock(block), meta, modelLocation);
    }

    private void registerCustomItemModel(Item item, int meta, String modelLocation) {
        ModelBakery.addVariantName(item, modelLocation);
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Constants.EcAssetsDomain + ":" + modelLocation, "inventory"));
        //EnchantChanger.logger.info("added ModelLocation of " + Constants.EcAssetsDomain + ":" + modelLocation);
    }

    private void registerBlockModel(Block block, int meta) {
        registerItemModel(Item.getItemFromBlock(block), meta);
    }

    private void registerItemModel(Item item, int meta) {
        mc.getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(GameRegistry.findUniqueIdentifierFor(item).toString(), "inventory"));
    }
}