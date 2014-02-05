package ak.EnchantChanger;


public class EcItemSephirothSwordImit extends EcItemSword
{
	public EcItemSephirothSwordImit()
	{
		super(ToolMaterial.IRON);
		this.setMaxDamage(ToolMaterial.IRON.getMaxUses());
        this.setTextureName(EnchantChanger.EcTextureDomain + "ImitateMasamuneBlade");
	}
}