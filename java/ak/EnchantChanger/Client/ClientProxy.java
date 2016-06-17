package ak.EnchantChanger.Client;

import ak.EnchantChanger.Client.models.BakedModelMateria;
import ak.EnchantChanger.Client.models.MakoReactorModelLoader;
import ak.EnchantChanger.Client.renderer.*;
import ak.EnchantChanger.CommonProxy;
import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.entity.EcEntityApOrb;
import ak.EnchantChanger.entity.EcEntityExExpBottle;
import ak.EnchantChanger.entity.EcEntityMeteor;
import ak.EnchantChanger.eventhandler.LivingEventHooks;
import ak.EnchantChanger.network.MessageKeyPressed;
import ak.EnchantChanger.network.MessageLevitation;
import ak.EnchantChanger.network.PacketHandler;
import ak.EnchantChanger.tileentity.EcTileEntityHugeMateria;
import ak.EnchantChanger.utils.ConfigurationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import static ak.EnchantChanger.EnchantChanger.*;
import static ak.EnchantChanger.api.Constants.*;

public class ClientProxy extends CommonProxy {
    public static final float MOVE_FACTOR = 0.4F;
    public static KeyBinding MagicKey = new KeyBinding("Key.EcMagic",
            Keyboard.KEY_V, "EnchantChanger");
    public static KeyBinding MateriaKey = new KeyBinding("Key.EcMateria", Keyboard.KEY_R, "EnchantChanger");
    public static int customRenderPass;
    public static int multiPassRenderType;
    public static Minecraft mc = Minecraft.getMinecraft();
    private int flyToggleTimer = 0;
    private int sprintToggleTimer = 0;

    @Override
    public void registerPreRenderInformation() {
        //1.8からのモデル登録。
        B3DLoader.instance.addDomain(Constants.MOD_ID);
        OBJLoader.instance.addDomain(Constants.MOD_ID);
        /* 魔晄炉用モデルローダー登録 */
        ModelLoaderRegistry.registerLoader(new MakoReactorModelLoader());
//        registerCustomBlockModel(blockHugeMateria, 0, "blockhugemateria", MODEL_TYPE_INVENTORY, false);
//        registerCustomBlockModel(blockLifeStream, 0, "life_stream", MODEL_TYPE_FLUID, true);
//        registerCustomBlockModel(blockMakoReactor, 0, "blockmakoreactor", MODEL_TYPE_INVENTORY, false);
    }

    @Override
    public void registerRenderInformation() {
        ClientModelUtils.registerCustomBlockModel(blockHugeMateria, 0, "blockhugemateria", MODEL_TYPE_INVENTORY, false);
        ClientModelUtils.registerCustomBlockModel(blockLifeStream, 0, "life_stream", MODEL_TYPE_FLUID, true);
        ClientModelUtils.registerCustomBlockModel(blockMakoReactor, 0, "blockmakoreactor", MODEL_TYPE_INVENTORY, false);
        MinecraftForge.EVENT_BUS.register(new RenderingOverlayEvent());
        RenderingRegistry.registerEntityRenderingHandler(
                EcEntityExExpBottle.class, new EcRenderItemThrowable(mc.getRenderManager(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EcEntityMeteor.class,
                new EcRenderItemThrowable(mc.getRenderManager(), ConfigurationUtils.sizeMeteor));
        RenderingRegistry.registerEntityRenderingHandler(EcEntityApOrb.class,
                new EcRenderApOrb(mc.getRenderManager()));

        //キー登録
        ClientRegistry.registerKeyBinding(MagicKey);
        ClientRegistry.registerKeyBinding(MateriaKey);
        ClientModelUtils.registerModels();
        EcRenderPlayerBack ecRenderPlayerBack = new EcRenderPlayerBack();
        MinecraftForge.EVENT_BUS.register(ecRenderPlayerBack);

        //TextureStitchEvent
        MinecraftForge.EVENT_BUS.register(this);
        //モデルの光源処理修正イベントクラス登録
        MinecraftForge.EVENT_BUS.register(new ModelLightningFixer());
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
            BakedModelMateria.registerExtraMateria(enchantId, texId);
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
            ClientInputUtils.movePlayerY(player);
            ClientInputUtils.movePlayerXZ(player);
        }

        if (player.onGround && var5) {
            LivingEventHooks.setLevitationModeToNBT(player, false);
        }

        PacketHandler.INSTANCE.sendToServer(new MessageLevitation(LivingEventHooks.getLevitationModeToNBT(player)));
    }

    private static byte getKeyIndex() {
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
                        ClientInputUtils.doMagic(entityPlayer.getCurrentEquippedItem(), entityPlayer);
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
    public void textureStitch(TextureStitchEvent.Pre event) {
        TextureMap textureMap = event.map;
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
    public void bakedModelRegister(ModelBakeEvent event) {
        ClientModelUtils.changeModels(event);
    }

}