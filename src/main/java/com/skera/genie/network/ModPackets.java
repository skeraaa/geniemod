package com.skera.genie.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import com.skera.genie.GenieMod;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import io.netty.buffer.Unpooled;

public class ModPackets {
	public static final Identifier OPEN_GENIE_SCREEN_ID = new Identifier(GenieMod.MOD_ID, "open_genie_screen");
	public static final Identifier WISH_SELECTION_ID = new Identifier(GenieMod.MOD_ID, "wish_selection");
	
	public static void register() {
		ServerPlayNetworking.registerGlobalReceiver(WISH_SELECTION_ID, (server, player, handler, buf, responseSender) -> {
			String wishId = buf.readString();
			int lampSlot = buf.readInt();
			
			server.execute(() -> {
				WishHandler.handleWishSelection(player, wishId, lampSlot);
			});
		});
	}
	
	public static void openGenieScreen(ServerPlayerEntity player) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		ServerPlayNetworking.send(player, new OpenGenieScreenPayload(buf));
	}
	
	public record OpenGenieScreenPayload(PacketByteBuf buf) implements CustomPayload {
		public static final Id<OpenGenieScreenPayload> PACKET_ID = new Id<>(OPEN_GENIE_SCREEN_ID);
		
		@Override
		public Id<? extends CustomPayload> getId() {
			return PACKET_ID;
		}
	}
	
	public record WishSelectionPayload(String wishId, int lampSlot) implements CustomPayload {
		public static final Id<WishSelectionPayload> PACKET_ID = new Id<>(WISH_SELECTION_ID);
		
		@Override
		public Id<? extends CustomPayload> getId() {
			return PACKET_ID;
		}
	}
}
