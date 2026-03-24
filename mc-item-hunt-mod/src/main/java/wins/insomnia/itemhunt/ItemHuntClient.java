package wins.insomnia.itemhunt;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import wins.insomnia.itemhunt.api.ItemHuntAPIAuthenticator;
import wins.insomnia.itemhunt.api.ItemHuntAPICommunication;

@Mod(value = ItemHunt.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ItemHunt.MODID, value = Dist.CLIENT)
public class ItemHuntClient {

    public ItemHuntClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        ItemHuntAPIAuthenticator.authenticateApiConnection();

        Thread testThread = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ItemHuntAPICommunication.startRun();
        });
        testThread.start();


    }
}
