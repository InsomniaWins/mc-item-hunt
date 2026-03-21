package wins.insomnia.mcitemhunt.model.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wins.insomnia.mcitemhunt.model.dto.RunSummary;
import wins.insomnia.mcitemhunt.model.dto.ItemHuntRunDTO;
import wins.insomnia.mcitemhunt.model.entity.ItemHuntRunEntity;
import wins.insomnia.mcitemhunt.model.service.ItemHuntService;

import java.util.List;

@RestController
@RequestMapping("/api/itemhunt")
@Slf4j
public class ItemHuntController {

    private final ItemHuntService itemHuntService;

    public ItemHuntController(ItemHuntService itemHuntService) {
        this.itemHuntService = itemHuntService;
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


    @PostMapping("/runs")
    public ResponseEntity<ItemHuntRunEntity> createRun(@RequestBody ItemHuntRunDTO startRunDTO) {
        ItemHuntRunEntity itemHuntRunEntity = itemHuntService.saveRun(startRunDTO);
        return ResponseEntity.ok(itemHuntRunEntity);
    }

}
