package ak.enchantchanger.potion;

import ak.enchantchanger.client.ClientProxy;
import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.api.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * 魔晄中毒用ポーションクラス
 * Created by A.K. on 14/03/10.
 */
public class EcPotionMako extends Potion {
    private static final ResourceLocation NEW_EFFECT = new ResourceLocation(Constants.EcAssetsDomain, Constants.EcPotionEffect);

    public EcPotionMako() {
        super(true, 0x12f9c7);
        this.setIconIndex(0, 0);
        this.setRegistryName("mako");
    }

    @Override
    public int getStatusIconIndex() {
        ClientProxy.mc.getTextureManager().bindTexture(NEW_EFFECT);
        return super.getStatusIconIndex();
    }

    @Override
    public void performEffect(@Nonnull EntityLivingBase entityLivingBaseIn, int amplifier) {
        entityLivingBaseIn.attackEntityFrom(EnchantChanger.damageSourceMako, 1.0F);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        int k;
//        if (this.id == ConfigurationUtils.idMakoPoison) {
            k = 40 >> amplifier;
            return k <= 0 || duration % k == 0;
//        } else return super.isReady(duration, amplifier);
    }
}
