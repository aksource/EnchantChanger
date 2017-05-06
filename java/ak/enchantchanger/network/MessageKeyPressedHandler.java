package ak.enchantchanger.network;

import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.ExtendedPlayerData;
import ak.enchantchanger.api.Constants;
import ak.enchantchanger.item.EcItemSword;
import ak.enchantchanger.utils.ConfigurationUtils;
import ak.MultiToolHolders.ItemMultiToolHolder;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;

import static ak.enchantchanger.api.Constants.*;

/**
 * Created by A.K. on 14/07/31.
 */
public class MessageKeyPressedHandler implements IMessageHandler<MessageKeyPressed, IMessage> {
    @Override
    public IMessage onMessage(MessageKeyPressed message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        if (player != null && player.getCurrentEquippedItem() != null && message.keyIndex != -1) {
            switch (message.keyIndex) {
                case MagicKEY:
                    doMagic(player.getCurrentEquippedItem(), player);
                    break;
                case MateriaKEY:
                    openMateriaWindow(player);
                    break;
                case CtrlKEY:
                    doCtrlKeyAction(player.getCurrentEquippedItem(), player);
                    break;
            }
        }
        return null;
    }

    private void doMagic(ItemStack itemStack, EntityPlayer player) {
        if (itemStack.getItem() instanceof EcItemSword) {
            EcItemSword.doMagic(itemStack, player.worldObj, player);
        } else if (EnchantChanger.loadMTH && itemStack.getItem() instanceof ItemMultiToolHolder) {
            //ツールホルダーとの連携処理。
            ItemMultiToolHolder mth = (ItemMultiToolHolder) itemStack.getItem();
            if (mth.getInventoryFromItemStack(itemStack).getStackInSlot(ItemMultiToolHolder.getSlotNumFromItemStack(itemStack)) != null
                    && mth.getInventoryFromItemStack(itemStack).getStackInSlot(ItemMultiToolHolder.getSlotNumFromItemStack(itemStack)).getItem() instanceof EcItemSword) {
                EcItemSword.doMagic(mth.getInventoryFromItemStack(itemStack).getStackInSlot(ItemMultiToolHolder.getSlotNumFromItemStack(itemStack)), player.worldObj, player);
            }
        }
    }

    private void openMateriaWindow(EntityPlayer player) {
        if (canOpenMateriaWindow(player)) {
            player.openGui(EnchantChanger.instance, Constants.GUI_ID_MATERIA_WINDOW, player.worldObj, MathHelper.ceiling_double_int(player.posX), MathHelper.ceiling_double_int(player.posY), MathHelper.ceiling_double_int(player.posZ));
        }
    }

    private boolean canOpenMateriaWindow(EntityPlayer player) {
        return ExtendedPlayerData.get(player).getSoldierMode() && (ConfigurationUtils.difficulty < 2 || checkCost(player));
    }

    private void doCtrlKeyAction(ItemStack itemStack, EntityPlayer player) {
        if (itemStack.getItem() instanceof EcItemSword) {
            ((EcItemSword) itemStack.getItem()).doCtrlKeyAction(itemStack, player.worldObj, player);
        }
    }

    private boolean checkCost(EntityPlayer player) {
        int expLv = player.experienceLevel;
        if (expLv >= ConfigurationUtils.enchantChangerCost) {
            player.addExperienceLevel(-ConfigurationUtils.enchantChangerCost);
            return true;
        }
        player.addChatComponentMessage(new ChatComponentText(String.format("Need %dLevel to open materia window", ConfigurationUtils.enchantChangerCost)));
        return false;
    }
}
