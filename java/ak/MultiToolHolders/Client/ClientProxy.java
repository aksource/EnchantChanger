package ak.MultiToolHolders.Client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.input.Keyboard;

import ak.MultiToolHolders.CommonProxy;
import ak.MultiToolHolders.MultiToolHolders;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
	public static KeyBinding OpenKey = new KeyBinding("Key.openToolHolder",Keyboard.KEY_F, "MTH:KeyOpen");
	public static KeyBinding NextKey = new KeyBinding("Key.nextToolHolder",Keyboard.KEY_T, "MTH:KeyNext");
	public static KeyBinding PrevKey = new KeyBinding("Key.prevToolHolder",Keyboard.KEY_R, "MTH:KeyPrevious");
	@Override
	public void registerClientInformation()
	{

		ClientRegistry.registerKeyBinding(OpenKey);
		ClientRegistry.registerKeyBinding(NextKey);
		ClientRegistry.registerKeyBinding(PrevKey);
		MinecraftForgeClient.registerItemRenderer(MultiToolHolders.ItemMultiToolHolder3, (IItemRenderer) MultiToolHolders.ItemMultiToolHolder3);
		MinecraftForgeClient.registerItemRenderer(MultiToolHolders.ItemMultiToolHolder5, (IItemRenderer) MultiToolHolders.ItemMultiToolHolder5);
		MinecraftForgeClient.registerItemRenderer(MultiToolHolders.ItemMultiToolHolder9, (IItemRenderer) MultiToolHolders.ItemMultiToolHolder9);
		MinecraftForgeClient.registerItemRenderer(MultiToolHolders.ItemMultiToolHolder7, (IItemRenderer) MultiToolHolders.ItemMultiToolHolder7);
	}

	@Override
	public void registerTileEntitySpecialRenderer()
	{
//		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPEnchantmentTable.class, new RenderPEnchantmentTable());
	}

	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}
}