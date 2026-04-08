package io.github.math0898.junitTests;

import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;
import io.github.math0898.rpgframework.commands.PartyCommand;
import io.github.math0898.rpgframework.parties.Party;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartyCommandTabCompletionTest {

    @Mock
    private CommandSender sender;

    @Mock
    private Player playerSender;

    private final PartyCommand command = new PartyCommand();

    @Test
    void partyTabReturnsCommandList() {
        List<String> tabs = command.simplifiedTab(sender, new String[]{""});

        assertIterableEquals(List.of("accept", "chat", "info", "invite", "kick", "leave", "list", "promote", "summon"), tabs);
    }

    @Test
    void partyInviteTabSuggestsOnlinePlayers() {
        Player alice = mock(Player.class);
        Player bob = mock(Player.class);
        when(alice.getName()).thenReturn("Alice");
        when(bob.getName()).thenReturn("Bob");

        try (MockedStatic<Bukkit> bukkit = org.mockito.Mockito.mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getOnlinePlayers).thenReturn(List.of(alice, bob));

            List<String> tabs = command.simplifiedTab(sender, new String[]{"invite", ""});
            assertIterableEquals(List.of("Alice", "Bob"), tabs);
        }
    }

    @Test
    void partyKickAndPromoteTabSuggestPartyMembers() {
        Player memberOne = mock(Player.class);
        Player memberTwo = mock(Player.class);
        when(memberOne.getName()).thenReturn("Knight");
        when(memberTwo.getName()).thenReturn("Mage");

        Party party = mock(Party.class);
        when(party.getPlayers()).thenReturn(new java.util.ArrayList<>(List.of(memberOne, memberTwo)));

        UUID senderId = UUID.randomUUID();
        RpgPlayer rpgPlayer = mock(RpgPlayer.class);
        when(rpgPlayer.getParty()).thenReturn(party);
        when(playerSender.getUniqueId()).thenReturn(senderId);

        try (MockedStatic<PlayerManager> playerManager = org.mockito.Mockito.mockStatic(PlayerManager.class)) {
            playerManager.when(() -> PlayerManager.getPlayer(senderId)).thenReturn(rpgPlayer);

            List<String> kickTabs = command.simplifiedTab(playerSender, new String[]{"kick", ""});
            List<String> promoteTabs = command.simplifiedTab(playerSender, new String[]{"promote", ""});

            assertEquals(List.of("Knight", "Mage"), kickTabs);
            assertEquals(List.of("Knight", "Mage"), promoteTabs);
        }
    }

    @Test
    void shortOrEmptyArgsDoNotThrow() {
        assertDoesNotThrow(() -> command.simplifiedTab(sender, new String[]{}));
        assertDoesNotThrow(() -> command.simplifiedTab(sender, new String[]{"invite"}));
    }
}
