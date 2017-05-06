package ak.EnchantChanger;

import ak.EnchantChanger.Client.gui.*;
import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.inventory.*;
import ak.EnchantChanger.tileentity.EcTileEntityHugeMateria;
import ak.EnchantChanger.tileentity.EcTileEntityMakoReactor;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class CommonProxy implements IGuiHandler {
    private static final Map<String, NBTTagCompound> extendedEntityData = new HashMap<>();
    public static int multiPassRenderType;

    public static void storeEntityData(String name, NBTTagCompound compound) {
        extendedEntityData.put(name, compound);
    }

    public static NBTTagCompound getEntityData(String name) {
        return extendedEntityData.remove(name);
    }

    public void registerRenderInformation() {
        multiPassRenderType = RenderingRegistry.getNextAvailableRenderId();
    }

    public void registerTileEntitySpecialRenderer() {}

    public void registerExtraMateriaRendering(NBTTagCompound nbt) {}

    public EntityPlayer getPlayer() {
        return null;
    }

    public void doFlightOnSide(EntityPlayer player) {}

    //returns an instance of the Container you made earlier
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

        if (id == Constants.GUI_ID_MATERIALIZER) {
            return new EcContainerMaterializer(world, player.inventory);
        }
//        if (id == Constants.GUI_ID_PORTABLE_ENCHANTMENT_TABLE) {
//            return new EcContainerPortableEnchantment(player.inventory, world, x, y, z);
//        }
        if (id == Constants.GUI_ID_HUGE_MATERIA) {
            TileEntity t = world.getTileEntity(x, y, z);
            if (t != null)
                return new EcContainerHugeMateria(player.inventory, (EcTileEntityHugeMateria) t);
        }
        if (id == Constants.GUI_ID_MATERIA_WINDOW) {
            ItemStack heldItem = player.getCurrentEquippedItem();
            int slot = player.inventory.currentItem;
            if (heldItem != null && heldItem.stackSize == 1) {
                return new EcContainerMateriaWindow(player.inventory, heldItem, slot);
            }
        }
        if (id == Constants.GUI_ID_MAKO_REACTOR) {
            TileEntity t = world.getTileEntity(x, y, z);
            if (t != null)
                return new EcContainerMakoReactor(player.inventory, (EcTileEntityMakoReactor) t);
        }
        return null;
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == Constants.GUI_ID_MATERIALIZER) {
            return new EcGuiMaterializer(world, player.inventory);
        }
//        if (id == Constants.GUI_ID_PORTABLE_ENCHANTMENT_TABLE) {
//            return new EcGuiPortableEnchantment(player.inventory, world, x, y, z);
//        }
        if (id == Constants.GUI_ID_HUGE_MATERIA) {
            TileEntity t = world.getTileEntity(x, y, z);
            if (t != null)
                return new EcGuiHugeMateria(player.inventory, (EcTileEntityHugeMateria) t);
        }
        if (id == Constants.GUI_ID_MATERIA_WINDOW) {
            ItemStack heldItem = player.getCurrentEquippedItem();
            int slot = player.inventory.currentItem;
            if (heldItem != null) {
                return new EcGuiMateriaWindow(player.inventory, heldItem, slot);
            }
        }
        if (id == Constants.GUI_ID_MAKO_REACTOR) {
            TileEntity t = world.getTileEntity(x, y, z);
            if (t != null) {
                if (EnchantChanger.loadRFAPI) {
                    return new EcGuiMakoReactorRF(player.inventory, (EcTileEntityMakoReactor) t);
                }
                if (EnchantChanger.loadSS) {
                    return new EcGuiMakoReactorGF(player.inventory, (EcTileEntityMakoReactor) t);
                }
                return new EcGuiMakoReactor(player.inventory, (EcTileEntityMakoReactor) t);
            }
        }
        return null;
    }

}