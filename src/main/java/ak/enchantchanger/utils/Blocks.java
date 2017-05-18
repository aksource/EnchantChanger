package ak.enchantchanger.utils;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.block.EcBlockHugeMateria;
import ak.enchantchanger.block.EcBlockLifeStreamFluid;
import ak.enchantchanger.block.EcBlockMakoReactor;
import ak.enchantchanger.block.EcBlockMaterializer;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import static ak.enchantchanger.api.Constants.LIFESTREAM_FLOW_RL;
import static ak.enchantchanger.api.Constants.LIFESTREAM_STILL_RL;

/**
 * ブロックインスタンス格納クラス
 * Created by A.K. on 2017/05/07.
 */
public class Blocks {
    public static Block blockEnchantChanger;
    public static Block blockHugeMateria;
    public static Block blockLifeStream;
    public static Block blockMakoReactor;
    public static Fluid fluidLifeStream;
    public static Material materialMako = new MaterialLiquid(MapColor.GRASS);

    public static void init() {
        blockEnchantChanger = (new EcBlockMaterializer())
                .setRegistryName("enchantchanger")
                .setUnlocalizedName("enchantchanger")
                .setCreativeTab(Constants.TAB_ENCHANT_CHANGER)
                .setHardness(5.0f)
                .setResistance(2000.0f)
                .setLightOpacity(0);
        blockHugeMateria = new EcBlockHugeMateria()
                .setRegistryName("blockhugemateria")
                .setHardness(5.0f)
                .setResistance(2000.0f)
                .setLightLevel(1.0f)
                .setLightOpacity(0)
                .setUnlocalizedName("blockHugeMateria");
        fluidLifeStream = new Fluid("lifestream", LIFESTREAM_STILL_RL, LIFESTREAM_FLOW_RL).setLuminosity(15);
        FluidRegistry.registerFluid(Blocks.fluidLifeStream);
        blockLifeStream = new EcBlockLifeStreamFluid(fluidLifeStream, materialMako)
                .setRegistryName("life_stream").setUnlocalizedName("lifestream");
        blockMakoReactor = new EcBlockMakoReactor()
                .setRegistryName("blockmakoreactor")
                .setUnlocalizedName("makoreactor")
                .setHardness(5.0f)
                .setResistance(10.0f)
                .setCreativeTab(Constants.TAB_ENCHANT_CHANGER);
    }
}
