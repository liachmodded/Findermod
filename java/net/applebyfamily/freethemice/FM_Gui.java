package net.applebyfamily.freethemice;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

//
// GuiBuffBar implements a simple status bar at the top of the screen which 
// shows the current buffs/debuffs applied to the character.
//
public class FM_Gui extends GuiScreen {

    public FM_GuiLabel[] labels;
    GuiTextField textField;
    GuiTextField rangeField;
    GuiButton doneButton;

    public FM_Gui() {
        super();

        mc = Minecraft.getMinecraft();
        textField = new GuiTextField(1, mc.fontRendererObj, 80, 80, 150, 20);
        textField.setText("");


        rangeField = new GuiTextField(2, mc.fontRendererObj, 80, 110, 150, 20);
        rangeField.setText("100");

        labels = new FM_GuiLabel[9];

        labels[0] = new FM_GuiLabel(mc.fontRendererObj, 10, 10);
        labels[0].setText("Press 'Enter' to search, 'Esc' to cancel/refresh, 'tab' to change text fields");

        labels[1] = new FM_GuiLabel(mc.fontRendererObj, 10, 30);
        labels[1].setText("Search for entities, blocks, IDs, players, and drops");

        labels[2] = new FM_GuiLabel(mc.fontRendererObj, 10, 50);
        labels[2].setText("Examples: 'drops', 'chest', 'horse', 'iron', 'ore'");

        labels[3] = new FM_GuiLabel(mc.fontRendererObj, 10, 85);
        labels[3].setText("Search for:");

        labels[4] = new FM_GuiLabel(mc.fontRendererObj, 10, 115);
        labels[4].setText("Max range:");


        labels[5] = new FM_GuiLabel(mc.fontRendererObj, 10, 150);
        labels[5].setText("*Range only applies to block search.");

        labels[6] = new FM_GuiLabel(mc.fontRendererObj, 10, 170);
        labels[6].setText("Searching for: Nothing");

        labels[7] = new FM_GuiLabel(mc.fontRendererObj, 10, 190);
        labels[7].setText("Current Range: 0");

        labels[8] = new FM_GuiLabel(mc.fontRendererObj, 10, 210);
        labels[8].setText("Found: 0");


        textField.setFocused(true);
        doneButton = new GuiButton(1, 10, 230, "Done");
        buttonList.add(doneButton);
        doneButton.enabled = true;

    }

    private static int parse(String st) {
        int t;
        try {
            t = Integer.parseInt(st);
        } catch (NumberFormatException e) {
            t = 30;
        }
        return t > 100 ? 100 : t;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int x, int y, float f) {

        drawDefaultBackground();

        labels[6].setText("Searching for: " + FinderMod.instance.searching);
        labels[7].setText("Current Range: " + FinderMod.instance.range);
        labels[8].setText("Found: " + FinderMod.instance.found);


        for (FM_GuiLabel l : labels) {
            if (l != null) {
                l.drawTextBox();
            }
        }
        textField.drawTextBox();
        rangeField.drawTextBox();

        super.drawScreen(x, y, f);
    }

    @Override
    public void keyTyped(char c, int i) throws IOException {
        super.keyTyped(c, i);

        if (i == 28) {
            mc.displayGuiScreen(null);
        } else if (i == 1) {
            if (!isStringNumber(rangeField.getText())) {
                rangeField.setText(Integer.toString(FinderMod.instance.range));
            }
        } else if (i == 15) {
            textField.setFocused(textField.isFocused());
            rangeField.setFocused(!textField.isFocused());
        }
        if (textField.isFocused()) {
            String t = textField.getText();
            textField.textboxKeyTyped(c, i);
        }
        if (rangeField.isFocused()) {
            String Pre = rangeField.getText();
            rangeField.textboxKeyTyped(c, i);
            if (!rangeField.getText().isEmpty()) {
                rangeField.setText(Pre);
            }
        }
    }

    public boolean isStringNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override protected void actionPerformed(GuiButton button) throws IOException {
        if (button == doneButton) {
            mc.displayGuiScreen(null);
        }
    }

    @Override public void onGuiClosed() {
        FinderMod.instance.searching = textField.getText();
        FinderMod.instance.range = parse(rangeField.getText());
    }
}
