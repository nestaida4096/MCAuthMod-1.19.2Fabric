package nest.authmod;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.print.attribute.standard.JobKOctets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class AuthChecker {

    private static List<String> authenticatedPlayers = new ArrayList<String>() {
    };
    private static int tickCount = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCount++;
            if (tickCount >= 100) { // 10秒ごと (1tick = 50ms, 20tick = 1秒)
                fetchAuthenticatedPlayers();
                tickCount = 0;
            }
        });
    }

    private static void fetchAuthenticatedPlayers() {
        try {
            URL url = new URL("http://127.0.0.1:8080/authenticatedPlayers");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            updateAuthenticatedPlayers(response.toString());

        } catch (Exception e) {
            System.err.println("認証リストの取得に失敗しました: " + e.getMessage());
        }
    }

    private static void updateAuthenticatedPlayers(String json) throws JsonProcessingException {
        authenticatedPlayers.clear();
        ObjectMapper objectMapper = new ObjectMapper();
        String[] Object = objectMapper.readValue(json, String[].class); // 配列に変換
        authenticatedPlayers.addAll(Arrays.asList(Object));
        authenticatedPlayers.replaceAll(String::trim);
    }

    public static boolean isAuthenticated(String playerName) {
        return authenticatedPlayers.contains(playerName);
    }
}
