package wins.insomnia.mcitemhunt.model.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wins.insomnia.mcitemhunt.model.dto.RunSummary;
import wins.insomnia.mcitemhunt.model.entity.ItemHuntRunEntity;

import java.util.List;


/**
 *
 * Repository accessor class for runs in the db.
 *
 */
@Repository
public interface ItemHuntRunRepository extends JpaRepository<@NonNull ItemHuntRunEntity, @NonNull Long> {

    List<ItemHuntRunEntity> findByPlayerId(String playerId);

    @Query("SELECT r.runId FROM ItemHuntRunEntity r")
    List<Long> findAllRunIds();

    @Query("SELECT new wins.insomnia.mcitemhunt.model.dto.RunSummary(r.runId, r.playerId, r.worldSeed, r.startTime) FROM ItemHuntRunEntity r")
    List<RunSummary> findAllRunSummaries();

}
