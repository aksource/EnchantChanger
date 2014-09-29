package ak.EnchantChanger;

import ak.EnchantChanger.Client.gui.*;
import ak.EnchantChanger.inventory.*;
import ak.EnchantChanger.tileentity.EcTileEntityHugeMateria;
import ak.EnchantChanger.tileentity.EcTileEntityMakoReactor;
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

    public void registerRenderInformation() {
    }

    public void registerTileEntitySpecialRenderer() {
    }

    public EntityPlayer getPlayer() {return null;}

    public void doFlightOnSide(EntityPlayer player) {}

    //returns an instance of the Container you made earlier
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

        if (id == EnchantChanger.guiIdMaterializer) {
            return new EcContainerMaterializer(world, player.inventory);
        }
        if (id == EnchantChanger.guiIdPortableEnchantmentTable) {
            return new EcContainerPortableEnchantment(player.inventory, world, x, y, z);
        }
        if (id == EnchantChanger.guiIdHugeMateria) {
            TileEntity t = world.getTileEntity(x, y, z);
            if (t != null)
                return new EcContainerHugeMateria(player.inventory, (EcTileEntityHugeMateria) t);
        }
        if (id == EnchantChanger.guiIdMateriaWindow) {
            ItemStack heldItem = player.getCurrentEquippedItem();
            int slot = player.inventory.currentItem;
            if (heldItem != null) {
                return new EcContainerMateriaWindow(world, player.inventory, heldItem, slot);
            }
        }
        if (id == EnchantChanger.guiIdMakoReactor) {
            TileEntity t = world.getTileEntity(x, y, z);
            if (t != null)
                return new EcContainerMakoReactor(player.inventory, (EcTileEntityMakoReactor) t);
        }
        return null;
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == EnchantChanger.guiIdMaterializer) {
            return new EcGuiMaterializer(world, player.inventory);
        }
        if (id == EnchantChanger.guiIdPortableEnchantmentTable) {
            return new EcGuiPortableEnchantment(player.inventory, world, x, y, z);
        }
        if (id == EnchantChanger.guiIdHugeMateria) {
            TileEntity t = world.getTileEntity(x, y, z);
            if (t != null)
                return new EcGuiHugeMateria(player.inventory, (EcTileEntityHugeMateria) t);
        }
        if (id == EnchantChanger.guiIdMateriaWindow) {
            ItemStack heldItem = player.getCurrentEquippedItem();
            int slot = player.inventory.currentItem;
            if (heldItem != null) {
                return new EcGuiMateriaWindow(world, player.inventory, heldItem, slot);
            }
        }
        if (id == EnchantChanger.guiIdMakoReactor) {
            TileEntity t = world.getTileEntity(x, y, z);
            if (t != null)
                return new EcGuiMakoReactor(player.inventory, (EcTileEntityMakoReactor) t);
        }
        return null;
    }

    public static void storeEntityData(String name, NBTTagCompound compound)
    {
        extendedEntityData.put(name, compound);
    }

    public static NBTTagCompound getEntityData(String name)
    {
        return extendedEntityData.remove(name);
    }

}