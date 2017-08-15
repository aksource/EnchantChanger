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
        blockEnchantChanger = new EcBlockMaterializer()
                .setRegistryName(Constants.REG_BLOCK_ENCHANTCHANGER)
                .setUnlocalizedName(Constants.REG_BLOCK_ENCHANTCHANGER)
                .setCreativeTab(Constants.TAB_ENCHANT_CHANGER)
                .setHardness(5.0f)
                .setResistance(2000.0f)
                .setLightOpacity(0);
        blockHugeMateria = new EcBlockHugeMateria()
                .setRegistryName(Constants.REG_BLOCK_HUGE_MATERIA)
                .setHardness(5.0f)
                .setResistance(2000.0f)
                .setLightLevel(1.0f)
                .setLightOpacity(0)
                .setUnlocalizedName(Constants.REG_BLOCK_HUGE_MATERIA);
        fluidLifeStream = new Fluid(Constants.REG_FLUID_LIFE_STREAM, LIFESTREAM_STILL_RL, LIFESTREAM_FLOW_RL).setLuminosity(15);
        FluidRegistry.registerFluid(Blocks.fluidLifeStream);
        // UniversalBucket登録処理
        FluidRegistry.addBucketForFluid(fluidLifeStream);
        blockLifeStream = new EcBlockLifeStreamFluid(fluidLifeStream, materialMako)
                .setRegistryName(Constants.REG_BLOCK_LIFE_STREAM).setUnlocalizedName(Constants.REG_BLOCK_LIFE_STREAM);
        blockMakoReactor = new EcBlockMakoReactor()
                .setRegistryName(Constants.REG_BLOCK_MAKO_REACTOR)
                .setUnlocalizedName(Constants.REG_BLOCK_MAKO_REACTOR)
                .setHardness(5.0f)
                .setResistance(10.0f)
                .setCreativeTab(Constants.TAB_ENCHANT_CHANGER);
    }
}
