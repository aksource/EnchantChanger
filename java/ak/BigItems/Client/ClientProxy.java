package ak.BigItems.Client;

import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import ak.BigItems.BigItems;
import ak.BigItems.CommonProxy;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.GameData;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerClientInformation()
	{
		Item item = null;
		for(int i= 0;i<BigItems.ItemIDs.length;i++){
			item = GameData.itemRegistry.getObject(BigItems.ItemIDs[i]);
			if(item != null)
				MinecraftForgeClient.registerItemRenderer(item, new BigItemRenderer());
			else
				System.out.println("error");
		}
	}
	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}
}