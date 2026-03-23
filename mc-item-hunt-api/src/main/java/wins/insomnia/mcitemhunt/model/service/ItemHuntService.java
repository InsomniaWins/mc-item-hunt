package wins.insomnia.mcitemhunt.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wins.insomnia.mcitemhunt.manager.ActiveRunsManager;
import wins.insomnia.mcitemhunt.model.dto.RunSummary;
import wins.insomnia.mcitemhunt.model.dto.RunVerificationStatus;
import wins.insomnia.mcitemhunt.model.dto.ItemHuntRunDTO;
import wins.insomnia.mcitemhunt.model.dto.runevent.ItemHuntRunEventDTO;
import wins.insomnia.mcitemhunt.model.entity.ItemHuntRunEntity;
import wins.insomnia.mcitemhunt.model.entity.ItemHuntRunEventEntity;
import wins.insomnia.mcitemhunt.model.repository.ItemHuntRunRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemHuntService {

    private final ActiveRunsManager activeRunsManager;
    private final MojangService mojangService;
    private final ItemHuntRunRepository runRepository;

    @Transactional(readOnly = true)
    public List<Long> getAllRunIds() {
        return runRepository.findAllRunIds();
    }

    @Transactional(readOnly = true)
    public List<RunSummary> getRunsWithVerificationStatus(RunVerificationStatus verificationStatus) {
        return runRepository.findRunSummariesByVerificationStatus(verificationStatus);
    }

    @Transactional(readOnly = true)
    public List<RunSummary> getRunsPendingVerification() {
        return getRunsWithVerificationStatus(RunVerificationStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public List<RunSummary> getVerifiedRuns() {
        return getRunsWithVerificationStatus(RunVerificationStatus.VERIFIED);
    }

    /**
     *
     * Creates a new run, enters it into the db, returns the entity obj.
     *
     * @param runData The initial data for the run.
     * @return A new {@link ItemHuntRunEntity} entered into the db.
     */
    @Transactional
    public ItemHuntRunEntity saveRun(ItemHuntRunDTO runData) {

        if (!runData.hasUsername()) {
            // grab and update username
            runData.setPlayerUsername(
                    mojangService.getUsernameFromPlayerId(runData.getPlayerId())
            );
        }

        // make entity
        ItemHuntRunEntity entity = new ItemHuntRunEntity();

        entity.setPlayerId(runData.getPlayerId());
        entity.setWorldSeed(runData.getWorldSeed());
        entity.setPlayerUsername(runData.getPlayerUsername());

        // add event entities
        for (ItemHuntRunEventDTO event : runData.getEvents()) {
            ItemHuntRunEventEntity eventEntity = new ItemHuntRunEventEntity();

            eventEntity.setRun(entity);
            eventEntity.setEventTimestamp(event.getEventTimestamp());
            eventEntity.setEventType(event.getEventType());
            eventEntity.setEventData(
                    event.getEntityDataToSave()
            );

            entity.addEvent(eventEntity);
        }

        // store in db
        return runRepository.save(entity);
    }


    /**
     *
     * Starts a new run and stores it in memory.
     *
     * @param runData The data/info for the new run.
     */
    public void startNewRun(ItemHuntRunDTO runData) {
        runData.setPlayerUsername(mojangService.getUsernameFromPlayerId(runData.getPlayerId()));
        runData.setStartTime(System.currentTimeMillis());
        activeRunsManager.addRun(runData.getPlayerId(), runData);
    }

    /**
     *
     * Adds a run event to the active run belonging to the player with uuid of playerId.
     *
     * @param playerId The uuid of the Minecraft account with an active run.
     * @param eventData The event to add to the active run.
     */
    public int addEventToRun(String playerId, ItemHuntRunEventDTO eventData) {
        ItemHuntRunDTO runData = activeRunsManager.getRun(playerId);
        if (runData == null) {
            log.error("Cannot add event: {} to run with player id of {}, because there is no active run for this player!", eventData, playerId);
            return 1;
        }

        runData.getEvents().add(eventData);
        return 0;
    }
}
