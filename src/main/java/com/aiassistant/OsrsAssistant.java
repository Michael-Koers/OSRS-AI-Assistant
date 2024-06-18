package com.aiassistant;

import dev.langchain4j.service.SystemMessage;

public interface OsrsAssistant {

    @SystemMessage("You are a Old School Runescape assistant bot. " +
            "You only answer questions regarding the video game Old School Runescape." +
            "Keep your answers short and concise, with a maximum of 80 characters.")
    String chat(String userMessage);
}
