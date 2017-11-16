package ak.enchantchanger.network;

import ak.MultiToolHolders.ItemMultiToolHolder;
import ak.MultiToolHolders.inventory.InventoryToolHolder;
import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.api.Constants;
import ak.enchantchanger.capability.CapabilityPlayerStatusHandler;
import ak.enchantchanger.item.EcItemSword;
import ak.enchantchanger.utils.ConfigurationUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static ak.enchantchanger.api.Constants.*;

/**
 * キー押下時に投げるMessageのHandlerクラス
 * Created by A.K. on 14/07/31.
 */
public class MessageKeyPressedHandler implements IMessageHandler<MessageKeyPressed, IMessage> {
    @Override
    public IMessage onMessage(MessageKeyPressed message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().player;
        if (player != null && !player.getHeldItemMainhand().isEmpty() && message.keyIndex != -1) {
            switch (message.keyIndex) {
                case MagicKEY:
                    doMagic(player.getHeldItemMainhand(), player);
                    break;
                case MateriaKEY:
                    openMateriaWindow(player);
                    break;
                case CtrlKEY:
                    doCtrlKeyAction(player.getHeldItemMainhand(), player);
                    break;
            }
        }
        return null;
    }

    private void doMagic(ItemStack itemStack, EntityPlayer player) {
        if (itemStack.getItem() instanceof EcItemSword) {
            EcItemSword.doMagic(itemStack, player.world, player);
        } else if (EnchantChanger.loadMTH && itemStack.getItem() instanceof ItemMultiToolHolder) {
            //ツールホルダーとの連携処理。
            ItemMultiToolHolder mth = (ItemMultiToolHolder) itemStack.getItem();
            InventoryToolHolder inventoryToolHolder = mth.getInventoryFromItemStack(itemStack);
            int slot = ItemMultiToolHolder.getSlotNumFromItemStack(itemStack);
            if (!inventoryToolHolder.getStackInSlot(slot).isEmpty()
                    && inventoryToolHolder.getStackInSlot(slot).getItem() instanceof EcItemSword) {
                EcItemSword.doMagic(inventoryToolHolder.getStackInSlot(slot), player.world, player);
            }
        }
    }

    private void openMateriaWindow(EntityPlayer player) {
        if (canOpenMateriaWindow(player)) {
            player.openGui(EnchantChanger.instance, Constants.GUI_ID_MATERIA_WINDOW, player.world, MathHelper.ceil(player.posX), MathHelper.ceil(player.posY), MathHelper.ceil(player.posZ));
        }
    }

    private boolean canOpenMateriaWindow(EntityPlayer player) {
        return CapabilityPlayerStatusHandler.getPlayerStatusHandler(player).getSoldierMode()
                && (ConfigurationUtils.difficulty < 2 || checkCost(player));
    }

    private void doCtrlKeyAction(ItemStack itemStack, EntityPlayer player) {
        if (itemStack.getItem() instanceof EcItemSword) {
            ((EcItemSword) itemStack.getItem()).doCtrlKeyAction(itemStack, player.world, player);
        }
    }

    private boolean checkCost(EntityPlayer player) {
        int expLv = player.experienceLevel;
        if (expLv >= ConfigurationUtils.enchantChangerCost) {
            player.addExperienceLevel(-ConfigurationUtils.enchantChangerCost);
            return true;
        }
        player.sendMessage(new TextComponentString(String.format("Need %dLevel to open materia window", ConfigurationUtils.enchantChangerCost)));
        return false;
    }
}
