package ak.VillagerTweaks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class VillagerInteractHook
{
	private Random rand = new Random();
	@SubscribeEvent
	public void interactEvent(EntityInteractEvent event)
	{
		ItemStack item = event.entityPlayer.getCurrentEquippedItem();
		if(event.target instanceof EntityVillager && item != null)
		{
			EntityVillager vil = (EntityVillager) event.target;
			if(getUniqueStrings(item.getItem()).equals(VillagerTweaks.changeTradeItem))
			{
				changeVillagerTrade(vil, item);
				event.setCanceled(true);
			}
			else if(getUniqueStrings(item.getItem()).equals(VillagerTweaks.changeProfessionItem))
			{
				changeVillagerProfession(vil, item);
				event.setCanceled(true);
			}
//			else if(item.itemID == VillagerTweaks.setMatingItem)
//			{
//				setVillagerMating(vil,item);
//				event.setCanceled(true);
//			}
		}
	}
	private void changeVillagerTrade(EntityVillager vil, ItemStack changeItem)
	{
		ObfuscationReflectionHelper.setPrivateValue(EntityVillager.class, vil, null, 5);
		changeItem.stackSize--;
	}
	private void changeVillagerProfession(EntityVillager vil, ItemStack changeItem)
	{
		int extra = VillagerRegistry.getRegisteredVillagers().size();
        int trade = rand.nextInt(5 + extra);
        vil.setProfession(trade < 5 ? trade : ((List<Integer>)VillagerRegistry.getRegisteredVillagers()).get(trade - 5));
		ObfuscationReflectionHelper.setPrivateValue(EntityVillager.class, vil, null, 5);
		changeItem.stackSize--;
	}
	private void setVillagerMating(EntityVillager vil, ItemStack setItem)
	{
		setItem.stackSize--;
	}
	public static String getUniqueStrings(Object obj)
	{
		UniqueIdentifier uId;
		if(obj instanceof Block) {
			uId = GameRegistry.findUniqueIdentifierFor((Block) obj);
		}else {
			uId = GameRegistry.findUniqueIdentifierFor((Item) obj);
		}
		return uId.modId + ":" + uId.name;

	}
}