package ak.enchantchanger.block;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

/**
 * 魔晄炉のベースブロックのEnum
 * Created by A.K. on 2017/05/10.
 */
public enum EnumMRBaseType implements IStringSerializable {
    IRON("iron", "minecraft:iron_block", "blockIron"),
    GOLD("gold", "minecraft:gold_block", "blockGold");
    private final String name;
    private final String registryName;
    private final String oreName;
    EnumMRBaseType(String name, String registryName, String oreName) {
        this.name = name;
        this.registryName = registryName;
        this.oreName = oreName;
    }
    @Override
    @Nonnull
    public String getName() {
        return this.name;
    }

    @Nonnull
    public String getRegistryName() {
        return this.registryName;
    }
    public static EnumMRBaseType getByIndex(int index) {
        for (EnumMRBaseType type : values()) {
            if (index == type.ordinal()) {
                return type;
            }
        }
        return IRON;
    }

    public String getOreName() {
        return oreName;
    }
}
