package ak.enchantchanger.api;

import ak.enchantchanger.item.EcItemMateria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * 魔法マテリアの列挙クラス
 * Created by A.K. on 2017/04/29.
 */
public enum MagicType {
    BLACK(1, "Black", "enchantment.Meteor", EcItemMateria::doMeteor),
    WHITE(2, "White", "enchantment.Holy", EcItemMateria::doHoly),
    TELEPORT(3, "Teleport", "enchantment.Teleport", EcItemMateria::teleportPlayer),
    FLOATING(4, "Floating", "enchantment.Floating", ((world, player) -> {
    })),
    THUNDER(5, "Thunder", "enchantment.Thunder", EcItemMateria::doThunder),
    DESPELL(6, "Despell", "enchantment.Despell", ((world, player) -> {
        EcItemMateria.doDespell(player, player);
    })),
    HASTE(7, "Haste", "enchantment.Haste", ((world, player) -> {
        EcItemMateria.doHaste(player, player);
    })),
    ABSORPTION(8, "Absorption", "enchantment.Absorption", ((world, player) -> {
    }));
    private int id;
    private String magicName;
    private String infoString;
    private BiConsumer<World, EntityPlayer> consumer;

    MagicType(int magicId, String magicName, String infoString, BiConsumer<World, EntityPlayer> consumer) {
        this.id = magicId;
        this.magicName = magicName;
        this.infoString = infoString;
        this.consumer = consumer;
    }

    public int getId() {
        return this.id;
    }

    public String getMagicName() {
        return magicName;
    }

    public String getInfoString() {
        return infoString;
    }

    public BiConsumer<World, EntityPlayer> getConsumer() {
        return this.consumer;
    }

    public static MagicType getById(int id) {
        Optional<MagicType> optional = Arrays.stream(values()).filter(type -> type.getId() == id).findFirst();
        return optional.orElse(BLACK);
    }
}
