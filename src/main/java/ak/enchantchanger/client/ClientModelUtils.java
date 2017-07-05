package ak.enchantchanger.client;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.api.MasterMateriaType;
import ak.enchantchanger.client.models.BakedModelMateria;
import ak.enchantchanger.client.models.EcSwordModel;
import ak.enchantchanger.item.EcItemMateria;
import ak.enchantchanger.utils.Blocks;
import ak.enchantchanger.utils.Items;
import ak.enchantchanger.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ak.enchantchanger.api.Constants.*;
import static ak.enchantchanger.client.ClientProxy.mc;

/**
 * クライアント側でモデルを扱うクラスです。
 * Created by A.K. on 2016/05/30.
 */
public class ClientModelUtils {

    private static final Map<ResourceLocation, ModelResourceLocation> MODEL_RESOURCE_LOCATION_MAP = Maps.newHashMap();
    static TextureAtlasSprite[] textureAtlasSpritesMateriaArray = new TextureAtlasSprite[16];
    public static ResourceLocation[] resourceLocationsMateria = new ResourceLocation[16];
    static ImmutableMap<String, String> textureMapUltimate = new ImmutableMap.Builder<String, String>()
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_ULTIMATE_EMBLEM), StringUtils.makeObjTexturePath(TEXTURE_NAME_ULTIMATE_EMBLEM))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_ULTIMATE_GRIP), StringUtils.makeObjTexturePath(TEXTURE_NAME_ULTIMATE_GRIP))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_ULTIMATE_HAND), StringUtils.makeObjTexturePath(TEXTURE_NAME_ULTIMATE_HAND))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_ULTIMATE_PIPE_01), StringUtils.makeObjTexturePath(TEXTURE_NAME_ULTIMATE_PIPE_01))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_ULTIMATE_PIPE_02), StringUtils.makeObjTexturePath(TEXTURE_NAME_ULTIMATE_PIPE_02))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_ULTIMATE_SWORD), StringUtils.makeObjTexturePath(TEXTURE_NAME_ULTIMATE_SWORD)).build();
    static ImmutableMap<String, String> textureMapBuster = new ImmutableMap.Builder<String, String>()
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_BUSTER_EDGE), StringUtils.makeObjTexturePath(TEXTURE_NAME_BUSTER_EDGE))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_BUSTER_CYLINDER), StringUtils.makeObjTexturePath(TEXTURE_NAME_BUSTER_CYLINDER))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_BUSTER_BOX), StringUtils.makeObjTexturePath(TEXTURE_NAME_BUSTER_BOX)).build();
    static ImmutableMap<String, String> textureMapMasamune = new ImmutableMap.Builder<String, String>()
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_MASAMUNE_SWORD), StringUtils.makeObjTexturePath(TEXTURE_NAME_MASAMUNE_SWORD))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_MASAMUNE_GRIP), StringUtils.makeObjTexturePath(TEXTURE_NAME_MASAMUNE_GRIP))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_MASAMUNE_SCABBARD), StringUtils.makeObjTexturePath(TEXTURE_NAME_MASAMUNE_SCABBARD)).build();
    static ImmutableMap<String, String> textureMapFirst = new ImmutableMap.Builder<String, String>()
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_FIRST_CASE), StringUtils.makeObjTexturePath(TEXTURE_NAME_FIRST_CASE))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_FIRST_CENTER), StringUtils.makeObjTexturePath(TEXTURE_NAME_FIRST_CENTER))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_FIRST_EDGE), StringUtils.makeObjTexturePath(TEXTURE_NAME_FIRST_EDGE))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_FIRST_GRIP), StringUtils.makeObjTexturePath(TEXTURE_NAME_FIRST_GRIP)).build();
    static ImmutableMap<String, String> textureMapUnion = new ImmutableMap.Builder<String, String>()
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_FIRST_CASE), StringUtils.makeObjTexturePath(TEXTURE_NAME_FIRST_CASE))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_FIRST_CENTER), StringUtils.makeObjTexturePath(TEXTURE_NAME_FIRST_CENTER))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_FIRST_EDGE), StringUtils.makeObjTexturePath(TEXTURE_NAME_FIRST_EDGE))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_FIRST_GRIP), StringUtils.makeObjTexturePath(TEXTURE_NAME_FIRST_GRIP))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_BUTTERFLY_EDGE), StringUtils.makeObjTexturePath(TEXTURE_NAME_BUTTERFLY_EDGE))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_BUTTERFLY_GRIP), StringUtils.makeObjTexturePath(TEXTURE_NAME_BUTTERFLY_GRIP))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_ORGANIX_EDGE), StringUtils.makeObjTexturePath(TEXTURE_NAME_ORGANIX_EDGE))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_ORGANIX_GRIP), StringUtils.makeObjTexturePath(TEXTURE_NAME_ORGANIX_GRIP))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_RUNE_EDGE), StringUtils.makeObjTexturePath(TEXTURE_NAME_RUNE_EDGE))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_RUNE_GRIP), StringUtils.makeObjTexturePath(TEXTURE_NAME_RUNE_GRIP))
            .put(StringUtils.makeObjMaterialKeyName(TEXTURE_NAME_RUNE_HAND), StringUtils.makeObjTexturePath(TEXTURE_NAME_RUNE_HAND)).build();

    private ClientModelUtils() {}

    static void registerModelsOnPreInit() {
        //1.8からのモデル登録。
        B3DLoader.INSTANCE.addDomain(Constants.MOD_ID);
        OBJLoader.INSTANCE.addDomain(Constants.MOD_ID);
//        registerCustomBlockModel(Blocks.blockHugeMateria, 0, Blocks.blockHugeMateria.getRegistryName().toString(), MODEL_TYPE_INVENTORY, false);
        registerCustomBlockModel(Blocks.blockMakoReactor, 0, Blocks.blockMakoReactor.getRegistryName().toString() + "_iron", MODEL_TYPE_INVENTORY);
        registerCustomBlockModel(Blocks.blockMakoReactor, 1, Blocks.blockMakoReactor.getRegistryName().toString() + "_gold", MODEL_TYPE_INVENTORY);

        registerFluidBlockModel(Blocks.blockLifeStream, MODEL_TYPE_FLUID);
        /* 魔晄炉用モデルローダー登録 */
//        ModelLoaderRegistry.registerLoader(new MakoReactorModelLoader());
    }

    static void registerModels() {
        registerItemModel(Items.itemZackSword, 0);
        registerItemModel(Items.itemCloudSwordCore, 0);
        registerItemModel(Items.itemCloudSword, 0);
        for (int i = 0; i <= EcItemMateria.MagicMateriaNum; i++) {
            registerItemModel(Items.itemMateria, i);
        }

        for (MasterMateriaType type : MasterMateriaType.values()) {
            registerItemModel(Items.itemMasterMateria, type.getMeta());
        }
        registerItemModel(Items.itemSephirothSword, 0);
        registerItemModel(Items.itemImitateSephirothSword, 0);
        registerItemModel(Items.itemUltimateWeapon, 0);

        registerItemModel(Items.itemHugeMateria, 0);
        registerItemModel(Items.itemExExpBottle, 0);
        registerItemModel(Items.itemBucketLifeStream, 0);
        registerItemModel(Items.itemPortableEnchantChanger, 0);
        registerItemModel(Items.itemPortableEnchantmentTable, 0);
        registerBlockModel(Blocks.blockEnchantChanger, 0);
    }

    static void changeModels(ModelBakeEvent event) {
        IRegistry<ModelResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
        ClientModelUtils.changeSwordModel(modelRegistry, Items.itemZackSword,
                Arrays.asList(ITEM_BUSTER_SWORD_MODEL_RL), 0.3F, 0.1F, ClientModelUtils.textureMapBuster);
        ClientModelUtils.changeSwordModel(modelRegistry, Items.itemCloudSword,
                Arrays.asList(ITEM_UNION_SWORD_MODEL_RL), 0.3F, 0.1F, ClientModelUtils.textureMapUnion);
        ClientModelUtils.changeSwordModel(modelRegistry, Items.itemCloudSwordCore,
                Arrays.asList(ITEM_FIRST_SWORD_CLOSED_MODEL_RL, ITEM_FIRST_SWORD_OPEN_MODEL_RL), 0.3F, 0.1F, ClientModelUtils.textureMapFirst);
        ClientModelUtils.changeSwordModel(modelRegistry, Items.itemUltimateWeapon,
                Arrays.asList(ITEM_ULTIMATE_WEAPON_MODEL_RL), 0.3F, 0.1F, ClientModelUtils.textureMapUltimate);
        ClientModelUtils.changeSwordModel(modelRegistry, Items.itemSephirothSword,
                Arrays.asList(ITEM_MASAMUNE_BLADE_MODEL_RL, ITEM_MASAMUNE_BLADE_SCABBARD_MODEL_RL), 0.3F, 0.1F, ClientModelUtils.textureMapMasamune);
        ClientModelUtils.changeSwordModel(modelRegistry, Items.itemImitateSephirothSword,
                Arrays.asList(ITEM_MASAMUNE_BLADE_MODEL_RL, ITEM_MASAMUNE_BLADE_SCABBARD_MODEL_RL), 0.3F, 0.1F, ClientModelUtils.textureMapMasamune);
        ClientModelUtils.changeMateriaModel(modelRegistry,
                Items.itemMateria.getRegistryName(), ITEM_MATERIA_MODEL_RL);
        ClientModelUtils.changeMateriaModel(modelRegistry,
                Items.itemMasterMateria.getRegistryName(), ITEM_MATERIA_MODEL_RL);
    }

    private static void changeSwordModel(IRegistry<ModelResourceLocation, IBakedModel> modelRegistry, Item ecSword, List<ResourceLocation> rlList,
                                           float sizeFPV, float sizeTPV, ImmutableMap<String, String> textureMap) {
        ResourceLocation name = ecSword.getRegistryName();
        List<IRetexturableModel> modelList = new ArrayList<>();
        IBakedModel iconModel = modelRegistry.getObject(MODEL_RESOURCE_LOCATION_MAP.get(name));
        try {
            for (ResourceLocation rl : rlList) {
                IModel model = ModelLoaderRegistry.getModel(rl);
                modelList.add((IRetexturableModel) model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        modelRegistry.putObject(MODEL_RESOURCE_LOCATION_MAP.get(name), new EcSwordModel(iconModel, modelList, sizeFPV, sizeTPV, textureMap));
    }

    private static void changeMateriaModel(IRegistry<ModelResourceLocation, IBakedModel> modelRegistry, ResourceLocation name, ResourceLocation rl) {
        IRetexturableModel model = null;
        try {
            model = (IRetexturableModel) ModelLoaderRegistry.getModel(rl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        modelRegistry.putObject(MODEL_RESOURCE_LOCATION_MAP.get(name), new BakedModelMateria(model));
    }

    static void registerCustomBlockModel(Block block, int meta, String modelLocation, String type) {
        registerCustomItemModel(Item.getItemFromBlock(block), meta, modelLocation, type);
    }

    /**
     * 液体ブロックモデルの登録<br />
     * preInitで行うこと
     * @param block 流体ブロック
     * @param type fluid or gas
     */
    static void registerFluidBlockModel(Block block, String type) {
        ModelResourceLocation modelResourceLocation = setCustomModelRsrcToMap(block.getRegistryName(), block.getRegistryName().toString(), type);
        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(block), new CustomItemMeshDefinition(modelResourceLocation));
        ModelLoader.setCustomStateMapper(block, new FluidStateMapperBase(modelResourceLocation));
    }

    private static void registerCustomItemModel(Item item, int meta, String modelLocation, String type) {
        ResourceLocation itemName = item.getRegistryName();
        ModelResourceLocation modelResourceLocation = setModelRsrcToMap(itemName, modelLocation, type);
        ModelLoader.setCustomModelResourceLocation(item, meta, modelResourceLocation);
    }

    private static void registerBlockModel(Block block, int meta) {
        registerItemModel(Item.getItemFromBlock(block), meta);
    }

    private static void registerItemModel(Item item, int meta) {
        ModelResourceLocation modelResourceLocation = setModelRsrcToMap(item.getRegistryName(), item.getRegistryName().toString(), MODEL_TYPE_INVENTORY);
        mc.getRenderItem().getItemModelMesher().register(item, meta, modelResourceLocation);
//        ModelLoader.setCustomModelResourceLocation(item, meta, modelResourceLocation);
    }


    private static ModelResourceLocation setCustomModelRsrcToMap(ResourceLocation objName, String modelLocation, String type) {
        return setModelRsrcToMap(objName, modelLocation, type);
    }

    private static ModelResourceLocation setModelRsrcToMap(ResourceLocation objName, String modelLocation, String type) {
        MODEL_RESOURCE_LOCATION_MAP.put(objName, new ModelResourceLocation(modelLocation, type));
        return MODEL_RESOURCE_LOCATION_MAP.get(objName);
    }

    protected static class CustomItemMeshDefinition implements ItemMeshDefinition {
        ModelResourceLocation modelResourceLocation;

        CustomItemMeshDefinition(ModelResourceLocation modelResourceLocation) {
            this.modelResourceLocation = modelResourceLocation;
        }

        @Override
        @Nonnull
        public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
            return this.modelResourceLocation;
        }
    }

    protected static class FluidStateMapperBase extends StateMapperBase {
        ModelResourceLocation modelResourceLocation;

        FluidStateMapperBase(ModelResourceLocation modelResourceLocation) {
            this.modelResourceLocation = modelResourceLocation;
        }

        @Override
        @Nonnull
        protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState iBlockState) {
            return this.modelResourceLocation;
        }
    }

    static {
        for (int i = 0; i < 16; i++) {
            resourceLocationsMateria[i] = new ResourceLocation(MOD_ID,
                    String.format("gui/materia%d", i));
        }
    }
}
