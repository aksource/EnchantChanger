package ak.EnchantChanger.Client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.input.Keyboard;

import ak.EnchantChanger.CommonProxy;
import ak.EnchantChanger.EcEntityApOrb;
import ak.EnchantChanger.EcEntityExExpBottle;
import ak.EnchantChanger.EcEntityMeteo;
import ak.EnchantChanger.EcTileEntityHugeMateria;
import ak.EnchantChanger.EnchantChanger;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	public static KeyBinding MagicKey = new KeyBinding("Key.EcMagic",
			Keyboard.KEY_V, "EnchantChanger:KeyMagic");

	@Override
	public void registerRenderInformation() {
		RenderingRegistry.registerEntityRenderingHandler(
				EcEntityExExpBottle.class, new EcRenderItemThrowable(0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EcEntityMeteo.class,
				new EcRenderItemThrowable(EnchantChanger.MeteoSize));
		RenderingRegistry.registerEntityRenderingHandler(EcEntityApOrb.class,
				new EcRenderApOrb());
//		TickRegistry.registerTickHandler(new CommonTickHandler(), Side.SERVER);
		// TickRegistry.registerTickHandler(new ClientTickHandler(),
		// Side.CLIENT);
		ClientRegistry.registerKeyBinding(MagicKey);
		IItemRenderer swordRenderer = new EcSwordRenderer();
		MinecraftForgeClient.registerItemRenderer(
				EnchantChanger.ItemSephirothSword,
				swordRenderer);
		MinecraftForgeClient.registerItemRenderer(
				EnchantChanger.ItemZackSword,
				swordRenderer);
		MinecraftForgeClient.registerItemRenderer(
				EnchantChanger.ItemCloudSwordCore,
				swordRenderer);
		MinecraftForgeClient.registerItemRenderer(
				EnchantChanger.ItemCloudSword,
				swordRenderer);
		MinecraftForgeClient.registerItemRenderer(
				EnchantChanger.ItemUltimateWeapon,
				swordRenderer);
		MinecraftForgeClient.registerItemRenderer(
				EnchantChanger.ItemImitateSephirothSword,
				swordRenderer);
		IItemRenderer materiaRenderer = new EcRenderMateria();
		MinecraftForgeClient.registerItemRenderer(EnchantChanger.ItemMat,
				materiaRenderer);
		MinecraftForgeClient.registerItemRenderer(
				EnchantChanger.MasterMateria, materiaRenderer);
	}

	@Override
	public void registerTileEntitySpecialRenderer() {
		ClientRegistry.bindTileEntitySpecialRenderer(
				EcTileEntityHugeMateria.class, new EcRenderHugeMateria());
	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}
}