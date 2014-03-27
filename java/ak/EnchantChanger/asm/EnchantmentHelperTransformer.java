package ak.EnchantChanger.asm;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * Created by A.K. on 14/03/26.
 */
public class EnchantmentHelperTransformer implements IClassTransformer, Opcodes{
    private static final String TARGET_CLASS_NAME = "net.minecraft.enchantment.EnchantmentHelper";
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!FMLLaunchHandler.side().isClient() || !transformedName.equals(TARGET_CLASS_NAME)) {return basicClass;}
        try {
            AKInternalCorePlugin.logger.info("Start transforming EnchantmentHelper Class");
            basicClass = changeConst(name, basicClass);
            AKInternalCorePlugin.logger.info("Finish transforming EnchantmentHelper Class");
            return basicClass;
        } catch (Exception e) {
            throw new RuntimeException("failed : PotionArrayTransformer loading", e);
        }
    }

    private byte[] changeConst(String target, byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        String targetMethodName = "func_77508_a";//getEnchantmentModifierDamage
        MethodNode mnode = null;
        for (MethodNode curMnode : classNode.methods) {
            if (targetMethodName.equals(curMnode.name)) {
                mnode = curMnode;
                break;
            }
        }
        if (mnode != null) {
            AbstractInsnNode oldInsnNode = mnode.instructions.get(19);
            AbstractInsnNode newInsnNode = new FieldInsnNode(GETSTATIC, "ak/EnchantChanger/asm/AKInternalCorePlugin", "maxDamageModifier", "I");
            mnode.instructions.set(oldInsnNode, newInsnNode);
            oldInsnNode = mnode.instructions.get(24);
            newInsnNode = new FieldInsnNode(GETSTATIC, "ak/EnchantChanger/asm/AKInternalCorePlugin", "maxDamageModifier", "I");
            mnode.instructions.set(oldInsnNode, newInsnNode);
            ClassWriter cw = new ClassWriter((ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS));
            classNode.accept(cw);
            bytes = cw.toByteArray();
        }
        return bytes;
    }
}
