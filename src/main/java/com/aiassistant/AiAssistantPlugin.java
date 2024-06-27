package com.aiassistant;

import com.google.inject.Provides;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatCommandManager;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Slf4j
@PluginDescriptor(
        name = "AI Assistant"
)
public class AiAssistantPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private ConfigManager configManager;

    @Inject
    private AiAssistantConfig config;

    @Inject
    private ChatCommandManager chatCommandManager;

    @Inject
    private ChatMessageManager chatMessageManager;

    private OsrsAssistant assistant;
    private static final String ASK_AI_COMMAND = "!ai";

    @Provides
    AiAssistantConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(AiAssistantConfig.class);
    }

    @Override
    protected void startUp() {
        log.info("Starting up model {}", config.model());
        this.assistant = buildAssistant(this.config);

        this.chatCommandManager.registerCommand(ASK_AI_COMMAND, this::interactWithAi);
    }

    @Override
    protected void shutDown() throws Exception {
        this.chatCommandManager.unregisterCommand(ASK_AI_COMMAND);
        super.shutDown();
    }

    @Subscribe
    protected void onConfigChanged(ConfigChanged configChanged) {
        log.info("AI Assistant config changed");

        if (!configChanged.getKey().equalsIgnoreCase("model")
                && !configChanged.getKey().equalsIgnoreCase("key")) {
            // Neither model nor key config are changed, doesn't require rebuild
            return;
        }

        log.info("Model or key changed, rebuilding assistant");
        this.assistant = buildAssistant(this.config);
    }

    private void interactWithAi(ChatMessage chatMessage, String message) {
        // Don't respond to other players queries
        if (!Text.sanitize(chatMessage.getName()).equalsIgnoreCase(client.getLocalPlayer().getName())) {
            return;
        }

        log.info("Player interacted with AI!");
        String query = message.substring(ASK_AI_COMMAND.length() + 1);

        CompletableFuture<String> response = CompletableFuture.supplyAsync(() -> {
            try {
                return this.assistant.chat(query);
            } catch (RuntimeException e) {
                return e.getMessage();
            }
        });

        response.whenComplete((result, ex) -> {
            String chatResponse = new ChatMessageBuilder()
                    .append("[AI Assistant]: ")
                    .append(result)
                    .build();

            this.chatMessageManager.queue(QueuedMessage.builder()
                    .type(ChatMessageType.GAMEMESSAGE)
                    .runeLiteFormattedMessage(chatResponse)
                    .build());
            this.client.refreshChat();
        });
    }

    private OsrsAssistant buildAssistant(AiAssistantConfig config) {
        var model = OpenAiChatModel.builder()
                .temperature(0.2d)
                .apiKey(config.apiKey())
                .modelName(config.model().modelname)
                .maxTokens(40)
                .build();

        return AiServices.builder(OsrsAssistant.class)
                .chatLanguageModel(model)
                .tools(new OsrsAssistantTools(this.client, this.configManager))
                .build();
    }
}
