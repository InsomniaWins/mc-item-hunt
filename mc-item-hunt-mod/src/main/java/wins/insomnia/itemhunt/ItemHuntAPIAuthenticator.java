package wins.insomnia.itemhunt;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.Minecraft;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;


/**
 * Class to handle mojang & api authentication & sessions.
 */
public class ItemHuntAPIAuthenticator {

    private static final String API_URL = "http://localhost:8080/api/itemhunt";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static String sessionToken = null;

    public boolean hasSessionToken() {
        return sessionToken != null;
    }


    /**
     *
     * Gets a server id from the api for session authentication.
     * Uses a {@link CompletableFuture} to assure main thread is not frozen.
     *
     * @param playerId The uuid of the player to validate.
     * @return A {@link CompletableFuture} of a server id from the api.
     */
    public static CompletableFuture<String> fetchServerId(String playerId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/auth/request-id?playerId=" + playerId))
                .GET()
                .build();

        return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    public static String getTrimmedPlayerId() {
        return Minecraft.getInstance().getUser().getProfileId().toString().replace("-", "");
    }

    public static void authenticateApiConnection() {

        String playerId = getTrimmedPlayerId();

        fetchServerId(playerId).thenAccept(serverId -> {

            serverId = serverId.trim();
            System.out.println("Got authenticated server id from api: " + serverId);
            authenticateWithMojang(serverId);

        }).exceptionally(exception -> {
            ItemHunt.LOGGER.error("Failed to authenticate api connection!", exception);
            return null;
        });
    }

    private static void authenticateWithMojang(String serverId) {

        Minecraft minecraft = Minecraft.getInstance();
        MinecraftSessionService sessionService = minecraft.services().sessionService();

        try {
            sessionService.joinServer(
                    minecraft.getUser().getProfileId(),
                    minecraft.getUser().getAccessToken(),
                    serverId
            );

            finalizeApiAuthentication(serverId);

        } catch (AuthenticationException e) {

            ItemHunt.LOGGER.error("Failed to authenticate api connection!", e);

        }

    }

    private static void finalizeApiAuthentication(String serverId) {

        String playerId = getTrimmedPlayerId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/auth/verify?serverId=" + serverId + "&playerId=" + playerId))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        sessionToken = response.body();
                        System.out.println("Session Authenticated! Token stored.");
                    } else {
                        System.err.println(
                                "Authentication failed at API: \n"
                                + "code: " + response.statusCode() + "\n"
                                + "message: " + response.body()
                        );
                    }
                });

    }



}
