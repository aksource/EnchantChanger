package ak.EnchantChanger.asm;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * Created by A.K. on 14/04/22.
 */
public class AnvilLevelTransformer implements IClassTransformer, Opcodes {
    private static final String TARGET_CLASS_NAME = "net.minecraft.inventory.ContainerRepair";
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (/*!FMLLaunchHandler.side().isClient() || */!transformedName.equals(TARGET_CLASS_NAME)) {return basicClass;}
        try {
            AKInternalCorePlugin.logger.info("Start transforming ContainerRepair Class");
            basicClass = changeableAnvilMaxLevel(name, basicClass);
            AKInternalCorePlugin.logger.info("Finish transforming ContainerRepair Class");
            return basicClass;
        } catch (Exception e) {
            throw new RuntimeException("failed : AnvilLevelTransformer loading", e);
        }
    }

    private byte[] changeableAnvilMaxLevel(String target, byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        String targetMethodName = "func_82848_d";//updateRepairOutput
        String targetMethodDesc = "()V";
        MethodNode mnode = null;
        for (MethodNode curMnode : classNode.methods) {
            if (targetMethodName.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(target, curMnode.name, curMnode.desc)) && targetMethodDesc.equals(curMnode.desc)) {
                mnode = curMnode;
                break;
            }
        }
        if (mnode != null) {
            AKInternalCorePlugin.logger.info("Transforming updateRepairOutput Method");
            AbstractInsnNode oldInsnNode1 = null;
            AbstractInsnNode oldInsnNode2 = null;
            AbstractInsnNode oldInsnNode3 = null;
            for (AbstractInsnNode abstractInsnNode : mnode.instructions.toArray()) {
                if (abstractInsnNode instanceof  IntInsnNode) {
                    if (((IntInsnNode)abstractInsnNode).operand == 40) {
                        if (oldInsnNode1 == null) oldInsnNode1 = abstractInsnNode;
                        else oldInsnNode3 = abstractInsnNode;
                    } else if (((IntInsnNode)abstractInsnNode).operand == 39) {
                        oldInsnNode2 = abstractInsnNode;
                    }
                }
            }

            if (oldInsnNode1 != null && oldInsnNode2 != null && oldInsnNode3 != null) {
                AbstractInsnNode newInsnNode1 = new FieldInsnNode(GETSTATIC, "ak/EnchantChanger/asm/AKInternalCorePlugin", "maxAnvilLevelModifier", "I");
                AbstractInsnNode newInsnNode2 = new FieldInsnNode(GETSTATIC, "ak/EnchantChanger/asm/AKInternalCorePlugin", "setAnvilLevelModifier", "I");
                AbstractInsnNode newInsnNode3 = new FieldInsnNode(GETSTATIC, "ak/EnchantChanger/asm/AKInternalCorePlugin", "maxAnvilLevelModifier", "I");
                mnode.instructions.set(oldInsnNode1, newInsnNode1);
                mnode.instructions.set(oldInsnNode2, newInsnNode2);
                mnode.instructions.set(oldInsnNode3, newInsnNode3);
                ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                classNode.accept(cw);
                bytes = cw.toByteArray();
            }
        }
        return bytes;
    }
}
