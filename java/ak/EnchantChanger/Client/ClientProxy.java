package ak.EnchantChanger.Client;

import ak.EnchantChanger.Client.renderer.*;
import ak.EnchantChanger.CommonProxy;
import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.entity.EcEntityApOrb;
import ak.EnchantChanger.entity.EcEntityExExpBottle;
import ak.EnchantChanger.entity.EcEntityMeteor;
import ak.EnchantChanger.eventhandler.LivingEventHooks;
import ak.EnchantChanger.item.EcItemSword;
import ak.EnchantChanger.network.MessageKeyPressed;
import ak.EnchantChanger.network.MessageLevitation;
import ak.EnchantChanger.network.PacketHandler;
import ak.EnchantChanger.tileentity.EcTileEntityHugeMateria;
import ak.MultiToolHolders.ItemMultiToolHolder;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {
	public static KeyBinding MagicKey = new KeyBinding("Key.EcMagic",
			Keyboard.KEY_V, "EnchantChanger");
    public static KeyBinding MateriaKey = new KeyBinding("Key.EcMateria", Keyboard.KEY_R, "EnchantChanger");
    public static int customRenderPass;
    public static int multiPassRenderType;
    public static EcRenderMultiPassBlock ecRenderMultiPassBlock = new EcRenderMultiPassBlock();
    public static Minecraft mc = Minecraft.getMinecraft();

    private int flyToggleTimer = 0;
    private int sprintToggleTimer = 0;

    private static final float moveFactor = 0.4F;

	@Override
	public void registerRenderInformation() {
        MinecraftForge.EVENT_BUS.register(new RenderingOverlayEvent());
		RenderingRegistry.registerEntityRenderingHandler(
				EcEntityExExpBottle.class, new EcRenderItemThrowable(0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EcEntityMeteor.class,
				new EcRenderItemThrowable(EnchantChanger.sizeMeteor));
		RenderingRegistry.registerEntityRenderingHandler(EcEntityApOrb.class,
				new EcRenderApOrb());
        multiPassRenderType = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(ecRenderMultiPassBlock);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(EnchantChanger.blockMakoReactor), ecRenderMultiPassBlock);

		ClientRegistry.registerKeyBinding(MagicKey);
        ClientRegistry.registerKeyBinding(MateriaKey);
		IItemRenderer swordRenderer = new EcRenderSwordModel();
		MinecraftForgeClient.registerItemRenderer(
				EnchantChanger.itemSephirothSword,
				swordRenderer);
		MinecraftForgeClient.registerItemRenderer(
				EnchantChanger.itemZackSword,
				swordRenderer);
		MinecraftForgeClient.registerItemRenderer(
				EnchantChanger.ItemCloudSwordCore,
				swordRenderer);
		MinecraftForgeClient.registerItemRenderer(
				EnchantChanger.itemCloudSword,
				swordRenderer);
		MinecraftForgeClient.registerItemRenderer(
				EnchantChanger.itemUltimateWeapon,
				swordRenderer);
		MinecraftForgeClient.registerItemRenderer(
				EnchantChanger.itemImitateSephirothSword,
				swordRenderer);
		IItemRenderer materiaRenderer = new EcRenderMateria();
		MinecraftForgeClient.registerItemRenderer(EnchantChanger.itemMateria,
				materiaRenderer);
		MinecraftForgeClient.registerItemRenderer(
				EnchantChanger.itemMasterMateria, materiaRenderer);
	}

	@Override
	public void registerTileEntitySpecialRenderer() {
		ClientRegistry.bindTileEntitySpecialRenderer(
				EcTileEntityHugeMateria.class, new EcRenderHugeMateria());
	}

    @Override
    public EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public void flightClient(EntityPlayer player) {
        boolean jump = ((EntityPlayerSP) player).movementInput.jump;
        float var2 = 0.8F;
        boolean var3 = ((EntityPlayerSP) player).movementInput.moveForward >= var2;
        ((EntityPlayerSP) player).movementInput.updatePlayerMoveState();
        if (!jump && ((EntityPlayerSP) player).movementInput.jump) {
            if (this.flyToggleTimer == 0) {
                this.flyToggleTimer = 7;
            } else {
                LivingEventHooks.setModeToNBT(player, !LivingEventHooks.getModeToNBT(player));
                this.flyToggleTimer = 0;
            }
        }
        boolean var4 = (float) player.getFoodStats().getFoodLevel() > 6.0F;

        //Sprint判定。updatePlayerMoveStateしているので、再度判定する必要が有る。
        if (((EntityPlayerSP) player).onGround && !var3
                && ((EntityPlayerSP) player).movementInput.moveForward >= var2
                && ! player.isSprinting() && var4 && !player.isUsingItem()
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

        boolean var5 = LivingEventHooks.getModeToNBT(player);
        if (var5) {
            movePlayerY(player);
            movePlayerXZ(player);
        }

        if (player.onGround && var5) {
            LivingEventHooks.setModeToNBT(player, false);
        }

        PacketHandler.INSTANCE.sendToServer(new MessageLevitation(LivingEventHooks.getModeToNBT(player)));
    }


    private void movePlayerY(EntityPlayer player) {
        EntityPlayerSP playerSP = (EntityPlayerSP)player;

        player.motionY = 0.0D;

        if (playerSP.movementInput.sneak) {
            player.motionY -= moveFactor;
        }

        if (playerSP.movementInput.jump) {
            player.motionY += moveFactor;
        }
    }

    private void movePlayerXZ(EntityPlayer player) {
        EntityPlayerSP playerSP = (EntityPlayerSP)player;
        float moveForward = playerSP.movementInput.moveForward;
        float moveStrafe = playerSP.movementInput.moveStrafe;

        if (moveForward != 0 || moveStrafe != 0) {
            player.motionX = player.motionZ = 0;
        }
        player.moveFlying(moveStrafe, moveForward, moveFactor * 1.2F);
    }

    private byte getKeyIndex() {
        byte key = -1;
        if (ClientProxy.MagicKey.isPressed()) {
            key = EnchantChanger.MagicKEY;
        } else if (ClientProxy.MateriaKey.isPressed()) {
            key = EnchantChanger.MateriaKEY;
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
                switch(keyIndex) {
                    case EnchantChanger.MagicKEY :doMagic(entityPlayer.getCurrentEquippedItem(), entityPlayer); break;
                }
            }
        }
    }

    private void doMagic(ItemStack itemStack, EntityPlayer player) {
        if (itemStack.getItem() instanceof EcItemSword) {
            EcItemSword.doMagic(itemStack, player.worldObj, player);
        } else if (itemStack.getItem() instanceof ItemMultiToolHolder) {
            //ツールホルダーとの連携処理。
            ItemMultiToolHolder mth = (ItemMultiToolHolder) player.inventory.getCurrentItem().getItem();
            if (mth.getInventoryFromItemStack(itemStack).getStackInSlot(ItemMultiToolHolder.getSlotNumFromItemStack(itemStack)) != null
                    && mth.getInventoryFromItemStack(itemStack).getStackInSlot(ItemMultiToolHolder.getSlotNumFromItemStack(itemStack)).getItem() instanceof EcItemSword)
            {
                EcItemSword.doMagic(mth.getInventoryFromItemStack(itemStack).getStackInSlot(ItemMultiToolHolder.getSlotNumFromItemStack(itemStack)), player.worldObj, player);
            }
        }
    }
}