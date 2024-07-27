package net.kinchanramen.github.kintweaks.features;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.kinchanramen.github.kintweaks.KinTweaksConfig;

public enum KinTypeFeatures {
    DISPLAY_TYPE("DisplayType", DisplayType.ACTION_BAR);

    public static final ImmutableList<KinTypeFeatures> OPTIONS = ImmutableList.of(
            DISPLAY_TYPE
    );

    private final String typeName;
    private final DisplayType defaultDisplayType;
    private DisplayType displayType;

    KinTypeFeatures(String typeName, DisplayType defaultDisplayType) {
        this.typeName = typeName;
        this.defaultDisplayType = defaultDisplayType;
        this.displayType = defaultDisplayType;
    }

    public String getTypeName() {
        return typeName;
    }

    public DisplayType getValueType() {
        return displayType;
    }

    public DisplayType getDefaultDisplayType() {
        return this.defaultDisplayType;
    }

    public void setValueType(DisplayType displayType) {
        this.displayType = displayType;
    }

    public JsonElement getAsJsonElement() {
        return new Gson().toJsonTree(this.displayType);
    }

    public void setValueFromJsonElement(JsonElement jsonElement) {
        if (jsonElement != null && jsonElement.isJsonPrimitive()) {
            this.displayType = DisplayType.valueOf(jsonElement.getAsString());
        }
    }

    public String getValueFromConfig() {
        return KinTweaksConfig.getInstance().getType(this);
    }
    public String getValueTypeWithString(){
        return getValueType().toString();
    }
    public String getDefaultValueTypeWithString(){
        return getDefaultDisplayType().toString();
    }

    public enum DisplayType {
        ACTION_BAR,
        TITLE,
        CHAT
    }
}
