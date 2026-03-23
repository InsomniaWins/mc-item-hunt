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

    /**
     * The base url for the api. TODO: Should be changed through config file in the future.
     */
    private static final String API_URL = "http://localhost:8080/api/itemhunt";
    /**
     * The http client used to communicate with the api.
     */
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    /**
     * Used to communicate with api to prove this is a real account.
     * Remains valid for 24 hours at most.
     * Invalidates after user does not communicate with the api for an hour.
     */
    private static String sessionToken = null;

    /**
     * @return Boolean describing the state of the value of the sessionToken.
     */
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

    /**
     * @return Player's uuid without any hyphens.
     */
    public static String getTrimmedPlayerId() {
        return Minecraft.getInstance().getUser().getProfileId().toString().replace("-", "");
    }

    /**
     * Begins the process of getting a session token with the api.
     */
    public static void authenticateApiConnection() {

        String playerId = getTrimmedPlayerId();

        fetchServerId(playerId).thenAccept(serverId -> {

            serverId = serverId.trim();
            authenticateWithMojang(serverId);

        }).exceptionally(exception -> {
            ItemHunt.LOGGER.error("Failed to authenticate api connection!", exception);
            return null;
        });
    }

    /**
     * Tells Mojang api the mod wants to tell the api that this is an official account,
     * @param serverId The server id returned from the api to give to Mojang.
     */
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

    /**
     * Final step in authentication. Gets the api session token for further communication.
     * @param serverId The server id returned from the api.
     */
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
                        ItemHunt.LOGGER.info("Successfully authenticated api connection! Token stored!");
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
