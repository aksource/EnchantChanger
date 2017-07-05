package ak.enchantchanger.client.gui;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.inventory.EcContainerMakoReactor;
import ak.enchantchanger.network.MessageRFStepping;
import ak.enchantchanger.network.PacketHandler;
import ak.enchantchanger.tileentity.EcTileEntityMakoReactor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * SS2導入時の魔晄炉のGUIクラス
 * Created by A.K. on 14/10/07.
 */
public class EcGuiMakoReactorGF extends GuiContainer {
    private static final ResourceLocation GUI = new ResourceLocation(Constants.EcAssetsDomain, Constants.EcGuiMako);
    private EcTileEntityMakoReactor tileEntity;
    private InventoryPlayer inventoryPlayer;
    private EcGuiMakoReactorButton prevButton;
    private EcGuiMakoReactorButton nextButton;

    public EcGuiMakoReactorGF(InventoryPlayer invPlayer, EcTileEntityMakoReactor te) {
        super(new EcContainerMakoReactor(invPlayer, te));
        inventoryPlayer = invPlayer;
        tileEntity = te;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.prevButton = new EcGuiMakoReactorButton(1, this.guiLeft + 126, this.guiTop + 24, false);
        this.nextButton = new EcGuiMakoReactorButton(2, this.guiLeft + 144, this.guiTop + 24, true);
        this.buttonList.add(this.prevButton);
        this.buttonList.add(this.nextButton);
    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton button) {
        boolean pushed = false;
        int step;
        step = EcTileEntityMakoReactor.STEP_RF_VALUE;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            step *= 10;
        }
        if (button.equals(this.prevButton)) {
            tileEntity.stepOutputMaxRFValue(-step);
            pushed = true;
        } else if (button.equals(this.nextButton)) {
            tileEntity.stepOutputMaxRFValue(step);
            pushed = true;
        }

        if (pushed) {
            PacketHandler.INSTANCE.sendToServer(new MessageRFStepping(tileEntity.getOutputMaxRFValue(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        fontRenderer.drawString(I18n.translateToLocal(tileEntity.getName()), 25, 3, 4210752);
        fontRenderer.drawString(I18n.translateToLocal(inventoryPlayer.getName()), 8, ySize - 96 + 2, 4210752);
        int x = this.guiLeft;
        int y = this.guiTop;
        if (mouseX >= x + 11 && mouseX <= x + 20 && mouseZ >= y + 21 && mouseZ <= y + 71) {
            List<String> list = new ArrayList<>();
            list.add(String.format("Mako : %dmB", tileEntity.tank.getFluidAmount()));
            drawHoveringText(list, mouseX - x, mouseZ - y, fontRenderer);
        }
        fontRenderer.drawString(I18n.translateToLocal(String.format("Max %d Speed/t", tileEntity.getOutputMaxRFValue())), 115, 6, 4210752);
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

        this.drawTexturedModalRect(x + 81, y + 26, 176, 0, smelt, 41);

        if (!tileEntity.tank.isEmpty()) {
            int fluidAmount = tileEntity.getFluidAmountScaled(50);
            this.drawTexturedModalRect(x + 11, y + 21 + (50 - fluidAmount), 177, 70, 9, fluidAmount);
        }
    }

    @Override
    protected void keyTyped(char c, int keycode) {
        if (keycode == 1 || keycode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
            Minecraft.getMinecraft().player.closeScreen();
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.prevButton.enabled = tileEntity.getOutputMaxRFValue() - EcTileEntityMakoReactor.STEP_RF_VALUE >= 10;
        this.nextButton.enabled = tileEntity.getOutputMaxRFValue() + EcTileEntityMakoReactor.STEP_RF_VALUE <= EcTileEntityMakoReactor.MAX_OUTPUT_RF_VALUE;
        EntityPlayer player = mc.player;
        if (!player.isEntityAlive() || player.isDead) {
            player.closeScreen();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
