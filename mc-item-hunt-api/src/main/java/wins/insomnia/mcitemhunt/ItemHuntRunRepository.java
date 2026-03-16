package wins.insomnia.mcitemhunt;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wins.insomnia.mcitemhunt.model.ItemHuntRunEntity;


/**
 *
 * Repository accessor class for runs in the db.
 *
 */
@Repository
public interface ItemHuntRunRepository extends JpaRepository<@NonNull ItemHuntRunEntity, @NonNull Long> {

}
