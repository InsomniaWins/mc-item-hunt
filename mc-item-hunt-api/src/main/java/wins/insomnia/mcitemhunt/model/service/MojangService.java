package wins.insomnia.mcitemhunt.model.service;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MojangService {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     *
     * Gets the mojang profile information of the minecraft account with the id of playerId;
     *
     * @param playerId The uuid of the Mojang account associated with the Minecraft account.
     * @return a {@link MojangProfile} of the Mojang account associated with the Minecraft account.
     */
    @Nullable
    public MojangProfile getMojangProfile(String playerId) {
        String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + playerId;
        try {
            return restTemplate.getForObject(url, MojangProfile.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the username of the Minecraft account with an uuid of playerId.
     * @param playerId The uuid / player id of the Mojang account associated with the Minecraft account.
     * @return The username of the Mojang / Minecraft account with uuid of playerId.
     */
    public String getUsernameFromPlayerId(String playerId) {
        final String fallbackUsername = "NULL USERNAME";
        MojangProfile mojangProfile = getMojangProfile(fallbackUsername);

        if (mojangProfile != null) {
            return mojangProfile.name();
        }

        return fallbackUsername;
    }

    public record MojangProfile(String id, String name) {
        @Override
        public boolean equals(Object obj) {

            if (obj instanceof MojangProfile(String otherId, String otherName)) {
                return id.equals(otherId) && name.equals(otherName);
            }

            return false;
        }
    };
}
