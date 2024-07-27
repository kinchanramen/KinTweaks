package net.kinchanramen.github.kintweaks;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KinTweaks implements ModInitializer {
    public static final String MOD_ID="KinTweaks";
    public static Logger logger;
    @Override
    public void onInitialize() {
        logger= LoggerFactory.getLogger(MOD_ID);
        logger.info("Initializing KinTweaks");
    }
}
