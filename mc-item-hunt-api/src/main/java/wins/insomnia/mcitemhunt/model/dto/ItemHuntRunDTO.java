package wins.insomnia.mcitemhunt.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemHuntRunDTO {

    private String playerId;
    private String worldSeed;
    /**
     * The username of the player. Only fetch the username right before saving into the db.
     * If any dev sees this, and you're considering setting this: DON'T. (Unless you know what
     * you're doing.) It likely auto-sets already at some point.
     */
    private String playerUsername;

    // start time is grabbed when run is entered into the db
    //private Long startTime;

}
