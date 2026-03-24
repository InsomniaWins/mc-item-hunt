package wins.insomnia.mcitemhunt.model.validation;

import wins.insomnia.mcitemhunt.model.dto.ItemHuntRunDTO;

public class ItemHuntValidation {

    public enum ItemHuntValidationResult {
        INVALID_PLAYER_ID,
        INVALID_WORLD_SEED,
        VALID;

        public boolean isValid() {
            return this == VALID;
        }
    }

    /**
     * Determines if the runData object contains enough information to start a new run.
     * @param runData The data of the run to start.
     * @return ItemHuntValidationResult determining if the runData object is valid to start a run.
     */
    public static ItemHuntValidationResult validateStartRun(ItemHuntRunDTO runData) {
        if (runData.getPlayerId() == null) {
            return ItemHuntValidationResult.INVALID_PLAYER_ID;
        }

        if (runData.getWorldSeed() == null) {
            return ItemHuntValidationResult.INVALID_WORLD_SEED;
        }

        return ItemHuntValidationResult.VALID;
    }


}
