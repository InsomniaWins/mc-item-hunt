package wins.insomnia.mcitemhunt;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
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

}
