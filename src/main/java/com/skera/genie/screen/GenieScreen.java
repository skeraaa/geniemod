package com.skera.genie.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GenieScreen extends Screen {
	private static final Identifier TEXTURE = new Identifier("genie", "textures/gui/genie_gui.png");

	public GenieScreen() {
		super(Text.literal("Выбор желания"));
	}

	@Override
	protected void init() {
		// Добавление кнопок категорий и желаний
		// addDrawableChild(Button.builder(Text.literal("Богатство"), btn -> {}).positions(...));
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderBackground(context);
		super.render(context, mouseX, mouseY, delta);
		context.drawText(this.textRenderer, "Выбери желание,", width / 2 - 40, height / 2 - 50, 0xFFFFFF, true);
	}
}
