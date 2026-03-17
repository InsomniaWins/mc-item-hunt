package wins.insomnia.mcitemhunt.model.entity.runevent;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import wins.insomnia.mcitemhunt.model.entity.ItemHuntRunEntity;
import wins.insomnia.mcitemhunt.model.entity.ItemHuntRunEventEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 *
 * Simple item hunt event factory and registration class.
 * Uses concurrent data structures to ensure multi-thread-safety.
 *
 */
@Slf4j
public class ItemHuntRunEventFactory {

    private static final ConcurrentHashMap<String, Function<Map<String, Object>, ItemHuntRunEvent>> REGISTRATION_MAP = new ConcurrentHashMap<>();

    /**
     * Determines if new registrations/entries are allowed.
     */
    @Getter
    private static boolean finalized = false;

    public static void finalizeRegistration() {
        finalized = true;
    }

    public static void registerEvent(
            String type,
            Function<Map<String, Object>, ItemHuntRunEvent> constructionFunction
    ) throws RegistrationException {

        validateEventKey(type);

        if (isFinalized()) {
            throw new FinalizedRegistrationException(type);
        }

        if (REGISTRATION_MAP.containsKey(type)) {
            throw new AlreadyRegisteredException(type);
        }

        REGISTRATION_MAP.put(type, constructionFunction);
    }

    @Nullable
    public static ItemHuntRunEvent createEventFromDb(ItemHuntRunEventEntity entity) {
        Map<String, Object> entityData = entity.getEventData();
        entityData.put(ItemHuntRunEvent.MAP_KEY_TYPE, entity.getEventType());
        return createEvent(entityData);
    }

    @Nullable
    public static ItemHuntRunEvent createEvent(Map<String, Object> objectMap) {
        String type;

        try {
            type = (String) objectMap.get(ItemHuntRunEvent.MAP_KEY_TYPE);
            validateEventKey(type);
        } catch (Exception exception) {
            if (exception instanceof InvalidRegistrationKeyException regException) {
                log.error("Could not create event from object map! -> {}", regException.getREASON());
            } else {
                log.error("Could not create event from object map for unknown reason! -> {}", objectMap);
            }
            return null;
        }

        Function<Map<String, Object>, ItemHuntRunEvent> constructionFunction = REGISTRATION_MAP.get(type);
        if (constructionFunction == null) {
            log.error("Could not create event from object map! -> Event {} has not been registered.", type);
            return null;
        }

        return constructionFunction.apply(objectMap);
    }

    private static void validateEventKey(String eventKey) throws InvalidRegistrationKeyException {
        if (eventKey == null) {
            throw new InvalidRegistrationKeyException(null, InvalidRegistrationKeyException.InvalidReason.NULL);
        }

        if (eventKey.isEmpty()) {
            throw new InvalidRegistrationKeyException(eventKey, InvalidRegistrationKeyException.InvalidReason.EMPTY);
        }
    }

    public static class RegistrationException extends Exception {
        @Getter
        @Nullable
        private final String TYPE;

        public RegistrationException(@Nullable String type) {
            TYPE = type;
        }
    }

    /**
     * Thrown when the registration map has already been finalized and is not accepting new entries/registrations.
     */
    public static class FinalizedRegistrationException extends RegistrationException {
        public FinalizedRegistrationException(@Nullable String type) {
            super(type);
        }
    }

    public static class InvalidRegistrationKeyException extends RegistrationException {

        public enum InvalidReason {
            EMPTY("Event key is empty."),
            NULL("Event key is null.");

            final String REASON_STRING;

            InvalidReason(String reasonString) {
                REASON_STRING = reasonString;
            }

            @Override
            public String toString() {
                return REASON_STRING;
            }
        }

        @Getter
        private final InvalidReason REASON;

        public InvalidRegistrationKeyException(String type, InvalidReason reason) {
            super(type);
            REASON = reason;
        }
    }

    public static class AlreadyRegisteredException extends RegistrationException {
        public AlreadyRegisteredException(String type) {
            super(type);
        }
    }
}
