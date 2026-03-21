package wins.insomnia.mcitemhunt.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wins.insomnia.mcitemhunt.model.dto.RunSummary;
import wins.insomnia.mcitemhunt.model.dto.RunVerificationStatus;
import wins.insomnia.mcitemhunt.model.dto.ItemHuntRunDTO;
import wins.insomnia.mcitemhunt.model.entity.ItemHuntRunEntity;
import wins.insomnia.mcitemhunt.model.repository.ItemHuntRunRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemHuntService {

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

        // grab and update username
        runData.setPlayerUsername(
                mojangService.getUsernameFromPlayerId(runData.getPlayerId())
        );

        // make entity
        ItemHuntRunEntity entity = new ItemHuntRunEntity();

        entity.setPlayerId(runData.getPlayerId());
        entity.setWorldSeed(runData.getWorldSeed());
        entity.setPlayerUsername(runData.getPlayerUsername());
        entity.setStartTime(System.currentTimeMillis());

        // store in db
        return runRepository.save(entity);
    }
}
