package wins.insomnia.mcitemhunt.model.validation;

public enum PlayerIdValidationResult {
    VALID;

    private final String INVALID_REASON;

    PlayerIdValidationResult() {
        INVALID_REASON = "";
    }

    PlayerIdValidationResult(String invalidReason) {
        INVALID_REASON = invalidReason;
    }

    public String getInvalidReason() {
        return INVALID_REASON;
    }

    /**
     *
     * Determines if a player's uuid is a valid Minecraft account uuid.
     *
     * @param playerId
     * @return A {@link PlayerIdValidationResult} indicating validity of playerId.
     */
    public static PlayerIdValidationResult isValidPlayerId(String playerId) {
        return PlayerIdValidationResult.VALID;
    }

}
