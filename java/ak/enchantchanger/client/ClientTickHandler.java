package ak.enchantchanger.client;

public class ClientTickHandler {
    //下のメソッドの処理はClientProxyで行われている。
//	@SubscribeEvent
//    public void onPlayerTick(PlayerTickEvent event)
//    {
//    	EntityPlayer player = event.player;
//    	World world = player.worldObj;
//    	if(!world.isRemote) return;
//    	Minecraft mc = Minecraft.getMinecraft();
//    	ItemStack ep = player.getCurrentEquippedItem();
//    	int reach = 10;
//    	if(ep != null && ep.getItem() instanceof EcItemSephirothSword){
//    		mc.objectMouseOver = EnchantChanger.getMouseOverCustom(player, world, reach);
//    		if(mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectType.ENTITY && mc.objectMouseOver.entityHit instanceof EntityLivingBase){
//    			mc.pointedEntity = mc.objectMouseOver.entityHit;
//    		}
//    	}
//    }

}