package ak.EnchantChanger.block;


import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.ExtendedPlayerData;
import ak.EnchantChanger.tileentity.EcTileEntityMaterializer;
import ak.EnchantChanger.utils.ConfigurationUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class EcBlockMaterializer extends BlockContainer {

    public EcBlockMaterializer() {
        super(Material.rock);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (ConfigurationUtils.difficulty < 2 || checkCost(player)) {
            player.openGui(EnchantChanger.instance, 0, world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }
        ExtendedPlayerData.get(player).setLimitGaugeValue(0);//test
        return true;
    }

    private boolean checkCost(EntityPlayer player) {
        int expLv = player.experienceLevel;
        if (expLv >= ConfigurationUtils.enchantChangerCost) {
            player.addExperienceLevel(-ConfigurationUtils.enchantChangerCost);
            return true;
        }
        player.addChatComponentMessage(new ChatComponentText(String.format("Need %dLevel to open EnchantChanger", ConfigurationUtils.enchantChangerCost)));
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState state) {
        super.breakBlock(world, blockPos, state);
        world.removeTileEntity(blockPos);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new EcTileEntityMaterializer();
    }

}