package com.aiassistant;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("AI Assistant")
public interface AiAssistantConfig extends Config {

    @ConfigItem(
            keyName = "model",
            name = "AI Model",
            description = "Pick your desired AI Model",
            warning = "Paid models will perform better"
    )
    default Model model() {
        return Model.CHATGPT4;
    }

    @ConfigItem(
            keyName = "key",
            name = "API Key",
            description = "API key for given model",
            secret = true
    )
    default String apiKey() {
        return "demo";
    }


}
