package ak.enchantchanger.tileentity;

import ak.enchantchanger.utils.StringUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * マルチパスレンダリングブロック用のTileEntityクラス
 * Created by A.K. on 14/03/08.
 */
public class EcTileMultiPass extends TileEntity {
    public String baseBlock = "";
    public int baseMeta = 0;

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        baseBlock = nbt.getString("baseBlock");
        baseMeta = nbt.getInteger("baseMeta");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setString("baseBlock", baseBlock);
        nbtTagCompound.setInteger("baseMeta", baseMeta);
        return nbtTagCompound;
    }


    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.getPos(), 0, this.writeToNBT(new NBTTagCompound()));
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(@Nonnull NetworkManager net, @Nonnull SPacketUpdateTileEntity pkt) {
        if (pkt.getPos().equals(this.getPos())) {
            this.readFromNBT(pkt.getNbtCompound());
        }
    }


    public Block getBaseBlock() {
        Block block = StringUtils.getBlockFromString(this.baseBlock);
        return (Blocks.AIR != block) ? block : Blocks.IRON_BLOCK;
    }

    public int getBaseMeta() {
        return baseMeta;
    }

    public ItemStack getBaseBlockItemStack() {
        return new ItemStack(getBaseBlock(), 1, getBaseMeta());
    }
}
