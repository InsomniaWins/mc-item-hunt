package wins.insomnia.mcitemhunt.config;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Configuration;
import wins.insomnia.mcitemhunt.model.dto.runevent.ItemHuntRunEventDTOFactory;

/**
 *
 * Used solely to finalize the ItemHuntRunEventFactory's registry map.
 *
 */
@Configuration
public class FinalizeEventRegistryConfig implements SmartInitializingSingleton {
    @Override
    public void afterSingletonsInstantiated() {
        ItemHuntRunEventDTOFactory.finalizeRegistration();
    }
}
