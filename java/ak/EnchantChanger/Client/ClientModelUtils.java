package ak.EnchantChanger.Client;

import ak.EnchantChanger.Client.models.BakedModelMateria;
import ak.EnchantChanger.Client.models.EcSwordModel;
import ak.EnchantChanger.item.EcItemMasterMateria;
import ak.EnchantChanger.item.EcItemMateria;
import ak.EnchantChanger.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ak.EnchantChanger.Client.ClientProxy.mc;
import static ak.EnchantChanger.EnchantChanger.*;
import static ak.EnchantChanger.api.Constants.*;

/**
 * クライアント側でモデルを扱うクラスです。
 * Created by A.K. on 2016/05/30.
 */
public class ClientModelUtils {

    public static final Map<String, ModelResourceLocation> MODEL_RESOURCE_LOCATION_MAP = Maps.newHashMap();
    public static TextureAtlasSprite[] textureAtlasSpritesMateriaArray = new TextureAtlasSprite[16];
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

    protected static void registerModels() {
        registerItemModel(itemZackSword, 0);
        registerItemModel(itemCloudSwordCore, 0);
        registerItemModel(itemCloudSword, 0);
        for (int i = 0; i <= EcItemMateria.MagicMateriaNum; i++) {
            registerItemModel(itemMateria, i);
        }

        for (int i = 0; i < EcItemMasterMateria.MasterMateriaNum; i++) {
            registerItemModel(itemMasterMateria, i);
        }
        registerItemModel(itemSephirothSword, 0);
        registerItemModel(itemImitateSephirothSword, 0);
        registerItemModel(itemUltimateWeapon, 0);

        registerItemModel(itemHugeMateria, 0);
        registerItemModel(itemExExpBottle, 0);
        registerItemModel(itemBucketLifeStream, 0);
        registerItemModel(itemPortableEnchantChanger, 0);
        registerItemModel(itemPortableEnchantmentTable, 0);
        registerBlockModel(blockEnchantChanger, 0);
    }

    protected static void changeModels(ModelBakeEvent event) {
        ClientModelUtils.changeSwordModel(event.modelRegistry, itemZackSword,
                Arrays.asList(ITEM_BUSTER_SWORD_MODEL_RL), 0.3F, 0.1F, ClientModelUtils.textureMapBuster);
        ClientModelUtils.changeSwordModel(event.modelRegistry, itemCloudSword,
                Arrays.asList(ITEM_UNION_SWORD_MODEL_RL), 0.3F, 0.1F, ClientModelUtils.textureMapUnion);
        ClientModelUtils.changeSwordModel(event.modelRegistry, itemCloudSwordCore,
                Arrays.asList(ITEM_FIRST_SWORD_CLOSED_MODEL_RL, ITEM_FIRST_SWORD_OPEN_MODEL_RL), 0.3F, 0.1F, ClientModelUtils.textureMapFirst);
        ClientModelUtils.changeSwordModel(event.modelRegistry, itemUltimateWeapon,
                Arrays.asList(ITEM_ULTIMATE_WEAPON_MODEL_RL), 0.3F, 0.1F, ClientModelUtils.textureMapUltimate);
        ClientModelUtils.changeSwordModel(event.modelRegistry, itemSephirothSword,
                Arrays.asList(ITEM_MASAMUNE_BLADE_MODEL_RL, ITEM_MASAMUNE_BLADE_SCABBARD_MODEL_RL), 0.3F, 0.1F, ClientModelUtils.textureMapMasamune);
        ClientModelUtils.changeSwordModel(event.modelRegistry, itemImitateSephirothSword,
                Arrays.asList(ITEM_MASAMUNE_BLADE_MODEL_RL, ITEM_MASAMUNE_BLADE_SCABBARD_MODEL_RL), 0.3F, 0.1F, ClientModelUtils.textureMapMasamune);
        ClientModelUtils.changeMateriaModel(event.modelRegistry,
                itemMateria.getRegistryName(), ITEM_MATERIA_MODEL_RL);
        ClientModelUtils.changeMateriaModel(event.modelRegistry,
                itemMasterMateria.getRegistryName(), ITEM_MATERIA_MODEL_RL);
    }

    protected static void changeSwordModel(IRegistry<ModelResourceLocation, IBakedModel> modelRegistry, Item ecSword, List<ResourceLocation> rlList,
                                           float sizeFPV, float sizeTPV, ImmutableMap<String, String> textureMap) {
        String name = ecSword.getRegistryName();
        List<IRetexturableModel> modelList = new ArrayList<>();
        IBakedModel iconModel = modelRegistry.getObject(MODEL_RESOURCE_LOCATION_MAP.get(name));
        IModel model = null;
        try {
            for (ResourceLocation rl : rlList) {
                model = ModelLoaderRegistry.getModel(rl);
                modelList.add((IRetexturableModel) model);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (model instanceof IRetexturableModel) {
            modelRegistry.putObject(MODEL_RESOURCE_LOCATION_MAP.get(name), new EcSwordModel(iconModel, modelList, sizeFPV, sizeTPV, textureMap));
        }
    }

    protected static void changeMateriaModel(IRegistry<ModelResourceLocation, IBakedModel> modelRegistry, String name, ResourceLocation rl) {
        IRetexturableModel model = null;
        try {
            model = (IRetexturableModel) ModelLoaderRegistry.getModel(rl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        modelRegistry.putObject(MODEL_RESOURCE_LOCATION_MAP.get(name), new BakedModelMateria(model));
    }

    protected static void registerCustomBlockModel(Block block, int meta, String modelLocation, String type, boolean noModelLoc) {
        registerCustomItemModel(Item.getItemFromBlock(block), meta, modelLocation, type, noModelLoc);
        if (type.equals(MODEL_TYPE_FLUID)) {
            ModelResourceLocation modelResourceLocation = MODEL_RESOURCE_LOCATION_MAP.get(block.getUnlocalizedName());
            ModelLoader.setCustomStateMapper(block, new FluidStateMapperBase(modelResourceLocation));
        }
    }

    protected static void registerCustomItemModel(Item item, int meta, String modelLocation, String type, boolean noModelLoc) {
//        if (noModelLoc) {
//            ModelBakery.registerItemVariants(item);
//        } else {
//            ModelBakery.addVariantName(item, modelLocation);
//        }
        String itemName = item.getRegistryName();
        ModelResourceLocation modelResourceLocation = setModelRsrcToMap(itemName, modelLocation, type);
        ModelLoader.setCustomModelResourceLocation(item, meta, modelResourceLocation);
//        ModelLoader.setCustomMeshDefinition(item, new CustomItemMeshDefinition(modelResourceLocation));
        //EnchantChanger.logger.info("added ModelLocation of " + Constants.EcAssetsDomain + ":" + modelLocation);
    }

    protected static void registerBlockModel(Block block, int meta) {
        registerItemModel(Item.getItemFromBlock(block), meta);
    }

    protected static void registerItemModel(Item item, int meta) {
//        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(GameRegistry.findUniqueIdentifierFor(item).toString(), MODEL_TYPE_INVENTORY);
        ModelResourceLocation modelResourceLocation = setModelRsrcToMap(item.getRegistryName(), item.getRegistryName(), MODEL_TYPE_INVENTORY);
        mc.getRenderItem().getItemModelMesher().register(item, meta, modelResourceLocation);
//        ModelLoader.setCustomModelResourceLocation(item, meta, modelResourceLocation);
    }


    protected static ModelResourceLocation setCustomModelRsrcToMap(String objName, String modelLocation, String type) {
        return setModelRsrcToMap(objName, EcAssetsDomain + ":" + modelLocation, type);
    }

    protected static ModelResourceLocation setModelRsrcToMap(String objName, String modelLocation, String type) {
        MODEL_RESOURCE_LOCATION_MAP.put(objName, new ModelResourceLocation(modelLocation, type));
        return MODEL_RESOURCE_LOCATION_MAP.get(objName);
    }

    protected static class CustomItemMeshDefinition implements ItemMeshDefinition {
        ModelResourceLocation modelResourceLocation;

        public CustomItemMeshDefinition(ModelResourceLocation modelResourceLocation) {
            this.modelResourceLocation = modelResourceLocation;
        }

        @Override
        public ModelResourceLocation getModelLocation(ItemStack stack) {
            return this.modelResourceLocation;
        }
    }

    protected static class FluidStateMapperBase extends StateMapperBase {
        ModelResourceLocation modelResourceLocation;

        public FluidStateMapperBase(ModelResourceLocation modelResourceLocation) {
            this.modelResourceLocation = modelResourceLocation;
        }

        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
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
