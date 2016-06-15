package ak.EnchantChanger.tileentity;

import ak.EnchantChanger.utils.StringUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * マルチパスレンダリングブロック用のTileEntityクラス
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
        return new S35PacketUpdateTileEntity(this.getPos(), 1, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    public Block getBaseBlock() {
        Block block = StringUtils.getBlockFromString(this.baseBlock);
        return (Blocks.air != block) ? block : Blocks.iron_block;
    }

    public int getBaseMeta() {
        return baseMeta;
    }

    public ItemStack getBaseBlockItemStack() {
        return new ItemStack(getBaseBlock(), 1, getBaseMeta());
    }
}
