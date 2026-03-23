package wins.insomnia.mcitemhunt.model.dto;

import lombok.Getter;
import lombok.Setter;
import wins.insomnia.mcitemhunt.model.dto.runevent.ItemHuntRunEventDTO;
import wins.insomnia.mcitemhunt.model.validation.PlayerUsernameValidationResult;

import java.util.ArrayList;

@Getter
@Setter
public class ItemHuntRunDTO {

    /**
     * Used to verify the user updating this run is the owner of the run.
     * Is NULL when run is finalized.
     */
    private String sessionToken = null;
    private String playerId;
    private String worldSeed;
    private String playerUsername;
    private Long startTime;
    private Long endTime;
    private RunVerificationStatus verificationStatus;
    private final ArrayList<ItemHuntRunEventDTO> events = new ArrayList<>();

    public boolean hasUsername() {
        return playerUsername != null
                && !playerUsername.isEmpty()
                && PlayerUsernameValidationResult.VALID == PlayerUsernameValidationResult.isValidPlayerUsername(playerUsername);
    }

    public boolean hasSessionToken() {
        return sessionToken != null;
    }
}
