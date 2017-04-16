package ak.EnchantChanger.block.property;

import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * 製姿のIUnlistedPropertyクラス
 * Created by Akira on 2015/09/05.
 */
public class UnlistedPropertyInteger implements IUnlistedProperty<Integer> {

    String name;

    public UnlistedPropertyInteger(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(Integer value) {
        return true;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public String valueToString(Integer value) {
        return value.toString();
    }
}
