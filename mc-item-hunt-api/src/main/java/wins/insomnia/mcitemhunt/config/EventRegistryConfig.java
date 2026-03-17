package wins.insomnia.mcitemhunt.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import wins.insomnia.mcitemhunt.model.entity.runevent.ItemHuntRunEventFactory;
import wins.insomnia.mcitemhunt.model.entity.runevent.player.PlayerTransformItemHuntRunEvent;

/**
 * Where ItemHuntRunEvents should be registered.
 */
@Configuration
public class EventRegistryConfig {

    @PostConstruct
    public void init() {
        try {
            ItemHuntRunEventFactory.registerEvent(
                    "player_transform",
                    PlayerTransformItemHuntRunEvent::new
            );
        } catch (ItemHuntRunEventFactory.RegistrationException exception) {
            throw new RuntimeException("Failed to initialize the event factory!");
        }
    }

}
