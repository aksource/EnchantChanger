package ak.EnchantChanger.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class EcCloudSwordData extends WorldSavedData {
    public ItemStack[] swords = new ItemStack[5];
    private boolean init = false;
    public boolean upDate;

    public EcCloudSwordData(String par1Str) {
        super(par1Str);
    }

    public void onUpdate(World var1, EntityPlayer var2) {
        if (!this.init) {
            this.init = true;
            this.markDirty();
        }
        if (var1.getWorldTime() % 80l == 0l)
            this.upDate = true;
        if (this.upDate) {
            this.markDirty();
            this.upDate = false;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound var1) {
        NBTTagList var2 = var1.getTagList("Items", 10);
        this.swords = new ItemStack[5];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.swords.length) {
                this.swords[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound var1) {
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.swords.length; ++var3) {
            if (this.swords[var3] != null) {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.swords[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        var1.setTag("Items", var2);
    }

}