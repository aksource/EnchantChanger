package ak.EnchantChanger.tileentity;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by A.K. on 14/03/08.
 */
public class EcTileMultiPass extends TileEntity {
    public String baseBlock = "";
    public int baseMeta = 0;

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        baseBlock = nbt.getString("baseBlock");
        baseMeta = nbt.getInteger("baseMeta");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setString("baseBlock", baseBlock);
        nbt.setInteger("baseMeta", baseMeta);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeToNBT(nbtTagCompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    public Block getBaseBlock() {
        Block block;
        String[] strings = baseBlock.split(":");
        if (strings.length > 1) {
            block = GameRegistry.findBlock(strings[0], strings[1]);
        } else {
            block = GameRegistry.findBlock("minecraft", strings[0]);
        }
        if (block == null) {
            throw new IllegalArgumentException(String.format("[EnchantChanger]EcTileMultiPass does not proper baseblock coordinates x:%d, y:%d, z:%d", this.xCoord, this.yCoord, this.zCoord));
        }
        return block;
    }

    public int getBaseMeta() {
        return baseMeta;
    }

    public ItemStack getBaseBlockItemStack() {
        return new ItemStack(getBaseBlock(), 1, getBaseMeta());
    }
}
