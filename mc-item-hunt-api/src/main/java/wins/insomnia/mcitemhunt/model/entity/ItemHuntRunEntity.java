package wins.insomnia.mcitemhunt.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import wins.insomnia.mcitemhunt.model.dto.RunVerificationStatus;
import wins.insomnia.mcitemhunt.model.validation.PlayerIdValidationResult;
import wins.insomnia.mcitemhunt.model.validation.PlayerUsernameValidationResult;
import wins.insomnia.mcitemhunt.model.validation.WorldSeedValidationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * Class representation of a run from the db.
 *
 */
@Entity
@Table(name = "item_hunt_runs")
@Slf4j
public class ItemHuntRunEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "run_id")
    private Long runId;

    // uuid of minecraft account
    @Column(name = "player_uuid", nullable = false, length = 36)
    @Getter
    private String playerId;

    @Column(name = "player_username", nullable = false)
    @Getter
    private String playerUsername;

    @Getter
    @Column(name = "world_seed", nullable = false)
    private String worldSeed;

    @Setter
    @Getter
    @Column(name = "start_time", updatable = false)
    private Long startTime = System.currentTimeMillis();

    @Setter
    @Getter
    @Column(name = "end_time", updatable = false)
    private Long endTime = System.currentTimeMillis();

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private RunVerificationStatus verificationStatus = RunVerificationStatus.PENDING;

    @OneToMany(mappedBy = "run", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ItemHuntRunEventEntity> events = new ArrayList<>();

    /**
     *
     * Gets a list of all events for this run.
     * Remember to keep db connection open by putting the @{@link Transactional} annotation on the calling method.
     *
     * @return An immutable {@link List} of all events in the run.
     */
    public List<ItemHuntRunEventEntity> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public int getEventAmount() {
        return events.size();
    }

    public void removeEvent(ItemHuntRunEventEntity event) {
        events.remove(event);
        event.setRun(null);
    }

    public void addEvent(ItemHuntRunEventEntity event) {
        events.add(event);
        event.setRun(this);
    }


    public void setPlayerId(String playerId) {
        PlayerIdValidationResult validity = PlayerIdValidationResult.isValidPlayerId(playerId);
        if (validity == PlayerIdValidationResult.VALID) {
            this.playerId = playerId;
        } else {
            log.warn("Tried setting invalid player id: {}", validity.getInvalidReason());
        }
    }

    public void setPlayerUsername(String playerUsername) {
        PlayerUsernameValidationResult validity = PlayerUsernameValidationResult.isValidPlayerUsername(playerUsername);
        if (validity == PlayerUsernameValidationResult.VALID) {
            this.playerUsername = playerUsername;
        } else {
            log.warn("Tried setting invalid player username: {}", validity.getInvalidReason());
        }
    }

    public void setWorldSeed(String worldSeed) {
        WorldSeedValidationResult validity = WorldSeedValidationResult.isValidWorldSeed(worldSeed);
        if (validity == WorldSeedValidationResult.VALID) {
            this.worldSeed = worldSeed;
        } else {
            log.warn("Tried setting invalid world seed: {}", validity.getInvalidReason());
        }
    }

}
