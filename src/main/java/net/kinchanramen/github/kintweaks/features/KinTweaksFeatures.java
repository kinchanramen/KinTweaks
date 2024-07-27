package net.kinchanramen.github.kintweaks.features;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.kinchanramen.github.kintweaks.KinTweaksConfig;

public enum KinTweaksFeatures {
    TWEAK_TOTEM ("tweakTotemAlert", false);

    public static final ImmutableList<KinTweaksFeatures> OPTIONS = ImmutableList.of(
            TWEAK_TOTEM
    );

    private final String tweakName;
    private final boolean defaultBool;
    private boolean valueBoolean;

    KinTweaksFeatures(String tweakName, boolean defaultBool) {
        this.tweakName = tweakName;
        this.defaultBool = defaultBool;
        this.valueBoolean = defaultBool;
    }

    public String getTweakName() {
        return tweakName;
    }

    public boolean getValueBoolean() {
        return valueBoolean;
    }

    public void setValueBoolean(boolean valueBoolean) {
        this.valueBoolean = valueBoolean;
    }

    public JsonElement getAsJsonElement() {
        return new Gson().toJsonTree(this.valueBoolean);
    }

    public void setValueFromJsonElement(JsonElement jsonElement) {
        if (jsonElement != null && jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isBoolean()) {
            this.valueBoolean = jsonElement.getAsBoolean();
        }
    }
    public boolean getValueFromConfig() {
        return KinTweaksConfig.getInstance().getTweak(this);
    }

    public boolean getDefaultBool() {
        return this.defaultBool;
    }
}
