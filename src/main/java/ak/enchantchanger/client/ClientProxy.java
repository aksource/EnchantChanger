package ak.enchantchanger.client;

import ak.enchantchanger.CommonProxy;
import ak.enchantchanger.api.Constants;
import ak.enchantchanger.client.models.BakedModelMateria;
import ak.enchantchanger.client.renderer.*;
import ak.enchantchanger.entity.EcEntityApOrb;
import ak.enchantchanger.entity.EcEntityExExpBottle;
import ak.enchantchanger.entity.EcEntityMeteor;
import ak.enchantchanger.network.MessageKeyPressed;
import ak.enchantchanger.network.MessageLevitation;
import ak.enchantchanger.network.PacketHandler;
import ak.enchantchanger.utils.ConfigurationUtils;
import ak.enchantchanger.utils.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import static ak.enchantchanger.EnchantChanger.livingeventhooks;
import static ak.enchantchanger.api.Constants.MagicKEY;
import static net.minecraft.init.Items.FIRE_CHARGE;

public class ClientProxy extends CommonProxy {
    static final float MOVE_FACTOR = 0.4F;
    public static Minecraft mc = Minecraft.getMinecraft();
    private static final KeyBinding MAGIC_KEY = new KeyBinding("Key.EcMagic",
            Keyboard.KEY_V, Constants.MOD_ID);
    private static final KeyBinding MATERIA_KEY = new KeyBinding("Key.EcMateria",
            Keyboard.KEY_R, Constants.MOD_ID);
    private int flyToggleTimer = 0;
    private int sprintToggleTimer = 0;

    private static byte getKeyIndex() {
        byte key = -1;
        if (MAGIC_KEY.isPressed()) {
            key = Constants.MagicKEY;
        } else if (MATERIA_KEY.isPressed()) {
            key = Constants.MateriaKEY;
        }

        return key;
    }

    @Override
    public void registerPreRenderInformation() {
        ClientModelUtils.registerModelsOnPreInit();
        // もともとinitで登録していたが、登録処理に使用するメソッドを変更したため、こちらに移動。
        // TODO registerModelsOnPreInitと統合
        ClientModelUtils.registerModels();
        RenderingRegistry.registerEntityRenderingHandler(
                EcEntityExExpBottle.class, manager -> new RenderSnowball<>(manager, Items.itemExExpBottle, mc.getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EcEntityMeteor.class,
                manager -> new EcRenderItemThrowable(manager, FIRE_CHARGE, mc.getRenderItem(), ConfigurationUtils.sizeMeteor));
        RenderingRegistry.registerEntityRenderingHandler(EcEntityApOrb.class,
                EcRenderApOrb::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityItem.class, manager -> new EcRenderEntityItemCustom(manager, mc.getRenderItem()));
    }

    @Override
    public void registerRenderInformation() {
        MinecraftForge.EVENT_BUS.register(new RenderingOverlayEvent());

        //キー登録
        ClientRegistry.registerKeyBinding(MAGIC_KEY);
        ClientRegistry.registerKeyBinding(MATERIA_KEY);
        EcRenderPlayerBack ecRenderPlayerBack = new EcRenderPlayerBack();
        MinecraftForge.EVENT_BUS.register(ecRenderPlayerBack);

        //TextureStitchEvent
        MinecraftForge.EVENT_BUS.register(this);
        //モデルの光源処理修正イベントクラス登録
        MinecraftForge.EVENT_BUS.register(new ModelLightningFixer());

    }

    @Override
    public void registerTileEntitySpecialRenderer() {
    }

    @Override
    public void registerExtraMateriaRendering(NBTTagCompound nbt) {
        if (nbt.hasKey("enchantId") && nbt.hasKey("materiaTexId")) {
            int enchantId = nbt.getInteger("enchantId");
            ResourceLocation registerName = new ResourceLocation(nbt.getString("register_name"));
            int texId = nbt.getInteger("materiaTexId");
            BakedModelMateria.registerExtraMateria(registerName, texId);
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
                livingeventhooks.setLevitationModeToNBT(player, !livingeventhooks.getLevitationModeToNBT(player));
                this.flyToggleTimer = 0;
            }
        }
        boolean var4 = (float) player.getFoodStats().getFoodLevel() > 6.0F;

        //Sprint判定。updatePlayerMoveStateしているので、再度判定する必要が有る。
        if (((EntityPlayerSP) player).onGround && !var3
                && ((EntityPlayerSP) player).movementInput.moveForward >= var2
                && !player.isSprinting() && var4 && !player.isHandActive()
                && !player.isPotionActive(MobEffects.BLINDNESS)) {
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

        boolean var5 = livingeventhooks.getLevitationModeToNBT(player);
        if (var5) {
            ClientInputUtils.movePlayerY(player);
            ClientInputUtils.movePlayerXZ(player);
        }

        if (player.onGround && var5) {
            livingeventhooks.setLevitationModeToNBT(player, false);
        }

        PacketHandler.INSTANCE.sendToServer(new MessageLevitation(livingeventhooks.getLevitationModeToNBT(player)));
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void KeyHandlingEvent(InputEvent.KeyInputEvent event) {
        if (FMLClientHandler.instance().getClient().inGameHasFocus && FMLClientHandler.instance().getClientPlayerEntity() != null) {
            EntityPlayer entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
            byte keyIndex = getKeyIndex();
            if (keyIndex != -1 && entityPlayer.getHeldItemMainhand() != null) {
                PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(keyIndex));
                switch (keyIndex) {
                    case MagicKEY:
                        ClientInputUtils.doMagic(entityPlayer.getHeldItemMainhand(), entityPlayer);
                        break;
                }
            }
        }
    }

    //    @SubscribeEvent
    public void mouseHandlingEvent(InputEvent.MouseInputEvent event) {
        if (mc.gameSettings.keyBindAttack.isKeyDown() && FMLClientHandler.instance().getClientPlayerEntity() != null) {
            ClientInputUtils.changeObjectMouseOver(FMLClientHandler.instance().getClientPlayerEntity());
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void textureStitch(TextureStitchEvent.Pre event) {
        TextureMap textureMap = event.getMap();
        for (int i = 0; i < 16; i++) {
            ClientModelUtils.textureAtlasSpritesMateriaArray[i] = textureMap.registerSprite(ClientModelUtils.resourceLocationsMateria[i]);
        }
        for (String key : ClientModelUtils.textureMapUltimate.keySet()) {
            textureMap.registerSprite(new ResourceLocation(ClientModelUtils.textureMapUltimate.get(key)));
        }
        for (String key : ClientModelUtils.textureMapBuster.keySet()) {
            textureMap.registerSprite(new ResourceLocation(ClientModelUtils.textureMapBuster.get(key)));
        }
        for (String key : ClientModelUtils.textureMapMasamune.keySet()) {
            textureMap.registerSprite(new ResourceLocation(ClientModelUtils.textureMapMasamune.get(key)));
        }
        for (String key : ClientModelUtils.textureMapFirst.keySet()) {
            textureMap.registerSprite(new ResourceLocation(ClientModelUtils.textureMapFirst.get(key)));
        }
        for (String key : ClientModelUtils.textureMapUnion.keySet()) {
            textureMap.registerSprite(new ResourceLocation(ClientModelUtils.textureMapUnion.get(key)));
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void bakedModelRegister(ModelBakeEvent event) {
        ClientModelUtils.changeModels(event);
    }

}