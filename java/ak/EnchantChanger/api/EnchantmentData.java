package ak.EnchantChanger.api;

import net.minecraft.enchantment.Enchantment;

/**
 * Created by A.K. on 14/02/27.
 */
public class EnchantmentData {
    public Enchantment enchantment;
    public int lv;
    public EnchantmentData(Enchantment enchantmentId, int enchantmentLv) {
        this.enchantment = enchantmentId;
        this.lv = enchantmentLv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnchantmentData that = (EnchantmentData) o;

        if (lv != that.lv) return false;
        if (enchantment != null ? !enchantment.equals(that.enchantment) : that.enchantment != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = enchantment != null ? enchantment.hashCode() : 0;
        result = 31 * result + lv;
        return result;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new EnchantmentData(this.enchantment, this.lv);
    }
}
