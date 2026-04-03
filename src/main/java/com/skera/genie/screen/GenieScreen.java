package com.skera.genie.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import com.skera.genie.GenieMod;

public class GenieScreen extends Screen {
	private static final Identifier TEXTURE = new Identifier(GenieMod.MOD_ID, "textures/gui/genie_screen.png");
	
	public GenieScreen() {
		super(Text.literal("Джин"));
	}
	
	@Override
	protected void init() {
		super.init();
		
		// TODO: Добавить кнопки с желаниями
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderBackground(context);
		super.render(context, mouseX, mouseY, delta);
		
		int x = (this.width - 256) / 2;
		int y = (this.height - 166) / 2;
		
		context.drawText(this.textRenderer, "Выберите желание", x + 80, y + 20, 0x404040, true);
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}
	
	@Override
	public void close() {
		super.close();
	}
}
