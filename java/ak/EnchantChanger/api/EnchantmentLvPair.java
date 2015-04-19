package ak.EnchantChanger.api;

import net.minecraft.enchantment.Enchantment;

/**
 * Created by A.K. on 14/02/27.
 */
public class EnchantmentLvPair {
    public Enchantment enchantment;
    public int lv;
    public EnchantmentLvPair(Enchantment enchantmentId, int enchantmentLv) {
        this.enchantment = enchantmentId;
        this.lv = enchantmentLv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnchantmentLvPair that = (EnchantmentLvPair) o;

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
        return new EnchantmentLvPair(this.enchantment, this.lv);
    }
}
