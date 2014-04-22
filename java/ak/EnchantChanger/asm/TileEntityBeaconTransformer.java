package ak.EnchantChanger.asm;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Created by A.K. on 14/04/22.
 */
public class TileEntityBeaconTransformer implements IClassTransformer, Opcodes{
    private static final String TARGET_CLASS_NAME = "net.minecraft.tileentity.TileEntityBeacon";
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!FMLLaunchHandler.side().isClient() || !transformedName.equals(TARGET_CLASS_NAME)) {return basicClass;}
        try {
            AKInternalCorePlugin.logger.info("Start transforming TileEntityBeacon Class");
            basicClass = changableAnvilMaxLevel(name, basicClass);
            AKInternalCorePlugin.logger.info("Finish transforming TileEntityBeacon Class");
            return basicClass;
        } catch (Exception e) {
            throw new RuntimeException("failed : TileEntityBeaconTransformer loading", e);
        }
    }

    private byte[] changableAnvilMaxLevel(String target, byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        String targetMethodName = "func_146000_x";//func_146000_x
        String targetMethodDesc = "()V";
        MethodNode mnode = null;
        for (MethodNode curMnode : classNode.methods) {
            if (targetMethodName.equals(curMnode.name) && targetMethodDesc.equals(curMnode.desc)) {
                mnode = curMnode;
                break;
            }
        }
        if (mnode != null) {
            AbstractInsnNode newInsnNode1 = new FieldInsnNode(GETSTATIC, "ak/EnchantChanger/asm/AKInternalCorePlugin", "beaconLevelRange", "I");
            AbstractInsnNode newInsnNode2 = new FieldInsnNode(GETSTATIC, "ak/EnchantChanger/asm/AKInternalCorePlugin", "beaconBaseRange", "I");
            mnode.instructions.set(mnode.instructions.get(20), newInsnNode1);
            mnode.instructions.set(mnode.instructions.get(22), newInsnNode2);
            ClassWriter cw = new ClassWriter((ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS));
            classNode.accept(cw);
            bytes = cw.toByteArray();
        }
        return bytes;
    }
}
