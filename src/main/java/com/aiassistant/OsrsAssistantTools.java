package com.aiassistant;

import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Client;
import net.runelite.api.Skill;

@RequiredArgsConstructor
public class OsrsAssistantTools {

    private final Client client;

    @Tool("Returns the player's combat level.")
    public int getCombatLevel() {
        return this.client.getLocalPlayer().getCombatLevel();
    }

    @Tool("Retrieve the player's level for a given skill.")
    public int getSkillLevel(Skill skill) {
        return this.client.getRealSkillLevel(skill);
    }
}
