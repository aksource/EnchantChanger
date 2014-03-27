package ak.EnchantChanger;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

/**
 * Created by A.K. on 14/03/10.
 */
public class EcPotionMako extends Potion {
    private static final ResourceLocation newEffect = new ResourceLocation(EnchantChanger.EcAssetsDomain, EnchantChanger.EcPotionEffect);
    public EcPotionMako(int id) {
        super(id, true, 0x12f9c7);
        this.setIconIndex(0,0);
    }

    @Override
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(newEffect);
        return super.getStatusIconIndex();
    }

    @Override
    public void performEffect(EntityLivingBase par1EntityLivingBase, int amplifier) {
        par1EntityLivingBase.attackEntityFrom(EnchantChanger.damageSourceMako, 1.0F);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        int k;
        if (this.id == EnchantChanger.idMakoPoison) {
            k = 40 >> amplifier;
            return k > 0 ? duration % k == 0 : true;
        } else return super.isReady(duration, amplifier);
    }

    @Override
    public boolean isUsable() {
        return false;
    }
}