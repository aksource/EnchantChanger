package ak.enchantchanger.item;

import ak.enchantchanger.capability.CapabilityPlayerStatusHandler;
import ak.enchantchanger.capability.IPlayerStatusHandler;
import ak.enchantchanger.utils.StatCheckUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EcItemMaterializer extends EcItem {

    public EcItemMaterializer(String name) {
        super(name);
        maxStackSize = 1;
        setMaxDamage(0);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote) {
            IPlayerStatusHandler statusHandler = CapabilityPlayerStatusHandler.getPlayerStatusHandler(playerIn);
            boolean soldierFlg = !statusHandler.getSoldierMode();
            statusHandler.setSoldierMode(soldierFlg);
            playerIn.sendMessage(new TextComponentString("Materia Setting Mode : " + soldierFlg));
            statusHandler.setSoldierWorkStartTime(worldIn.getWorldTime());
            statusHandler.setMobKillCount(StatCheckUtils.getTotalMobKillCount(playerIn));
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
    }
}