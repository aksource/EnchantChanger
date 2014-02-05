package ModItemName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(modid="ModifierItemName", name="ModifierItemName", version="1.0-universal",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)

public class ModifierItemName
{
	@Mod.Instance("ModifierItemName")
	public static ModifierItemName instance;
	public static HashMap<Integer, List> modStringMap = new HashMap();
	public static HashMap<Integer, List> modStringJpMap = new HashMap();
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		
	}
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new PickUpEventHooks());
		this.addModName();
	}
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
//		this.checkMap();
	}
	public void addModName()
	{
		List<String> list0 = new ArrayList();
		List<String> listJp0 = new ArrayList();
		listJp0.add("鉄壁な");
		listJp0.add("堅固な");
		listJp0.add("頑丈な");
		list0.add("hard");
		list0.add("impregnable");
		list0.add("strong");
		modStringJpMap.put(Enchantment.protection.effectId, listJp0);
		modStringMap.put(Enchantment.protection.effectId, list0);
		List<String> list1 = new ArrayList();
		List<String> listJp1 = new ArrayList();
		listJp1.add("防炎処理された");
		listJp1.add("炎を防ぎし");
		list1.add("fireproof");
		modStringJpMap.put(Enchantment.fireProtection.effectId, listJp1);
		modStringMap.put(Enchantment.fireProtection.effectId, list1);
		List<String> list2 = new ArrayList();
		List<String> listJp2 = new ArrayList();
		listJp2.add("羽を持つ");
		listJp2.add("体が軽くなる");
		list2.add("feather");
		modStringJpMap.put(Enchantment.featherFalling.effectId, listJp2);
		modStringMap.put(Enchantment.featherFalling.effectId, list2);
		List<String> list3 = new ArrayList();
		List<String> listJp3 = new ArrayList();
		listJp3.add("耐爆処理された");
		listJp3.add("ボマー用の");
		list3.add("explosion proof");
		modStringJpMap.put(Enchantment.blastProtection.effectId, listJp3);
		modStringMap.put(Enchantment.blastProtection.effectId, list3);
		List<String> list4 = new ArrayList();
		List<String> listJp4 = new ArrayList();
		listJp4.add("防弾処理された");
		listJp4.add("飛び道具に強い");
		list4.add("bulletproof");
		modStringJpMap.put(Enchantment.projectileProtection.effectId, listJp4);
		modStringMap.put(Enchantment.projectileProtection.effectId, list4);
		List<String> list5 = new ArrayList();
		List<String> listJp5 = new ArrayList();
		listJp5.add("呼吸補助の付いた");
		listJp5.add("息が長く続く");
		list5.add("breath help");
		modStringJpMap.put(Enchantment.respiration.effectId, listJp5);
		modStringMap.put(Enchantment.respiration.effectId, list5);
		List<String> list6 = new ArrayList();
		List<String> listJp6 = new ArrayList();
		listJp6.add("水中作業可能な");
		list6.add("watarworkable");
		modStringJpMap.put(Enchantment.aquaAffinity.effectId, listJp6);
		modStringMap.put(Enchantment.aquaAffinity.effectId, list6);
		List<String> list7 = new ArrayList();
		List<String> listJp7 = new ArrayList();
		listJp7.add("棘のある");
		listJp7.add("自動反撃する");
		list7.add("thorn-added");
		modStringJpMap.put(Enchantment.thorns.effectId, listJp7);
		modStringMap.put(Enchantment.thorns.effectId, list7);
		List<String> list16 = new ArrayList();
		List<String> listJp16 = new ArrayList();
		listJp16.add("鋭利な");
		listJp16.add("よく研がれた");
		list16.add("sharp");
		modStringJpMap.put(Enchantment.sharpness.effectId, listJp16);
		modStringMap.put(Enchantment.sharpness.effectId, list16);
		List<String> list17 = new ArrayList();
		List<String> listJp17 = new ArrayList();
		listJp17.add("死者殺しと言われる");
		list17.add("undead-killer");
		modStringJpMap.put(Enchantment.smite.effectId, listJp17);
		modStringMap.put(Enchantment.smite.effectId, list17);
		List<String> list18 = new ArrayList();
		List<String> listJp18 = new ArrayList();
		listJp18.add("虫狩りと呼ばれる");
		list18.add("arthropods-killer");
		modStringJpMap.put(Enchantment.baneOfArthropods.effectId, listJp18);
		modStringMap.put(Enchantment.baneOfArthropods.effectId, list18);
		List<String> list19 = new ArrayList();
		List<String> listJp19 = new ArrayList();
		listJp19.add("飛ばしに使われる");
		list19.add("knockback");
		modStringJpMap.put(Enchantment.knockback.effectId, listJp19);
		modStringMap.put(Enchantment.knockback.effectId, list19);
		List<String> list20 = new ArrayList();
		List<String> listJp20 = new ArrayList();
		listJp20.add("炎を纏いし");
		list20.add("flamed");
		modStringJpMap.put(Enchantment.fireAspect.effectId, listJp20);
		modStringMap.put(Enchantment.fireAspect.effectId, list20);
		List<String> list21 = new ArrayList();
		List<String> listJp21 = new ArrayList();
		listJp21.add("分捕りに最適な");
		list21.add("looting");
		modStringJpMap.put(Enchantment.looting.effectId, listJp21);
		modStringMap.put(Enchantment.looting.effectId, list21);
		List<String> list32 = new ArrayList();
		List<String> listJp32 = new ArrayList();
		listJp32.add("効率的な");
		listJp32.add("時間節約になる");
		list32.add("efficiently workable");
		modStringJpMap.put(Enchantment.efficiency.effectId, listJp32);
		modStringMap.put(Enchantment.efficiency.effectId, list32);
		List<String> list33 = new ArrayList();
		List<String> listJp33 = new ArrayList();
		listJp33.add("そのまま採れる");
		list33.add("shape-keeping");
		modStringJpMap.put(Enchantment.silkTouch.effectId, listJp33);
		modStringMap.put(Enchantment.silkTouch.effectId, list33);
		List<String> list34 = new ArrayList();
		List<String> listJp34 = new ArrayList();
		listJp34.add("壊れにくい");
		list34.add("unbreaking");
		modStringJpMap.put(Enchantment.unbreaking.effectId, listJp34);
		modStringMap.put(Enchantment.unbreaking.effectId, list34);
		List<String> list35 = new ArrayList();
		List<String> listJp35 = new ArrayList();
		listJp35.add("幸運になる");
		list35.add("fortune");
		modStringJpMap.put(Enchantment.fortune.effectId, listJp35);
		modStringMap.put(Enchantment.fortune.effectId, list35);
		List<String> list48 = new ArrayList();
		List<String> listJp48 = new ArrayList();
		listJp48.add("強弓と自慢できる");
		list48.add("power shootable");
		modStringJpMap.put(Enchantment.power.effectId, listJp48);
		modStringMap.put(Enchantment.power.effectId, list48);
		List<String> list49 = new ArrayList();
		List<String> listJp49 = new ArrayList();
		listJp49.add("吹き飛ばせる");
		list49.add("arrow-punching");
		modStringJpMap.put(Enchantment.punch.effectId, listJp49);
		modStringMap.put(Enchantment.punch.effectId, list49);
		List<String> list50 = new ArrayList();
		List<String> listJp50 = new ArrayList();
		listJp50.add("炎を帯びし矢を放つ");
		list50.add("flamed arrow shootable");
		modStringJpMap.put(Enchantment.flame.effectId, listJp50);
		modStringMap.put(Enchantment.flame.effectId, list50);
		List<String> list51 = new ArrayList();
		List<String> listJp51 = new ArrayList();
		listJp51.add("撃ち放題な");
		list51.add("infinite arrow");
		modStringJpMap.put(Enchantment.infinity.effectId, listJp51);
		modStringMap.put(Enchantment.infinity.effectId, list51);
		List<String> list61 = new ArrayList();
		List<String> listJp61 = new ArrayList();
		listJp61.add("良いものが釣れる");
		list61.add("good-fishing");
		modStringJpMap.put(Enchantment.field_151370_z.effectId, listJp61);
		modStringMap.put(Enchantment.field_151370_z.effectId, list61);
		List<String> list62 = new ArrayList();
		List<String> listJp62 = new ArrayList();
		listJp62.add("釣りやすい");
		list62.add("easy-fishing");
		modStringJpMap.put(Enchantment.field_151369_A.effectId, listJp61);
		modStringMap.put(Enchantment.field_151369_A.effectId, list61);
	}
	public void checkMap()
	{
		String mn = null;
		List list;
		int lv;
		int size;
		for(int i = 0;i<Enchantment.enchantmentsList.length;i++)
		{
			if(Enchantment.enchantmentsList[i] != null)
			{
				System.out.println(i);
				list = ModifierItemName.modStringJpMap.get(i);
				if(list != null)
				{
					size = list.size();
					System.out.println(size);
				}
//				mn = (String) ((List)ModifierItemName.modStringJpMap.get(i)).get(rand.nextInt(size));
//				if(mn != null)
//					System.out.println(mn);
			}
		}
	}
	public class PickUpEventHooks
	{
		private Random rand = new Random();
		@SubscribeEvent
		public void PickUp(EntityItemPickupEvent event)
		{
			if(!event.item.getEntityItem().hasDisplayName() && event.item.getEntityItem().isItemEnchanted())
			{
				String str = "";
				String mn = null;
				List list;
				int lv;
				int size;
				for(int i = 0;i<Enchantment.enchantmentsList.length;i++)
				{
					if(Enchantment.enchantmentsList[i] != null && EnchantmentHelper.getEnchantmentLevel(i, event.item.getEntityItem()) > 0)
					{
						list = ModifierItemName.modStringJpMap.get(i);
						if(list != null)
						{
							size = list.size();
							System.out.println(size);
							mn = (String) ((List)ModifierItemName.modStringJpMap.get(i)).get(rand.nextInt(size));
						}
						else{
							mn = "不思議な";
						}
						if(mn != null)
							str = str + mn;
					}
				}
				str = str + event.item.getEntityItem().getDisplayName();
				event.item.getEntityItem().setStackDisplayName(str);
			}
		}
	}
}