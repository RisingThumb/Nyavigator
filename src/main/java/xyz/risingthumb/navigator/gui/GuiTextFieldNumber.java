package xyz.risingthumb.navigator.gui;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextFieldNumber extends GuiTextField {

	public GuiTextFieldNumber(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width,
			int par6Height) {
		super(componentId, fontrendererObj, x, y, par5Width, par6Height);
	}
	
	public void keyTyped(char typedChar, int keyCode) {
		if (!this.isFocused()) return; // If not focused get out
		if (typedChar=='-' && this.getText().length() == 0)	// Negatives
			this.textboxKeyTyped(typedChar, keyCode);
		else if (typedChar==8)								// Backspace
			this.textboxKeyTyped(typedChar, keyCode);
		else if (typedChar >= 48 && typedChar <= 57)		// Numbers
			this.textboxKeyTyped(typedChar, keyCode);
		else if (keyCode == Keyboard.KEY_DOWN || keyCode == Keyboard.KEY_UP ||
				 keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_RIGHT)
			this.textboxKeyTyped(typedChar, keyCode);
	}
	
	@Override
	public String getText() {
		String text = super.getText();
		if (text.equals("") || text.equals("-"))
			return "0";
		return text;
	}

}
