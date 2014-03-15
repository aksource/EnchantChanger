package ak.EnchantChanger;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by A.K. on 14/03/11.
 */
public class EcTileEntityMakoReactor extends EcTileMultiPass{
    public byte face;
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("face", face);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        face = nbt.getByte("face");
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
    }
}
