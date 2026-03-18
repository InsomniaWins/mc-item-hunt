package wins.insomnia.mcitemhunt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wins.insomnia.mcitemhunt.model.service.ItemHuntService;

import java.util.List;

@RestController
@RequestMapping("/api/itemhunt")
@Slf4j
public class ItemHuntController {

    private final ItemHuntService ITEM_HUNT_SERVICE;

    public ItemHuntController(ItemHuntService itemHuntService) {
        ITEM_HUNT_SERVICE = itemHuntService;
    }

    /**
     *
     * @return A {@link List}<{@link Long}> of item hunt run ids.
     */
    @GetMapping("/ids")
    public List<Long> getAllRunIds() {
        return ITEM_HUNT_SERVICE.getAllRunIds();
    }

    @GetMapping("/admin/ids")
    public List<Long> getAllRunIdsAdmin() {
        return ITEM_HUNT_SERVICE.getAllRunIds();
    }

}
