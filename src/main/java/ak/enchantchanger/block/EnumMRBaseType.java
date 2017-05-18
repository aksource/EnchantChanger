package ak.enchantchanger.block;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

/**
 * 魔晄炉のベースブロックのEnum
 * Created by A.K. on 2017/05/10.
 */
public enum EnumMRBaseType implements IStringSerializable {
    IRON("iron"),
    GOLD("gold");
    private final String name;
    EnumMRBaseType(String name) {
        this.name = name;
    }
    @Override
    @Nonnull
    public String getName() {
        return this.name;
    }

    public static EnumMRBaseType getByIndex(int index) {
        for (EnumMRBaseType type : values()) {
            if (index == type.ordinal()) {
                return type;
            }
        }
        return IRON;
    }
}
