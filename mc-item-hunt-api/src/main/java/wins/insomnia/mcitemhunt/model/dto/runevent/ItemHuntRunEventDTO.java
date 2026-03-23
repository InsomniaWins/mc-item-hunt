package wins.insomnia.mcitemhunt.model.dto.runevent;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Used to read json data of the ItemHuntRunEventEntity.
 * THIS IS NOT USED AS AN ENTITY IN THE DB.
 * <p>
 *     For data which is stored outside the json column in the run event db table,
 *     access using the entity class directly.
 * </p>
 *
 */
public abstract class ItemHuntRunEventDTO {

    /**
     * The key string in the json object map that determines the type of event object to construct.
     */
    public static final String MAP_KEY_TYPE = "type";

    public abstract String getEventType();
    public abstract void readObjectMap(Map<String, Object> objectMap);
    public abstract void writeObjectMap(Map<String, Object> objectMap);

    @Getter
    @Setter
    private Long eventTimestamp;

    public ItemHuntRunEventDTO(Map<String, Object> objectMap) {
        readObjectMap(objectMap);
    }


    //region These method must not be overridden by child classes unless you know what you're doing!
    /**
     * @return A {@link Map}<{@link String}, {@link Object}> of data to be stored as json in the db.
     */
    public Map<String, Object> toObjectMap() {
        Map<String, Object> objectMap = getEntityDataToSave();
        objectMap.put(MAP_KEY_TYPE, getEventType());
        return objectMap;
    }

    public Map<String, Object> getEntityDataToSave() {
        Map<String, Object> objectMap = new HashMap<>();
        writeObjectMap(objectMap);
        return objectMap;
    }

    /**
     *
     * Constructs an {@link ItemHuntRunEventDTO} object from the objectMap's data.
     *
     * @param objectMap A {@link Map} holding data for the event object to read.
     * @return An {@link ItemHuntRunEventDTO} object constructed from the objectMap's data.
     */
    @Nullable
    public static ItemHuntRunEventDTO fromObjectMap(Map<String, Object> objectMap) {
        return ItemHuntRunEventDTOFactory.createEvent(objectMap);
    }
    //endregion

}
