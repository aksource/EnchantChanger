package ak.enchantchanger.api;

import ak.enchantchanger.item.EcItemMateria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.Optional;

/**
 * 魔法マテリアの列挙クラス
 * Created by A.K. on 2017/04/29.
 */
public enum MagicType {
    BLACK(1, EcItemMateria::doMeteor),
    WHITE(2, EcItemMateria::doHoly),
    TELEPORT(3, EcItemMateria::teleportPlayer),
    FLOATING(4, ((world, player) -> {})),
    THUNDER(5, EcItemMateria::doThunder),
    DESPELL(6, ((world, player) -> {EcItemMateria.doDespell(player, player);})),
    HASTE(7, ((world, player) -> {EcItemMateria.doHaste(player, player);})),
    ABSORPTION(8, ((world, player) -> {}));
    private int id;
    private BiConsumer<World, EntityPlayer> consumer;
    MagicType(int magicId, BiConsumer<World, EntityPlayer> consumer) {
        this.id = magicId;
        this.consumer = consumer;
    }
    public int getId() {
        return this.id;
    }
    public BiConsumer<World, EntityPlayer> getConsumer() {
        return this.consumer;
    }
    public static MagicType getById(int id) {
        Optional<MagicType> optional = Arrays.stream(values()).filter(type -> type.getId() == id).findFirst();
        return optional.orElse(BLACK);
    }
}
