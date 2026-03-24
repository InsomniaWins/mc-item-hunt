package wins.insomnia.itemhunt.api;

import com.google.gson.Gson;
import wins.insomnia.itemhunt.ItemHunt;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ItemHuntAPICommunication {

    /**
     * The base url for the api. TODO: Should be changed through config file in the future.
     */
    private static final String API_URL = "http://localhost:8080/api/itemhunt";
    /**
     * The http client used to communicate with the api.
     */
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();


    /**
     * Automates error logging for no session token being set.
     * Returns true if the token is missing. Returns false otherwise.
     * @return A boolean describing if the session token has not been set (true) or has (false).
     */
    private static boolean checkMissingSessionToken() {
        if (!ItemHuntAPIAuthenticator.hasSessionToken()) {
            ItemHunt.LOGGER.error("No session token has been set.");
            return true;
        }
        return false;
    }

    public static void startRun() {

        if (checkMissingSessionToken()) {
            return;
        }
        String sessionToken = ItemHuntAPIAuthenticator.getSessionToken();

        Map<String, Object> bodyMap = Map.of(
                "playerId", ItemHuntAPIAuthenticator.getTrimmedPlayerId(),
                "worldSeed", 0L
        );
        String body = new Gson().toJson(bodyMap);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getApiUrl() + "/start-run"))
                .header("Content-Type", "application/json")
                .header("Authorization", sessionToken)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        System.out.println("Run successfully started on API!");
                    } else {
                        System.err.println("Failed to start run. Status: " + response.statusCode());
                    }
                }).exceptionally(exception -> {
                    System.err.println("Failed to start run. Error: " + exception);
                    return null;
                });
    }

    public static void addEventToRun() {

        if (checkMissingSessionToken()) {
            return;
        }
        String sessionToken = ItemHuntAPIAuthenticator.getSessionToken();

        Map<String, Object> bodyMap = Map.of(

        );

        String body = new Gson().toJson(bodyMap);

    }





    public static HttpClient getHttpClient() {
        return HTTP_CLIENT;
    }
    public static String getApiUrl() {
        return API_URL;
    }

}
