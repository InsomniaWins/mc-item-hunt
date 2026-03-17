package wins.insomnia.mcitemhunt.model.entity.runevent.player;

import lombok.Getter;
import wins.insomnia.mcitemhunt.model.entity.runevent.ItemHuntRunEvent;

import java.util.Map;

public class PlayerTransformItemHuntRunEvent extends ItemHuntRunEvent {

    public static String TYPE = "player_transform";

    @Getter
    private double x;
    @Getter
    private double y;
    @Getter
    private double z;
    @Getter
    private double pitch;
    @Getter
    private double yaw;

    public PlayerTransformItemHuntRunEvent(Map<String, Object> objectMap) {
        super(objectMap);
    }

    @Override
    public String getEventType() {
        return TYPE;
    }

    @Override
    public void readObjectMap(Map<String, Object> objectMap) {
        x = ((Number) objectMap.getOrDefault("x", 0.0)).doubleValue();
        y = ((Number) objectMap.getOrDefault("y", 0.0)).doubleValue();
        z = ((Number) objectMap.getOrDefault("z", 0.0)).doubleValue();
        pitch = ((Number) objectMap.getOrDefault("pitch", 0.0)).doubleValue();
        yaw = ((Number) objectMap.getOrDefault("yaw", 0.0)).doubleValue();
    }

    @Override
    public void writeObjectMap(Map<String, Object> objectMap) {
        objectMap.put("x", x);
        objectMap.put("y", y);
        objectMap.put("z", z);
        objectMap.put("pitch", pitch);
        objectMap.put("yaw", yaw);
    }
}
