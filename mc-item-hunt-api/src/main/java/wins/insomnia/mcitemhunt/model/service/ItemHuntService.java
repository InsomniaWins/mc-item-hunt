package wins.insomnia.mcitemhunt.model.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wins.insomnia.mcitemhunt.model.repository.ItemHuntRunRepository;

import java.util.List;

@Service
@Slf4j
public class ItemHuntService {

    private final ItemHuntRunRepository RUN_REPOSITORY;

    public ItemHuntService(ItemHuntRunRepository runRepository) {
        RUN_REPOSITORY = runRepository;
    }


    @Transactional(readOnly = true)
    public List<Long> getAllRunIds() {
        return RUN_REPOSITORY.findAllRunIds();
    }



}
