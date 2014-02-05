package Booster;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemBooster extends ItemArmor {
	private String boostername;
	public ItemBooster(ItemArmor.ArmorMaterial par2EnumArmorMaterial, int par3, int par4, String name) {
		super(par2EnumArmorMaterial, par3, par4);
		this.setTextureName(Booster.TextureDomain + name);
		boostername = name;
	}
	//うまく動かない。
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String layer)
	{
		String st = "";
		if(boostername.equals("Booster08") /*stack.getItem() == Booster.Booster08*/)
			st = (layer.equals(""))? Booster.TextureDomain + Booster.Armor08_1:Booster.TextureDomain + Booster.Armor08_2;
		else if(boostername.equals("Booster20")/*stack.getItem() == Booster.Booster20*/)
			st = (layer.equals(""))? Booster.TextureDomain + Booster.Armor20_1:Booster.TextureDomain + Booster.Armor20_2;
    	return st;
	}
}
