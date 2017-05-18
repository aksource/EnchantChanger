package ak.enchantchanger;

import ak.enchantchanger.client.gui.EcGuiHugeMateria;
import ak.enchantchanger.client.gui.EcGuiMakoReactor;
import ak.enchantchanger.client.gui.EcGuiMateriaWindow;
import ak.enchantchanger.client.gui.EcGuiMaterializer;
import ak.enchantchanger.api.Constants;
import ak.enchantchanger.inventory.EcContainerHugeMateria;
import ak.enchantchanger.inventory.EcContainerMakoReactor;
import ak.enchantchanger.inventory.EcContainerMateriaWindow;
import ak.enchantchanger.inventory.EcContainerMaterializer;
import ak.enchantchanger.tileentity.EcTileEntityHugeMateria;
import ak.enchantchanger.tileentity.EcTileEntityMakoReactor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {

    public void registerPreRenderInformation() {}

    public void registerRenderInformation() {}

    public void registerTileEntitySpecialRenderer() {}

    public void registerExtraMateriaRendering(NBTTagCompound nbt) {}

    public EntityPlayer getPlayer() {
        return null;
    }

    public void doFlightOnSide(EntityPlayer player) {}

    //returns an instance of the Container you made earlier
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        if (id == Constants.GUI_ID_MATERIALIZER) {
            return new EcContainerMaterializer(world, player.inventory);
        }
//        if (id == Constants.GUI_ID_PORTABLE_ENCHANTMENT_TABLE) {
//            return new EcContainerPortableEnchantment(player.inventory, world, x, y, z);
//        }
        if (id == Constants.GUI_ID_HUGE_MATERIA) {
            TileEntity t = world.getTileEntity(blockPos);
            if (t != null)
                return new EcContainerHugeMateria(player.inventory, (EcTileEntityHugeMateria) t);
        }
        if (id == Constants.GUI_ID_MATERIA_WINDOW) {
            ItemStack heldItem = player.getHeldItemMainhand();
            int slot = player.inventory.currentItem;
            if (!heldItem.isEmpty()) {
                return new EcContainerMateriaWindow(player.inventory, heldItem, slot);
            }
        }
        if (id == Constants.GUI_ID_MAKO_REACTOR) {
            TileEntity t = world.getTileEntity(blockPos);
            if (t != null)
                return new EcContainerMakoReactor(player.inventory, (EcTileEntityMakoReactor) t);
        }
        return null;
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        if (id == Constants.GUI_ID_MATERIALIZER) {
            return new EcGuiMaterializer(world, player.inventory);
        }
//        if (id == Constants.GUI_ID_PORTABLE_ENCHANTMENT_TABLE) {
//            return new EcGuiPortableEnchantment(player.inventory, world, x, y, z);
//        }
        if (id == Constants.GUI_ID_HUGE_MATERIA) {
            TileEntity t = world.getTileEntity(blockPos);
            if (t != null)
                return new EcGuiHugeMateria(player.inventory, (EcTileEntityHugeMateria) t);
        }
        if (id == Constants.GUI_ID_MATERIA_WINDOW) {
            ItemStack heldItem = player.getHeldItemMainhand();
            int slot = player.inventory.currentItem;
            if (!heldItem.isEmpty()) {
                return new EcGuiMateriaWindow(player.inventory, heldItem, slot);
            }
        }
        if (id == Constants.GUI_ID_MAKO_REACTOR) {
            TileEntity t = world.getTileEntity(blockPos);
            if (t != null) {
//                if (enchantchanger.loadTE) {
//                    return new EcGuiMakoReactorRF(player.inventory, (EcTileEntityMakoReactor)t);
//                }
//                if (enchantchanger.loadSS) {
//                    return new EcGuiMakoReactorGF(player.inventory, (EcTileEntityMakoReactor)t);
//                }
                return new EcGuiMakoReactor(player.inventory, (EcTileEntityMakoReactor) t);
            }
        }
        return null;
    }
}