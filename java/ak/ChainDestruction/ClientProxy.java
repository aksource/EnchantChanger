package ak.ChainDestruction;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
	public static KeyBinding registItemKey = new KeyBinding("Key.CDRegistItem",Keyboard.KEY_K, "CD:KeyRegItem");
	public static KeyBinding digUnderKey = new KeyBinding("Key.CDDIgUnder",Keyboard.KEY_U, "CD:KeyDigUnder");
	@Override
	public void registerClientInfo(){
		ClientRegistry.registerKeyBinding(registItemKey);
		ClientRegistry.registerKeyBinding(digUnderKey);
	}
}