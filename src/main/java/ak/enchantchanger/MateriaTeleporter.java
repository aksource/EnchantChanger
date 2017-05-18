package ak.enchantchanger;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;

public class MateriaTeleporter extends Teleporter {

    public MateriaTeleporter(WorldServer worldServer) {
        super(worldServer);
        worldServer.customTeleporters.add(this);
    }

    //placeInPortal
    @Override
    public void placeInPortal(@Nonnull Entity entityIn, float rotationYaw) {
        entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0.0D;

        int x = (int) entityIn.posX;
        int z = (int) entityIn.posZ;
        BlockPos blockPos = entityIn.getEntityWorld()
                .getTopSolidOrLiquidBlock(new BlockPos(entityIn.posX, entityIn.posY, entityIn.posZ));
        entityIn.moveToBlockPosAndAngles(blockPos, 0, 0);
        entityIn.fallDistance = 0;
    }
}
