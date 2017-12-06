package ak.enchantchanger.api;

/**
 * マスターマテリアの種類
 * Created by A.K. on 2017/05/15.
 */
public enum MasterMateriaType {
    ULTIMATUM(0, 5000),
    PROTECTION(1, 1000),
    WATER(2, 1000),
    ATTACK(3, 1000),
    DIGGING(4, 1000),
    BOW(5, 1000),
    ADDITION(999, 1000);
    private final int meta;
    private final int makoAmount;

    MasterMateriaType(int meta, int makoAmount) {
        this.meta = meta;
        this.makoAmount = makoAmount;
    }

    public int getMeta() {
        return meta;
    }

    public int getMakoAmount() {
        return makoAmount;
    }
}
