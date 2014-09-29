package ak.EnchantChanger.Client.gui;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.inventory.EcContainerMakoReactor;
import ak.EnchantChanger.tileentity.EcTileEntityMakoReactor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A.K. on 14/09/23.
 */
public class EcGuiMakoReactor extends GuiContainer {
    private EcTileEntityMakoReactor tileEntity;
    private InventoryPlayer inventoryPlayer;
    private static final ResourceLocation GUI = new ResourceLocation(EnchantChanger.EcAssetsDomain,EnchantChanger.EcGuiMako);

    public EcGuiMakoReactor(InventoryPlayer invPlayer, EcTileEntityMakoReactor te) {
        super(new EcContainerMakoReactor(invPlayer, te));
        inventoryPlayer = invPlayer;
        tileEntity = te;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        fontRendererObj.drawString(StatCollector.translateToLocal(tileEntity.getInventoryName()), 115, 6, 4210752);
        fontRendererObj.drawString(StatCollector.translateToLocal(inventoryPlayer.getInventoryName()), 8, ySize - 96 + 2, 4210752);
        int x = this.guiLeft;
        int y = this.guiTop;
        if (mouseX >= x + 11 && mouseX <= x + 20 && mouseZ >= y + 21 && mouseZ <= y + 71) {
            List<String> list = new ArrayList<>();
            list.add(String.format("Mako : %dmB", tileEntity.tank.getFluidAmount()));
            drawHoveringText(list, mouseX - x, mouseZ - y, fontRendererObj);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GUI);
        int x = this.guiLeft;
        int y = this.guiTop;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        if (tileEntity.isSmelting()) {
            this.drawTexturedModalRect(x + 45, y + 36, 176, 55, 14, 14);
        }

        int smelt = tileEntity.getSmeltingTimeScaled(33);

        this.drawTexturedModalRect(x + 81, y + 21, 176, 0, smelt, 41);

        if (!tileEntity.tank.isEmpty()) {
            int fluidAmount = tileEntity.getFluidAmountScaled(50);
            this.drawTexturedModalRect(x + 11, y + 21 + (50 - fluidAmount), 177, 70, 9, fluidAmount);
        }
    }

    @Override
    protected void keyTyped(char c, int keycode) {
        if (keycode == 1 || keycode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!Minecraft.getMinecraft().thePlayer.isEntityAlive() || Minecraft.getMinecraft().thePlayer.isDead) {
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
