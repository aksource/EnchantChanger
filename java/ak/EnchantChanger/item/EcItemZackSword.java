package ak.EnchantChanger.item;

public class EcItemZackSword extends EcItemSword
{
	public EcItemZackSword(String name)
	{
		super(ToolMaterial.IRON, name);
	}
//	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer player)
//	{
//		if(player.isSneaking() && CommonTickHandler.LimitBreakCoolDownCount[0] == 0&&(player.getHealth() < 3 || player.capabilities.isCreativeMode)){
//			CommonTickHandler.LimitBreakFlag[0]=true;
//			CommonTickHandler.LimitBreakCount[0]=20*15;
//			CommonTickHandler.LimitBreakCoolDownCount[0]=20*60*3;
//			player.addPotionEffect(new PotionEffect(3,CommonTickHandler.LimitBreakCount[0],3));
//			player.addChatMessage(new ChatComponentText("LIMIT BREAK!!"));
//			return par1ItemStack;
//		}
//		player.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
//		return par1ItemStack;
//	}
}
