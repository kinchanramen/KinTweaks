package net.kinchanramen.github.kintweaks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.kinchanramen.github.kintweaks.features.KinTweaksFeatures;
import net.kinchanramen.github.kintweaks.features.KinTypeFeatures;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class KinTweaksConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().resolve("kintweaks.json").toUri());

    private static KinTweaksConfig instance;
    private Map<KinTweaksFeatures, Boolean> tweaks;
    private Map<KinTypeFeatures,String> displayTypes;

    private KinTweaksConfig() {
        tweaks = new EnumMap<>(KinTweaksFeatures.class);
    }

    public static KinTweaksConfig getInstance() {
        if (instance == null) {
            synchronized (KinTweaksConfig.class) {
                if (instance == null) {
                    instance = new KinTweaksConfig();
                    instance.load(); // インスタンスを取得した後に設定を読み込む
                }
            }
        }
        return instance;
    }

    public Map<KinTweaksFeatures, Boolean> getTweaks() {
        return tweaks;
    }
    public Map<KinTypeFeatures,String> getDisplayTypes(){
        return displayTypes;
    }

    public void setTweak(KinTweaksFeatures feature, boolean value) {
        tweaks.put(feature, value);
        feature.setValueBoolean(value);
    }
    public void setType(KinTypeFeatures features,String value){
        displayTypes.put(features,value);
        features.setValueType(KinTypeFeatures.DisplayType.valueOf(value));
    }

    public boolean getTweak(KinTweaksFeatures feature) {
        return tweaks.getOrDefault(feature, feature.getDefaultBool());
    }
    public String getType(KinTypeFeatures features){
        return displayTypes.getOrDefault(features,features.getDefaultValueTypeWithString());
    }

    public void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                KinTweaksConfig config = GSON.fromJson(reader, KinTweaksConfig.class);
                if (config != null) {
                    this.tweaks = config.getTweaks();
                    this.displayTypes=config.getDisplayTypes();
                }
            } catch (JsonIOException | JsonSyntaxException | IOException e) {
                e.printStackTrace();
            }
        } else {
            createDefaultConfig();
        }
    }

    private void createDefaultConfig() {
        if (tweaks == null) {
            tweaks = new EnumMap<>(KinTweaksFeatures.class);
        } else if (displayTypes==null) {
            displayTypes=new EnumMap<>(KinTypeFeatures.class);
        }
        for (KinTweaksFeatures feature : KinTweaksFeatures.values()) {
            tweaks.put(feature, feature.getDefaultBool());
        }
        for(KinTypeFeatures features:KinTypeFeatures.values()){
            displayTypes.put(features,features.getDefaultValueTypeWithString());
        }
        save();
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Screen getConfigScreen(Screen parent) {
        KinTweaksConfig.getInstance().load();
        KinTweaksConfig config = getInstance();
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.kintweaks.config"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.kintweaks.general"));

        for (KinTweaksFeatures feature : KinTweaksFeatures.OPTIONS) {
            general.addEntry(entryBuilder.startBooleanToggle(Text.of(feature.getTweakName()), config.getTweak(feature))
                    .setDefaultValue(feature.getDefaultBool())
                    .setSaveConsumer(newValue -> {
                        config.setTweak(feature, newValue);
                        config.save();
                    })
                    .build());
                general.addEntry(entryBuilder.startEnumSelector(Text.of(KinTypeFeatures.DISPLAY_TYPE.getTypeName()), KinTypeFeatures.DisplayType.class,KinTypeFeatures.DISPLAY_TYPE.getValueType())
                        .setDefaultValue(KinTypeFeatures.DISPLAY_TYPE.getDefaultDisplayType())
                        .setSaveConsumer(newdisplaytype->{
                            KinTweaksConfig.getInstance().setType(KinTypeFeatures.DISPLAY_TYPE,newdisplaytype.name());
                            KinTweaksConfig.getInstance().save();
                        })
                        .build());
        }

        builder.setSavingRunnable(config::save);
        return builder.build();
    }

    public boolean getTweakValue(String tweakName) {
        for (KinTweaksFeatures feature : KinTweaksFeatures.OPTIONS) {
            if (feature.getTweakName().equals(tweakName)) {
                return this.getTweak(feature);
            }
        }
        return false; // デフォルト値
    }
}