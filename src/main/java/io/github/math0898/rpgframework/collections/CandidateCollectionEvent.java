package io.github.math0898.rpgframework.collections;

import org.bukkit.Material;

import java.util.UUID;

/**
 * The CandidateCollectionEvent stores all the information the CollectionManager needs to consider a particular event in
 * a single object, so we can store it in a stack easily.
 *
 * @param type The material involved.
 * @param amount The number of items.
 * @param player The uuid of the player who may be credited.
 */
public record CandidateCollectionEvent (Material type, long amount, UUID player) { }
