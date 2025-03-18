package nest.authmod;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.text.Text;

public class PlayerJoinHandler {

    public static void register() {
        // イベントリスナーを修正
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            checkAuthentication(handler, server);
        });
    }

    private static void checkAuthentication(ServerPlayNetworkHandler handler, MinecraftServer server) {
        String playerName = handler.player.getName().getString();
        System.out.println("Join: "+playerName);
        if (!AuthChecker.isAuthenticated(playerName)) {
            server.execute(() -> { // メインスレッドで処理する
                handler.disconnect(Text.of("あなたは認証されていません。管理者の指示に従って認証してください。"));
            });
        }
    }
}