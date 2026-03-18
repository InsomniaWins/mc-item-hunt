package wins.insomnia.mcitemhunt.component;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import wins.insomnia.mcitemhunt.model.repository.ItemHuntRunRepository;
import wins.insomnia.mcitemhunt.model.entity.ItemHuntRunEntity;
import wins.insomnia.mcitemhunt.model.entity.ItemHuntRunEventEntity;
import wins.insomnia.mcitemhunt.model.dto.runevent.ItemHuntRunEvent;
import wins.insomnia.mcitemhunt.model.dto.runevent.ItemHuntRunEventFactory;
import wins.insomnia.mcitemhunt.model.dto.runevent.player.PlayerTransformItemHuntRunEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
//@Component
public class TestDatabaseRunner implements CommandLineRunner {

    @Getter
    private final ItemHuntRunRepository RUN_REPOSITORY;

    public TestDatabaseRunner(ItemHuntRunRepository runRepository) {
        RUN_REPOSITORY = runRepository;
    }


    /**
     * Simple fake data input to test the db.
     */
    private void simpleDataTest() {
        System.out.println("--- starting db integration test --- ");

        ItemHuntRunEntity run = new ItemHuntRunEntity();
        run.setPlayerUsername("Insomnia_Wins");
        run.setPlayerId("8f47c32b-31d6-4e50-a931-9f9316694371");
        run.setStartTime(System.currentTimeMillis());
        run.setWorldSeed("sample world seed");

        ItemHuntRunEvent testEvent = ItemHuntRunEventFactory.createEvent(
                new HashMap<>(Map.of(
                        "type", PlayerTransformItemHuntRunEvent.TYPE,
                        "x", 1.0,
                        "y", 1.0,
                        "z", 1.0,
                        "pitch", 0.0,
                        "yaw", 0.0
                ))
        );

        if (testEvent != null) {
            ItemHuntRunEventEntity testEventEntity = new ItemHuntRunEventEntity();
            testEventEntity.setEventType(testEvent.getEventType());
            testEventEntity.setEventTimestamp(System.currentTimeMillis());
            testEventEntity.setEventData(testEvent.getEntityDataToSave());
            run.addEvent(testEventEntity);
        }

        try {
            ItemHuntRunEntity savedRun = RUN_REPOSITORY.save(run);
            log.info("Successfully saved run #{} with {} events", run.getRunId(), run.getEventAmount());
        } catch (Exception exception) {
            log.error("Database save failed! Check MySQL constraints or JSON mapping.", exception);
        }
    }

    @Override
    @Transactional
    public void run(String... args) {

        simpleDataTest();
        List<@NonNull ItemHuntRunEntity> runs = RUN_REPOSITORY.findAll();
        for (ItemHuntRunEntity run : runs) {

            List<ItemHuntRunEventEntity> runEventEntities = run.getEvents();


            System.out.println("Events for run #" + run.getRunId() + ":");
            for (ItemHuntRunEventEntity eventEntity : runEventEntities) {
                ItemHuntRunEvent event = ItemHuntRunEventFactory.createEventFromDb(eventEntity);
                if (event == null) {
                    System.out.println("Null event!");
                    continue;
                }
                System.out.println(event.getEventType() + " : " + event);
            }
        }

    }
}
