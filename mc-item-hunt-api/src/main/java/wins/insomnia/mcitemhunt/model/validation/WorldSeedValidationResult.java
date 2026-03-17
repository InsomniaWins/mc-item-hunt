package wins.insomnia.mcitemhunt.model.validation;

public enum WorldSeedValidationResult {
    VALID;

    private final String INVALID_REASON;

    WorldSeedValidationResult() {
        INVALID_REASON = "";
    }

    WorldSeedValidationResult(String invalidReason) {
        INVALID_REASON = invalidReason;
    }

    public String getInvalidReason() {
        return INVALID_REASON;
    }



    /**
     *
     * Determines if a world seed is valid.
     *
     * @param worldSeed
     * @return A {@link WorldSeedValidationResult} indicating validity of worldSeed.
     */
    public static WorldSeedValidationResult isValidWorldSeed(String worldSeed) {
        return WorldSeedValidationResult.VALID;
    }

}
