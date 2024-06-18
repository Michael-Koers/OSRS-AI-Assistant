package com.aiassistant;

import dev.langchain4j.model.openai.OpenAiChatModelName;

public enum Model {

    CHATGPT_3_5(OpenAiChatModelName.GPT_3_5_TURBO.toString()),
    CHATGPT4(OpenAiChatModelName.GPT_4.toString());

    public final String modelname;

    Model(String modelname) {
        this.modelname = modelname;
    }
}
