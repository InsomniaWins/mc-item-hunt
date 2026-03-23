package wins.insomnia.mcitemhunt.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import wins.insomnia.mcitemhunt.model.dto.runevent.ItemHuntRunEventDTOFactory;
import wins.insomnia.mcitemhunt.model.dto.runevent.player.PlayerTransformItemHuntRunEventDTO;

/**
 * Where ItemHuntRunEvents should be registered.
 */
@Configuration
public class EventRegistryConfig {

    @PostConstruct
    public void init() {
        try {
            ItemHuntRunEventDTOFactory.registerEvent(
                    "player_transform",
                    PlayerTransformItemHuntRunEventDTO::new
            );
        } catch (ItemHuntRunEventDTOFactory.RegistrationException exception) {
            throw new RuntimeException("Failed to initialize the event factory!");
        }
    }

}
