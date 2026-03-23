package wins.insomnia.mcitemhunt.model.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wins.insomnia.mcitemhunt.manager.ActiveRunsManager;
import wins.insomnia.mcitemhunt.model.UserSession;
import wins.insomnia.mcitemhunt.model.dto.RunSummary;
import wins.insomnia.mcitemhunt.model.dto.ItemHuntRunDTO;
import wins.insomnia.mcitemhunt.model.dto.runevent.ItemHuntRunEventDTO;
import wins.insomnia.mcitemhunt.model.entity.ItemHuntRunEntity;
import wins.insomnia.mcitemhunt.model.service.ItemHuntService;
import wins.insomnia.mcitemhunt.model.service.MojangService;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/itemhunt")
@Slf4j
public class ItemHuntController {

    private final ItemHuntService itemHuntService;
    private final ActiveRunsManager activeRunsManager;
    private final MojangService mojangService;

    public ItemHuntController(ItemHuntService itemHuntService, ActiveRunsManager activeRunsManager, MojangService mojangService) {
        this.itemHuntService = itemHuntService;
        this.activeRunsManager = activeRunsManager;
        this.mojangService = mojangService;
    }

    /**
     *
     * @return A {@link List}<{@link Long}> of item hunt run ids.
     */
    @GetMapping("/ids")
    public List<Long> getAllRunIds() {
        return itemHuntService.getAllRunIds();
    }

    /**
     *
     * @return A {@link List}<{@link RunSummary}> of item hunt runs pending verification.
     */
    @GetMapping("/runs/pendingverification")
    public List<RunSummary> getRunsPendingVerification() {
        return itemHuntService.getRunsPendingVerification();
    }

    /**
     *
     * @return A {@link List}<{@link RunSummary}> of verified item hunt runs.
     */
    @GetMapping("/runs/verified")
    public List<RunSummary> getVerifiedRuns() {
        return itemHuntService.getVerifiedRuns();
    }




    @GetMapping("/auth/request-id")
    public ResponseEntity<String> requestServerId(@RequestParam String playerId) {
        String serverId = Long.toHexString(new SecureRandom().nextLong());
        activeRunsManager.setPendingUser(serverId, playerId);
        return ResponseEntity.ok(serverId);
    }


    @PostMapping("/auth/verify")
    public ResponseEntity<String> verifyServerId(@RequestParam String serverId, @RequestParam String playerId) {

        if (!activeRunsManager.isPendingUser(serverId, playerId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    "Invalid or expired authentication attempt."
            );
        }

        MojangService.MojangProfile playerProfile = mojangService.getMojangProfile(playerId);

        if (playerProfile == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    "Invalid account used in authentication attempt."
            );
        }

        String playerUsername = playerProfile.name();
        boolean isMojangVerified = mojangService.isValidPlayerSession(playerUsername, serverId);

        if (!isMojangVerified) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    "Could not verify your session with Mojang."
            );
        }

        String sessionToken = UUID.randomUUID().toString();
        activeRunsManager.addSession(new UserSession(playerId, sessionToken));

        return ResponseEntity.ok(sessionToken);
    }

    @PostMapping("/start-run")
    public ResponseEntity<String> startRun(@RequestBody ItemHuntRunDTO runData) {
        itemHuntService.startNewRun(runData);
        return ResponseEntity.ok("Run started for " + runData.getPlayerId());
    }

    /**
     * Adds event to a run.
     * @param playerId The user id of the player who's active run will be modified.
     * @param eventData The event to add.
     * @return A response to the client/sender.
     */
    @PatchMapping("/add-run-event")
    public ResponseEntity<String> addRunEvent(@RequestParam String playerId, @RequestBody ItemHuntRunEventDTO eventData) {
        int result = itemHuntService.addEventToRun(playerId, eventData);
        if (result > 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No active run for player " + playerId);
        }
        return ResponseEntity.ok("Received event for active run for player " + playerId);
    }

    @PostMapping("/finish-run")
    public ResponseEntity<ItemHuntRunEntity> finishRun(@RequestParam String playerId) {
        ItemHuntRunDTO runData = activeRunsManager.getRun(playerId);
        if (runData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        runData.setEndTime(System.currentTimeMillis());

        activeRunsManager.removeRun(playerId);

        return ResponseEntity.ok(itemHuntService.saveRun(runData));
    }

}
