package ak.enchantchanger.asm;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * Created by A.K. on 14/03/26.
 */
public class EnchantmentHelperTransformer implements IClassTransformer, Opcodes {
    private static final String TARGET_CLASS_NAME = "net.minecraft.enchantment.EnchantmentHelper";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!transformedName.equals(TARGET_CLASS_NAME)) {
            return basicClass;
        }
        try {
            basicClass = changeConst(name, basicClass);
            return basicClass;
        } catch (Exception e) {
            throw new RuntimeException("failed : EnchantmentHelperTransformer loading", e);
        }
    }

    private byte[] changeConst(String target, byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        String targetMethodName = "func_77508_a";//getEnchantmentModifierDamage
        MethodNode mnode = null;
        for (MethodNode curMnode : classNode.methods) {
            if (targetMethodName.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(target, curMnode.name, curMnode.desc))) {
                mnode = curMnode;
                break;
            }
        }
        if (mnode != null) {
            AKInternalCorePlugin.logger.debug("Transforming getEnchantmentModifierDamage Method");
            AbstractInsnNode oldInsnNode1 = null;
            AbstractInsnNode oldInsnNode2 = null;

            for (AbstractInsnNode abstractInsnNode : mnode.instructions.toArray()) {
                if (abstractInsnNode instanceof IntInsnNode && ((IntInsnNode) abstractInsnNode).operand == 25) {
                    if (oldInsnNode1 == null) oldInsnNode1 = abstractInsnNode;
                    else oldInsnNode2 = abstractInsnNode;
                }
            }

            if (oldInsnNode1 != null && oldInsnNode2 != null) {
                AbstractInsnNode newInsnNode = new FieldInsnNode(GETSTATIC, "ak/enchantchanger/asm/AKInternalCorePlugin", "maxDamageModifier", "I");
                mnode.instructions.set(oldInsnNode1, newInsnNode);
                newInsnNode = new FieldInsnNode(GETSTATIC, "ak/enchantchanger/asm/AKInternalCorePlugin", "maxDamageModifier", "I");
                mnode.instructions.set(oldInsnNode2, newInsnNode);
                ClassWriter cw = new ClassWriter((ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS));
                classNode.accept(cw);
                bytes = cw.toByteArray();
            }
        }
        return bytes;
    }
}
