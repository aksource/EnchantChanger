package ak.EnchantChanger.asm;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * Created by A.K. on 14/04/22.
 */
public class AnvilLevelClientTransformer implements IClassTransformer, Opcodes {
    private static final String TARGET_CLASS_NAME = "net.minecraft.client.gui.GuiRepair";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!FMLLaunchHandler.side().isClient() || !transformedName.equals(TARGET_CLASS_NAME)) {
            return basicClass;
        }
        try {
            basicClass = changableAnvilMaxLevel(name, basicClass);
            return basicClass;
        } catch (Exception e) {
            throw new RuntimeException("failed : AnvilLevelClientTransformer loading", e);
        }
    }

    private byte[] changableAnvilMaxLevel(String target, byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        String targetMethodName = "func_146979_b";//drawGuiContainerForegroundLayer
        String targetMethodDesc = "(II)V";
        MethodNode mnode = null;
        for (MethodNode curMnode : classNode.methods) {
            if (targetMethodName.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(target, curMnode.name, curMnode.desc)) && targetMethodDesc.equals(curMnode.desc)) {
                mnode = curMnode;
                break;
            }
        }
        if (mnode != null) {
            AKInternalCorePlugin.logger.debug("Transforming drawGuiContainerForegroundLayer Method");
            AbstractInsnNode oldInsnNode = null;
            for (AbstractInsnNode abstractInsnNode : mnode.instructions.toArray()) {
                if (abstractInsnNode instanceof IntInsnNode && ((IntInsnNode) abstractInsnNode).operand == 40) {
                    oldInsnNode = abstractInsnNode;
                }
            }

            if (oldInsnNode != null) {
                AbstractInsnNode newInsnNode1 = new FieldInsnNode(GETSTATIC, "ak/EnchantChanger/asm/AKInternalCorePlugin", "maxAnvilLevelModifier", "I");
                mnode.instructions.set(oldInsnNode, newInsnNode1);
                ClassWriter cw = new ClassWriter((ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS));
                classNode.accept(cw);
                bytes = cw.toByteArray();
            }
        }
        return bytes;
    }
}
