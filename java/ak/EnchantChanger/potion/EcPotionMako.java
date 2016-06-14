package ak.EnchantChanger.potion;

import ak.EnchantChanger.Client.ClientProxy;
import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.utils.ConfigurationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

/**
 * Created by A.K. on 14/03/10.
 */
public class EcPotionMako extends Potion {
    private static final ResourceLocation newEffect = new ResourceLocation(Constants.EcAssetsDomain, Constants.EcPotionEffect);

    public EcPotionMako(int id) {
        super(id, true, 0x12f9c7);
        this.setIconIndex(0, 0);
    }

    @Override
    public int getStatusIconIndex() {
        ClientProxy.mc.getTextureManager().bindTexture(newEffect);
        return super.getStatusIconIndex();
    }

    @Override
    public void performEffect(EntityLivingBase par1EntityLivingBase, int amplifier) {
        par1EntityLivingBase.attackEntityFrom(EnchantChanger.damageSourceMako, 1.0F);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        int k;
        if (this.id == ConfigurationUtils.idMakoPoison) {
            k = 40 >> amplifier;
            return k <= 0 || duration % k == 0;
        } else return super.isReady(duration, amplifier);
    }

    @Override
    public boolean isUsable() {
        return false;
    }
}
