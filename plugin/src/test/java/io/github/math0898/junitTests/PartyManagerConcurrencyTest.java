package io.github.math0898.junitTests;

import io.github.math0898.rpgframework.parties.Party;
import io.github.math0898.rpgframework.parties.PartyManager;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PartyManagerConcurrencyTest {

    @AfterEach
    void clearState () throws Exception {
        getPartyStore().clear();
        getPartyChatStore().clear();
    }

    @Test
    void findPartyStaysStableDuringConcurrentMutation () throws Exception {
        List<Player> players = java.util.stream.IntStream.range(0, 64)
            .mapToObj(i -> fakePlayer("player-" + i, UUID.nameUUIDFromBytes(("player-" + i).getBytes())))
            .toList();

        for (Player player : players) {
            PartyManager.addParty(new Party(player));
        }

        Collection<Throwable> failures = new ConcurrentLinkedQueue<>();
        ExecutorService pool = Executors.newFixedThreadPool(4);
        CountDownLatch start = new CountDownLatch(1);

        pool.submit(() -> {
            await(start);
            for (Player player : players) {
                PartyManager.removeParty(PartyManager.findParty(player));
            }
        });
        pool.submit(() -> {
            await(start);
            for (Player player : players) {
                PartyManager.addParty(new Party(player));
            }
        });
        pool.submit(() -> {
            await(start);
            for (int i = 0; i < 1_000; i++) {
                Player player = players.get(i % players.size());
                try {
                    Party found = PartyManager.findParty(player);
                    if (getPartyStore().stream().anyMatch(p -> p.hasMember(player))) {
                        assertNotNull(found, "Expected lookup to return a party when membership exists");
                    }
                } catch (Throwable t) {
                    failures.add(t);
                }
            }
        });

        start.countDown();
        pool.shutdown();
        assertTrue(pool.awaitTermination(10, TimeUnit.SECONDS));
        assertTrue(failures.isEmpty(), () -> "Unexpected concurrent failures: " + failures);
    }

    @Test
    void togglePartyChatIsDuplicateSafeUnderConcurrency () throws Exception {
        Player player = fakePlayer("toggle-user", UUID.randomUUID());
        PartyManager.addParty(new Party(player));

        ExecutorService pool = Executors.newFixedThreadPool(8);
        CountDownLatch start = new CountDownLatch(1);

        for (int i = 0; i < 64; i++) {
            pool.submit(() -> {
                await(start);
                PartyManager.togglePartyChat(player);
            });
        }

        start.countDown();
        pool.shutdown();
        assertTrue(pool.awaitTermination(10, TimeUnit.SECONDS));

        long matchingEntries = getPartyChatStore().stream().filter(player::equals).count();
        assertTrue(matchingEntries <= 1, "Party chat toggle should never create duplicate entries");
    }

    @Test
    void findPartyReturnsMatchingPartyAfterBulkRegistration () {
        Player alpha = fakePlayer("alpha", UUID.randomUUID());
        Player beta = fakePlayer("beta", UUID.randomUUID());

        Party first = new Party(alpha);
        Party second = new Party(beta);
        PartyManager.addParty(first);
        PartyManager.addParty(second);

        assertEquals(first, PartyManager.findParty(alpha));
        assertEquals(second, PartyManager.findParty(beta));
    }

    private static void await (CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(interrupted);
        }
    }

    @SuppressWarnings("unchecked")
    private static Collection<Party> getPartyStore () throws Exception {
        Field partiesField = PartyManager.class.getDeclaredField("parties");
        partiesField.setAccessible(true);
        return (Collection<Party>) partiesField.get(null);
    }

    @SuppressWarnings("unchecked")
    private static Collection<Player> getPartyChatStore () throws Exception {
        Field partyChatField = PartyManager.class.getDeclaredField("partyChatPlayers");
        partyChatField.setAccessible(true);
        return (Collection<Player>) partyChatField.get(null);
    }

    private static Player fakePlayer (String name, UUID uniqueId) {
        InvocationHandler handler = (proxy, method, args) -> switch (method.getName()) {
            case "getName" -> name;
            case "getUniqueId" -> uniqueId;
            case "sendMessage" -> null;
            case "hasPermission" -> false;
            case "isOnline" -> true;
            case "equals" -> proxy == args[0];
            case "hashCode" -> System.identityHashCode(proxy);
            case "toString" -> "FakePlayer{" + name + "}";
            default -> defaultValue(method.getReturnType());
        };

        return (Player) Proxy.newProxyInstance(
            PartyManagerConcurrencyTest.class.getClassLoader(),
            new Class<?>[] { Player.class },
            handler
        );
    }

    private static Object defaultValue (Class<?> type) {
        if (!type.isPrimitive()) return null;
        if (boolean.class.equals(type)) return false;
        if (byte.class.equals(type)) return (byte) 0;
        if (short.class.equals(type)) return (short) 0;
        if (int.class.equals(type)) return 0;
        if (long.class.equals(type)) return 0L;
        if (float.class.equals(type)) return 0f;
        if (double.class.equals(type)) return 0d;
        if (char.class.equals(type)) return '\0';
        return null;
    }
}
