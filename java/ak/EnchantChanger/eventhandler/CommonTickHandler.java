package ak.EnchantChanger.eventhandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;


public class CommonTickHandler
{
    public static int[] LimitBreakCount = new int[]{0,0,0};
    public static int[] LimitBreakCoolDownCount = new int[]{0,0,0};
    public static boolean[] LimitBreakFlag = new boolean[]{false,false,false};
    @SubscribeEvent
    public void playerTick(PlayerTickEvent event)
    {
    	EntityPlayer player = event.player;
    	if(player != null)
    	{
    		this.LimitBreak(player);
    		this.LimitBreakCoolDown();
    	}
    }

    private void LimitBreak(EntityPlayer player)
    {
    	for(int i=0;i < LimitBreakFlag.length;i++)
    	{
	    	if(LimitBreakCount[i] <= 0)
	    	{
	    		if(LimitBreakFlag[i])
	    		{
	    			player.addChatMessage(new ChatComponentText("LIMIT BREAK FINISH."));
	    			player.clearActivePotions();
	    			for (int k=0;k<33;k++)
	        		{
	    				player.removePotionEffect(k);
	        		}
	    		}
	    		LimitBreakFlag[i]=false;
	    		LimitBreakCount[i] = 0;
	    	}
	    	else
	    	{
	    		LimitBreakCount[i]--;
	    	}
    	}
    }
    private void LimitBreakCoolDown()
    {
    	for(int i=0;i < LimitBreakFlag.length;i++)
    	{
    		if(LimitBreakCoolDownCount[i]>0)
    		{
    			LimitBreakCoolDownCount[i]--;
    		}
    	}
    }
}