package ak.EnchantChanger.Client;

import ak.EnchantChanger.*;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {
	public static KeyBinding MagicKey = new KeyBinding("Key.EcMagic",
			Keyboard.KEY_V, "EnchantChanger");
    public static KeyBinding MateriaKey = new KeyBinding("Key.EcMateria", Keyboard.KEY_R, "EnchantChanger");
    public static int customRenderPass;
    public static int multiPassRenderType;
    public static EcRenderMultiPassBlock ecRenderMultiPassBlock = new EcRenderMultiPassBlock();
	@Override
	public void registerRenderInformation() {
		RenderingRegistry.registerEntityRenderingHandler(
				EcEntityExExpBottle.class, new EcRenderItemThrowable(0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EcEntityMeteo.class,
				new EcRenderItemThrowable(EnchantChanger.sizeMeteo));
		RenderingRegistry.registerEntityRenderingHandler(EcEntityApOrb.class,
				new EcRenderApOrb());
        multiPassRenderType = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(ecRenderMultiPassBlock);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(EnchantChanger.blockMakoReactor), ecRenderMultiPassBlock);

		ClientRegistry.registerKeyBinding(MagicKey);
        ClientRegistry.registerKeyBinding(MateriaKey);
		IItemRenderer swordRenderer = new EcSwordRenderer();
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
}