package net.applebyfamily.freethemice;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

class FM_GuiLabel extends GuiTextField {

    FM_GuiLabel(FontRenderer par1FontRenderer, int x, int y) {
        super(-1, par1FontRenderer, x, y, 50000, 20);
        this.setFocused(false);
        this.setEnabled(false);
        this.setEnableBackgroundDrawing(false);
        this.setMaxStringLength(Integer.MAX_VALUE);
        this.setDisabledTextColour(16777215);
    }

}
