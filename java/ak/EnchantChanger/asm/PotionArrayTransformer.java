package ak.EnchantChanger.asm;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * Created by A.K. on 14/03/13.
 */
public class PotionArrayTransformer implements IClassTransformer, Opcodes{
    private static final String TARGET_CLASS_NAME = "net.minecraft.potion.Potion";//qi
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!FMLLaunchHandler.side().isClient() || !transformedName.equals(TARGET_CLASS_NAME)) {return basicClass;}
        try {
            AKInternalCorePlugin.logger.info("Start transforming Potion Class");
//            basicClass = extendPotionArray(name, basicClass);
            ClassReader classReader = new ClassReader(basicClass);
            ClassWriter classWriter = new ClassWriter(1);
            classReader.accept(new CustomVisitor(name,classWriter), 8);
            AKInternalCorePlugin.logger.info("Finish transforming Potion Class");
//            return basicClass;
            return classWriter.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("failed : PotionArrayTransformer loading", e);
        }
    }

    private byte[] extendPotionArray(String target, byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        String targetMethodName = "<clinit>";//static init method
        MethodNode mnode = null;
        for (MethodNode curMnode : classNode.methods) {
            if (targetMethodName.equals(curMnode.name)) {
                mnode = curMnode;
                break;
            }
        }
        if (mnode != null) {
            AKInternalCorePlugin.logger.info("Transforming static init method");
            AbstractInsnNode oldInsnNode = mnode.instructions.get(2);
            AbstractInsnNode newInsnNode = new VarInsnNode(Opcodes.BIPUSH, Byte.MAX_VALUE);
            mnode.instructions.set(oldInsnNode, newInsnNode);
            ClassWriter cw = new ClassWriter((ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS));
            classNode.accept(cw);
            bytes = cw.toByteArray();
        }
        return bytes;
    }

    //Custom ClassVisitor
    class CustomVisitor extends ClassVisitor {
        String owner;
        public CustomVisitor(String owner ,ClassVisitor cv) {
            super(Opcodes.ASM4,cv);
            this.owner = owner;
        }
        static final String targetMethodName = "<clinit>";//static init method
        static final String targetMethodDesc = "()V";

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (targetMethodName.equals(name) && targetMethodDesc.equals(desc)) {
                //static initメソッドの時のみ、Custom MethodVisitorを生成する。
                AKInternalCorePlugin.logger.info("Transforming static init method");
                return new CustomMethodVisitor(this.api, super.visitMethod(access, name, desc, signature, exceptions));
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }
    }
    //Custom MethodVisitor
    class CustomMethodVisitor extends MethodVisitor {
        public CustomMethodVisitor(int api, MethodVisitor mv) {
            super(api, mv);
        }

        static final int targetOpcode = Opcodes.BIPUSH;
        static final int targetOperand = 32;
        static final int newOperand = Byte.MAX_VALUE;

        @Override
        public void visitIntInsn(int opcode, int operand) {
            if (targetOpcode == opcode && targetOperand == operand) {
                //BIPUSH 32を、BIPUSH Byte.MAX_VALUEに入れ替える。
                AKInternalCorePlugin.logger.info("Change BIPUSH 32 to BIPUSH BYTE.MAX_VALUE");
                super.visitIntInsn(opcode, newOperand);
            } else super.visitIntInsn(opcode, operand);
        }
    }
}
