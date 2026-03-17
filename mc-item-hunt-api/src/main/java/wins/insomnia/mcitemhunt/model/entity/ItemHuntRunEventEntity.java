package wins.insomnia.mcitemhunt.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;


/**
 *
 * Event with timestamp in a specific run from the db.
 *
 */
@Slf4j
@Entity
@Table(name = "item_hunt_run_events")
public class ItemHuntRunEventEntity {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "run_id", nullable = false)
    private ItemHuntRunEntity run;

    /**
     * The offset in Minecraft ticks of this event from the run's start time.
     */
    @Getter
    private Long eventTimestamp;

    @Setter
    @Getter
    private String eventType;

    @Setter
    @Getter
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json", nullable = false)
    private Map<String, Object> eventData;



    public void setEventTimestamp(Long timestamp) {
        if (timestamp < 0) {
            Long runId = getRun() == null ? null : getRun().getRunId();

            log.warn(
                    "Tried setting a negative timestamp of event #{} in run #{}.",
                    eventId, runId
            );
            return;
        }
        eventTimestamp = timestamp;
    }

}
