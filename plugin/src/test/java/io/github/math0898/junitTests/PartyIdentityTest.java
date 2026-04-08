package io.github.math0898.junitTests;

import io.github.math0898.rpgframework.parties.Party;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PartyIdentityTest {

    @Test
    void partiesWithSameLeaderAreNotEqual() {
        Player leader = mockPlayer("leader");

        Party first = new Party(leader);
        Party second = new Party(leader);

        assertNotEquals(first, second);
        assertNotEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void equalityAndHashCodeRemainStableAfterPromotion() {
        Player leader = mockPlayer("leader");
        Player promoted = mockPlayer("promoted");
        Party party = new Party(leader);
        party.addPlayer(promoted);

        Set<Party> set = new HashSet<>();
        set.add(party);

        int beforePromotionHash = party.hashCode();
        UUID beforePromotionId = party.getPartyId();

        party.promote(promoted);

        assertEquals(beforePromotionId, party.getPartyId());
        assertEquals(beforePromotionHash, party.hashCode());
        assertTrue(set.contains(party));
    }

    @Test
    void partyEqualsItselfAfterLeaderPromotion() {
        Player leader = mockPlayer("leader");
        Player promoted = mockPlayer("promoted");
        Party party = new Party(leader);
        party.addPlayer(promoted);

        Party sameReference = party;
        party.promote(promoted);

        assertEquals(sameReference, party);
    }

    private Player mockPlayer(String name) {
        UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());
        return (Player) Proxy.newProxyInstance(
                Player.class.getClassLoader(),
                new Class[]{Player.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getUniqueId" -> uuid;
                    case "getName" -> name;
                    case "hasPermission" -> false;
                    case "equals" -> proxy == args[0];
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "toString" -> "MockPlayer(" + name + ")";
                    default -> null;
                }
        );
    }
}
