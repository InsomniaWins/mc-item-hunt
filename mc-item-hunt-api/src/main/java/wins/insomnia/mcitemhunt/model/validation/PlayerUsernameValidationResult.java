package wins.insomnia.mcitemhunt.model.validation;

public enum PlayerUsernameValidationResult {
    VALID,
    EMPTY("Username is null or empty."),
    TOO_SHORT("Username is too short."),
    TOO_LONG("Username is too long."),
    SPECIAL_CHARACTERS("Username contains invalid characters. (Valid characters: [a-z, A-Z, 0-9, _])");

    private final String INVALID_REASON;

    PlayerUsernameValidationResult() {
        INVALID_REASON = "";
    }

    PlayerUsernameValidationResult(String invalidReason) {
        INVALID_REASON = invalidReason;
    }

    public String getInvalidReason() {
        return INVALID_REASON;
    }



    /**
     *
     * Determines if a player's username is a valid Minecraft account username.
     *
     * @param playerUsername
     * @return A {@link PlayerUsernameValidationResult} indicating validity of playerUsername.
     */
    public static PlayerUsernameValidationResult isValidPlayerUsername(String playerUsername) {

        if (playerUsername == null || playerUsername.isEmpty()) {
            return PlayerUsernameValidationResult.EMPTY;
        }

        int usernameLength = playerUsername.length();
        if (usernameLength < 3) {
            return PlayerUsernameValidationResult.TOO_SHORT;
        }

        if (usernameLength > 16) {
            return PlayerUsernameValidationResult.TOO_LONG;
        }

        if (!playerUsername.matches("^[a-zA-Z0-9_]+$")) {
            return PlayerUsernameValidationResult.SPECIAL_CHARACTERS;
        }

        return PlayerUsernameValidationResult.VALID;
    }
}
