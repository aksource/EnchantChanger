package ak.enchantchanger.client.forge.model;

import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;

public interface IModelCustom
{
    String getType();
    @SideOnly(CLIENT)
    void renderAll();
    @SideOnly(CLIENT)
    void renderOnly(String... groupNames);
    @SideOnly(CLIENT)
    void renderPart(String partName);
    @SideOnly(CLIENT)
    void renderAllExcept(String... excludedGroupNames);
}