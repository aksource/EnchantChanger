package ak.EnchantChanger;

import net.minecraft.entity.player.EntityPlayer;
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
    	for(int i=0;i < this.LimitBreakFlag.length;i++)
    	{
	    	if(this.LimitBreakCount[i] <= 0)
	    	{
	    		if(this.LimitBreakFlag[i])
	    		{
	    			player.addChatMessage(new ChatComponentTranslation("LIMIT BREAK FINISH.", new Object[]{}));
	    			player.clearActivePotions();
	    			for (int k=0;k<33;k++)
	        		{
	    				player.removePotionEffect(k);
	        		}
	    		}
	    		this.LimitBreakFlag[i]=false;
	    		this.LimitBreakCount[i] = 0;
	    	}
	    	else
	    	{
	    		this.LimitBreakCount[i]--;
	    	}
    	}
    }
    private void LimitBreakCoolDown()
    {
    	for(int i=0;i < this.LimitBreakFlag.length;i++)
    	{
    		if(this.LimitBreakCoolDownCount[i]>0)
    		{
    			this.LimitBreakCoolDownCount[i]--;
    		}
    	}
    }
}