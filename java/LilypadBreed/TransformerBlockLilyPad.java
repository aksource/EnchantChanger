package LilypadBreed;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.Opcodes;

public class TransformerBlockLilyPad implements IClassTransformer, Opcodes {

	private static final String TARGET_CLASS_NAME = "BlockLilyPad";

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (!transformedName.equals(TARGET_CLASS_NAME)) {
			return basicClass;
		}
		return null;
	}

	private byte[] replaceMethodonBlockActivated(byte[] basicClass)
	{

		return basicClass;
	}
}