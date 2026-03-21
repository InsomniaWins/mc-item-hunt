package wins.insomnia.mcitemhunt.model.service;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class MojangServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MojangService mojangService;

    @Test
    public void testProfileGrabber() {
        String trimmedUuid = "b0365e882778441e93ae4f6ad1aeced4";
        String username = "Insomnia_Wins";

        MojangService.MojangProfile mockProfile = new MojangService.MojangProfile(trimmedUuid, username);
        MojangService.MojangProfile realProfile = mojangService.getMojangProfile(trimmedUuid);

        log.info("Mojang profile: {}", realProfile);

        assert(realProfile != null);
        assert(mockProfile.equals(realProfile));
    }

}
