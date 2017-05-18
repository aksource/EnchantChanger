package ak.enchantchanger.api;

/**
 * マスターマテリアの種類
 * Created by A.K. on 2017/05/15.
 */
public enum MasterMateriaType {
    ULTIMATUM(0),
    PROTECTION(1),
    WATER(2),
    ATTACK(3),
    DIGGING(4),
    BOW(5),
    ADDITION(999);
    private final int meta;
    MasterMateriaType(int meta) {
        this.meta = meta;
    }

    public int getMeta() {
        return meta;
    }
}
