package nest.authmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class AuthMod implements ModInitializer {
	public static final String MOD_ID = "authmod";

	@Override
	public void onInitialize() {
		System.out.println(MOD_ID + " has been initialized!！");
		AuthChecker.register(); // 認証リスト取得のイベント登録
		PlayerJoinHandler.register();
	}

}
