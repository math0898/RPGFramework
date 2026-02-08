//package io.github.math0898.junitTests;
//
//import org.bukkit.*;
//import org.bukkit.block.BlockFace;
//import org.bukkit.block.PistonMoveReaction;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.EntityType;
//import org.bukkit.entity.Pose;
//import org.bukkit.entity.SpawnCategory;
//import org.bukkit.event.entity.EntityDamageEvent;
//import org.bukkit.event.player.PlayerTeleportEvent;
//import org.bukkit.metadata.MetadataValue;
//import org.bukkit.permissions.Permission;
//import org.bukkit.permissions.PermissionAttachment;
//import org.bukkit.permissions.PermissionAttachmentInfo;
//import org.bukkit.persistence.PersistentDataContainer;
//import org.bukkit.plugin.Plugin;
//import org.bukkit.util.BoundingBox;
//import org.bukkit.util.Vector;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//
//public class TestEntity implements Entity {
//
//    /**
//     * Gets the entity's current position
//     *
//     * @return a new copy of Location containing the position of this entity
//     */
//    @Override
//    public Location getLocation() {
//        return null;
//    }
//
//    /**
//     * Stores the entity's current position in the provided Location object.
//     * <p>
//     * If the provided Location is null this method does nothing and returns
//     * null.
//     *
//     * @param loc the location to copy into
//     * @return The Location object provided or null
//     */
//    @Override
//    public Location getLocation(Location loc) {
//        return null;
//    }
//
//    /**
//     * Sets this entity's velocity in meters per tick
//     *
//     * @param velocity New velocity to travel with
//     */
//    @Override
//    public void setVelocity(Vector velocity) {
//
//    }
//
//    /**
//     * Gets this entity's current velocity
//     *
//     * @return Current traveling velocity of this entity
//     */
//    @Override
//    public Vector getVelocity() {
//        return null;
//    }
//
//    /**
//     * Gets the entity's height
//     *
//     * @return height of entity
//     */
//    @Override
//    public double getHeight() {
//        return 0;
//    }
//
//    /**
//     * Gets the entity's width
//     *
//     * @return width of entity
//     */
//    @Override
//    public double getWidth() {
//        return 0;
//    }
//
//    /**
//     * Gets the entity's current bounding box.
//     * <p>
//     * The returned bounding box reflects the entity's current location and
//     * size.
//     *
//     * @return the entity's current bounding box
//     */
//    @Override
//    public BoundingBox getBoundingBox() {
//        return null;
//    }
//
//    /**
//     * Returns true if the entity is supported by a block. This value is a
//     * state updated by the server and is not recalculated unless the entity
//     * moves.
//     *
//     * @return True if entity is on ground.
//     */
//    @Override
//    public boolean isOnGround() {
//        return false;
//    }
//
//    /**
//     * Returns true if the entity is in water.
//     *
//     * @return <code>true</code> if the entity is in water.
//     */
//    @Override
//    public boolean isInWater() {
//        return false;
//    }
//
//    /**
//     * Gets the current world this entity resides in
//     *
//     * @return World
//     */
//    @Override
//    public World getWorld() {
//        return null;
//    }
//
//    /**
//     * Sets the entity's rotation.
//     * <p>
//     * Note that if the entity is affected by AI, it may override this rotation.
//     *
//     * @param yaw   the yaw
//     * @param pitch the pitch
//     * @throws UnsupportedOperationException if used for players
//     */
//    @Override
//    public void setRotation(float yaw, float pitch) {
//
//    }
//
//    /**
//     * Teleports this entity to the given location. If this entity is riding a
//     * vehicle, it will be dismounted prior to teleportation.
//     *
//     * @param location New location to teleport this entity to
//     * @return <code>true</code> if the teleport was successful
//     */
//    @Override
//    public boolean teleport(Location location) {
//        return false;
//    }
//
//    /**
//     * Teleports this entity to the given location. If this entity is riding a
//     * vehicle, it will be dismounted prior to teleportation.
//     *
//     * @param location New location to teleport this entity to
//     * @param cause    The cause of this teleportation
//     * @return <code>true</code> if the teleport was successful
//     */
//    @Override
//    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
//        return false;
//    }
//
//    /**
//     * Teleports this entity to the target Entity. If this entity is riding a
//     * vehicle, it will be dismounted prior to teleportation.
//     *
//     * @param destination Entity to teleport this entity to
//     * @return <code>true</code> if the teleport was successful
//     */
//    @Override
//    public boolean teleport(Entity destination) {
//        return false;
//    }
//
//    /**
//     * Teleports this entity to the target Entity. If this entity is riding a
//     * vehicle, it will be dismounted prior to teleportation.
//     *
//     * @param destination Entity to teleport this entity to
//     * @param cause       The cause of this teleportation
//     * @return <code>true</code> if the teleport was successful
//     */
//    @Override
//    public boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause) {
//        return false;
//    }
//
//    /**
//     * Returns a list of entities within a bounding box centered around this
//     * entity
//     *
//     * @param x 1/2 the size of the box along x axis
//     * @param y 1/2 the size of the box along y axis
//     * @param z 1/2 the size of the box along z axis
//     * @return {@code List<Entity>} List of entities nearby
//     */
//    @Override
//    public List<Entity> getNearbyEntities(double x, double y, double z) {
//        return null;
//    }
//
//    /**
//     * Returns a unique id for this entity
//     *
//     * @return Entity id
//     */
//    @Override
//    public int getEntityId() {
//        return 0;
//    }
//
//    /**
//     * Returns the entity's current fire ticks (ticks before the entity stops
//     * being on fire).
//     *
//     * @return int fireTicks
//     */
//    @Override
//    public int getFireTicks() {
//        return 0;
//    }
//
//    /**
//     * Returns the entity's maximum fire ticks.
//     *
//     * @return int maxFireTicks
//     */
//    @Override
//    public int getMaxFireTicks() {
//        return 0;
//    }
//
//    /**
//     * Sets the entity's current fire ticks (ticks before the entity stops
//     * being on fire).
//     *
//     * @param ticks Current ticks remaining
//     */
//    @Override
//    public void setFireTicks(int ticks) {
//
//    }
//
//    /**
//     * Sets if the entity has visual fire (it will always appear to be on fire).
//     *
//     * @param fire whether visual fire is enabled
//     */
//    @Override
//    public void setVisualFire(boolean fire) {
//
//    }
//
//    /**
//     * Gets if the entity has visual fire (it will always appear to be on fire).
//     *
//     * @return whether visual fire is enabled
//     */
//    @Override
//    public boolean isVisualFire() {
//        return false;
//    }
//
//    /**
//     * Returns the entity's current freeze ticks (amount of ticks the entity has
//     * been in powdered snow).
//     *
//     * @return int freeze ticks
//     */
//    @Override
//    public int getFreezeTicks() {
//        return 0;
//    }
//
//    /**
//     * Returns the entity's maximum freeze ticks (amount of ticks before it will
//     * be fully frozen)
//     *
//     * @return int max freeze ticks
//     */
//    @Override
//    public int getMaxFreezeTicks() {
//        return 0;
//    }
//
//    /**
//     * Sets the entity's current freeze ticks (amount of ticks the entity has
//     * been in powdered snow).
//     *
//     * @param ticks Current ticks
//     */
//    @Override
//    public void setFreezeTicks(int ticks) {
//
//    }
//
//    /**
//     * Gets if the entity is fully frozen (it has been in powdered snow for max
//     * freeze ticks).
//     *
//     * @return freeze status
//     */
//    @Override
//    public boolean isFrozen() {
//        return false;
//    }
//
//    /**
//     * Mark the entity's removal.
//     */
//    @Override
//    public void remove() {
//
//    }
//
//    /**
//     * Returns true if this entity has been marked for removal.
//     *
//     * @return True if it is dead.
//     */
//    @Override
//    public boolean isDead() {
//        return false;
//    }
//
//    /**
//     * Returns false if the entity has died or been despawned for some other
//     * reason.
//     *
//     * @return True if valid.
//     */
//    @Override
//    public boolean isValid() {
//        return false;
//    }
//
//    /**
//     * Sends this sender a message
//     *
//     * @param message Message to be displayed
//     */
//    @Override
//    public void sendMessage(String message) {
//
//    }
//
//    /**
//     * Sends this sender multiple messages
//     *
//     * @param messages An array of messages to be displayed
//     */
//    @Override
//    public void sendMessage(String... messages) {
//
//    }
//
//    /**
//     * Sends this sender a message
//     *
//     * @param sender  The sender of this message
//     * @param message Message to be displayed
//     */
//    @Override
//    public void sendMessage(UUID sender, String message) {
//
//    }
//
//    /**
//     * Sends this sender multiple messages
//     *
//     * @param sender   The sender of this message
//     * @param messages An array of messages to be displayed
//     */
//    @Override
//    public void sendMessage(UUID sender, String... messages) {
//
//    }
//
//    /**
//     * Gets the {@link Server} that contains this Entity
//     *
//     * @return Server instance running this Entity
//     */
//    @Override
//    public Server getServer() {
//        return null;
//    }
//
//    /**
//     * Gets the name of this command sender
//     *
//     * @return Name of the sender
//     */
//    @Override
//    public String getName() {
//        return null;
//    }
//
//    /**
//     * Returns true if the entity gets persisted.
//     * <p>
//     * By default all entities are persistent. An entity will also not get
//     * persisted, if it is riding an entity that is not persistent.
//     * <p>
//     * The persistent flag on players controls whether or not to save their
//     * playerdata file when they quit. If a player is directly or indirectly
//     * riding a non-persistent entity, the vehicle at the root and all its
//     * passengers won't get persisted.
//     * <p>
//     * <b>This should not be confused with
//     *
//     * @return true if this entity is persistent
//     */
//    @Override
//    public boolean isPersistent() {
//        return false;
//    }
//
//    /**
//     * Sets whether or not the entity gets persisted.
//     *
//     * @param persistent the persistence status
//     * @see #isPersistent()
//     */
//    @Override
//    public void setPersistent(boolean persistent) {
//
//    }
//
//    /**
//     * Gets the primary passenger of a vehicle. For vehicles that could have
//     * multiple passengers, this will only return the primary passenger.
//     *
//     * @return an entity
//     * @deprecated entities may have multiple passengers, use
//     * {@link #getPassengers()}
//     */
//    @Override
//    public Entity getPassenger() {
//        return null;
//    }
//
//    /**
//     * Set the passenger of a vehicle.
//     *
//     * @param passenger The new passenger.
//     * @return false if it could not be done for whatever reason
//     * @deprecated entities may have multiple passengers, use
//     * {@link #addPassenger(Entity)}
//     */
//    @Override
//    public boolean setPassenger(Entity passenger) {
//        return false;
//    }
//
//    /**
//     * Gets a list of passengers of this vehicle.
//     * <p>
//     * The returned list will not be directly linked to the entity's current
//     * passengers, and no guarantees are made as to its mutability.
//     *
//     * @return list of entities corresponding to current passengers.
//     */
//    @Override
//    public List<Entity> getPassengers() {
//        return null;
//    }
//
//    /**
//     * Add a passenger to the vehicle.
//     *
//     * @param passenger The passenger to add
//     * @return false if it could not be done for whatever reason
//     */
//    @Override
//    public boolean addPassenger(Entity passenger) {
//        return false;
//    }
//
//    /**
//     * Remove a passenger from the vehicle.
//     *
//     * @param passenger The passenger to remove
//     * @return false if it could not be done for whatever reason
//     */
//    @Override
//    public boolean removePassenger(Entity passenger) {
//        return false;
//    }
//
//    /**
//     * Check if a vehicle has passengers.
//     *
//     * @return True if the vehicle has no passengers.
//     */
//    @Override
//    public boolean isEmpty() {
//        return false;
//    }
//
//    /**
//     * Eject any passenger.
//     *
//     * @return True if there was a passenger.
//     */
//    @Override
//    public boolean eject() {
//        return false;
//    }
//
//    /**
//     * Returns the distance this entity has fallen
//     *
//     * @return The distance.
//     */
//    @Override
//    public float getFallDistance() {
//        return 0;
//    }
//
//    /**
//     * Sets the fall distance for this entity
//     *
//     * @param distance The new distance.
//     */
//    @Override
//    public void setFallDistance(float distance) {
//
//    }
//
//    /**
//     * Record the last {@link EntityDamageEvent} inflicted on this entity
//     *
//     * @param event a {@link EntityDamageEvent}
//     */
//    @Override
//    public void setLastDamageCause(EntityDamageEvent event) {
//
//    }
//
//    /**
//     * Retrieve the last {@link EntityDamageEvent} inflicted on this entity.
//     * This event may have been cancelled.
//     *
//     * @return the last known {@link EntityDamageEvent} or null if hitherto
//     * unharmed
//     */
//    @Override
//    public EntityDamageEvent getLastDamageCause() {
//        return null;
//    }
//
//    /**
//     * Returns a unique and persistent id for this entity
//     *
//     * @return unique id
//     */
//    @Override
//    public UUID getUniqueId() {
//        return null;
//    }
//
//    /**
//     * Gets the amount of ticks this entity has lived for.
//     * <p>
//     * This is the equivalent to "age" in entities.
//     *
//     * @return Age of entity
//     */
//    @Override
//    public int getTicksLived() {
//        return 0;
//    }
//
//    /**
//     * Sets the amount of ticks this entity has lived for.
//     * <p>
//     * This is the equivalent to "age" in entities. May not be less than one
//     * tick.
//     *
//     * @param value Age of entity
//     */
//    @Override
//    public void setTicksLived(int value) {
//
//    }
//
//    /**
//     * Performs the specified {@link EntityEffect} for this entity.
//     * <p>
//     * This will be viewable to all players near the entity.
//     * <p>
//     * If the effect is not applicable to this class of entity, it will not play.
//     *
//     * @param type Effect to play.
//     */
//    @Override
//    public void playEffect(EntityEffect type) {
//
//    }
//
//    /**
//     * Get the type of the entity.
//     *
//     * @return The entity type.
//     */
//    @Override
//    public EntityType getType() {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public Sound getSwimSound () {
//        return Sound.ENTITY_FISH_SWIM;
//    }
//
//    @NotNull
//    @Override
//    public Sound getSwimSplashSound () {
//        return Sound.ENTITY_FISH_SWIM;
//    }
//
//    @NotNull
//    @Override
//    public Sound getSwimHighSpeedSplashSound () {
//        return Sound.ENTITY_FISH_SWIM;
//    }
//
//    /**
//     * Returns whether this entity is inside a vehicle.
//     *
//     * @return True if the entity is in a vehicle.
//     */
//    @Override
//    public boolean isInsideVehicle() {
//        return false;
//    }
//
//    /**
//     * Leave the current vehicle. If the entity is currently in a vehicle (and
//     * is removed from it), true will be returned, otherwise false will be
//     * returned.
//     *
//     * @return True if the entity was in a vehicle.
//     */
//    @Override
//    public boolean leaveVehicle() {
//        return false;
//    }
//
//    /**
//     * Get the vehicle that this player is inside. If there is no vehicle,
//     * null will be returned.
//     *
//     * @return The current vehicle.
//     */
//    @Override
//    public Entity getVehicle() {
//        return null;
//    }
//
//    /**
//     * Sets whether or not to display the mob's custom name client side. The
//     * name will be displayed above the mob similarly to a player.
//     * <p>
//     * This value has no effect on players, they will always display their
//     * name.
//     *
//     * @param flag custom name or not
//     */
//    @Override
//    public void setCustomNameVisible(boolean flag) {
//
//    }
//
//    /**
//     * Gets whether or not the mob's custom name is displayed client side.
//     * <p>
//     * This value has no effect on players, they will always display their
//     * name.
//     *
//     * @return if the custom name is displayed
//     */
//    @Override
//    public boolean isCustomNameVisible() {
//        return false;
//    }
//
//    /**
//     * Sets whether the entity has a team colored (default: white) glow.
//     *
//     * <b>nb: this refers to the 'Glowing' entity property, not whether a
//     * glowing potion effect is applied</b>
//     *
//     * @param flag if the entity is glowing
//     */
//    @Override
//    public void setGlowing(boolean flag) {
//
//    }
//
//    /**
//     * Gets whether the entity is glowing or not.
//     *
//     * <b>nb: this refers to the 'Glowing' entity property, not whether a
//     * glowing potion effect is applied</b>
//     *
//     * @return whether the entity is glowing
//     */
//    @Override
//    public boolean isGlowing() {
//        return false;
//    }
//
//    /**
//     * Sets whether the entity is invulnerable or not.
//     * <p>
//     * When an entity is invulnerable it can only be damaged by players in
//     * creative mode.
//     *
//     * @param flag if the entity is invulnerable
//     */
//    @Override
//    public void setInvulnerable(boolean flag) {
//
//    }
//
//    /**
//     * Gets whether the entity is invulnerable or not.
//     *
//     * @return whether the entity is
//     */
//    @Override
//    public boolean isInvulnerable() {
//        return false;
//    }
//
//    /**
//     * Gets whether the entity is silent or not.
//     *
//     * @return whether the entity is silent.
//     */
//    @Override
//    public boolean isSilent() {
//        return false;
//    }
//
//    /**
//     * Sets whether the entity is silent or not.
//     * <p>
//     * When an entity is silent it will not produce any sound.
//     *
//     * @param flag if the entity is silent
//     */
//    @Override
//    public void setSilent(boolean flag) {
//
//    }
//
//    /**
//     * Returns whether gravity applies to this entity.
//     *
//     * @return whether gravity applies
//     */
//    @Override
//    public boolean hasGravity() {
//        return false;
//    }
//
//    /**
//     * Sets whether gravity applies to this entity.
//     *
//     * @param gravity whether gravity should apply
//     */
//    @Override
//    public void setGravity(boolean gravity) {
//
//    }
//
//    /**
//     * Gets the period of time (in ticks) before this entity can use a portal.
//     *
//     * @return portal cooldown ticks
//     */
//    @Override
//    public int getPortalCooldown() {
//        return 0;
//    }
//
//    /**
//     * Sets the period of time (in ticks) before this entity can use a portal.
//     *
//     * @param cooldown portal cooldown ticks
//     */
//    @Override
//    public void setPortalCooldown(int cooldown) {
//
//    }
//
//    /**
//     * Returns a set of tags for this entity.
//     * <br>
//     * Entities can have no more than 1024 tags.
//     *
//     * @return a set of tags for this entity
//     */
//    @Override
//    public Set<String> getScoreboardTags() {
//        return null;
//    }
//
//    /**
//     * Add a tag to this entity.
//     * <br>
//     * Entities can have no more than 1024 tags.
//     *
//     * @param tag the tag to add
//     * @return true if the tag was successfully added
//     */
//    @Override
//    public boolean addScoreboardTag(String tag) {
//        return false;
//    }
//
//    /**
//     * Removes a given tag from this entity.
//     *
//     * @param tag the tag to remove
//     * @return true if the tag was successfully removed
//     */
//    @Override
//    public boolean removeScoreboardTag(String tag) {
//        return false;
//    }
//
//    /**
//     * Returns the reaction of the entity when moved by a piston.
//     *
//     * @return reaction
//     */
//    @Override
//    public PistonMoveReaction getPistonMoveReaction() {
//        return null;
//    }
//
//    /**
//     * Get the closest cardinal {@link BlockFace} direction an entity is
//     * currently facing.
//     * <br>
//     * This will not return any non-cardinal directions such as
//     * {@link BlockFace#UP} or {@link BlockFace#DOWN}.
//     * <br>
//     *
//     * @return the entity's current cardinal facing.
//     */
//    @Override
//    public BlockFace getFacing() {
//        return null;
//    }
//
//    /**
//     * Gets the entity's current pose.
//     *
//     * <b>Note that the pose is only updated at the end of a tick, so may be
//     * inconsistent with other methods. being
//     * true does not imply the current pose will be {@link Pose#SNEAKING}</b>
//     *
//     * @return current pose
//     */
//    @Override
//    public Pose getPose() {
//        return null;
//    }
//
//    /**
//     * Get the category of spawn to which this entity belongs.
//     *
//     * @return the entity´s category spawn
//     */
//    @NotNull
//    @Override
//    public SpawnCategory getSpawnCategory() {
//        return null;
//    }
//
//    @Override
//    public Spigot spigot() {
//        return null;
//    }
//
//    /**
//     * Gets the custom name on a mob or block. If there is no name this method
//     * will return null.
//     * <p>
//     * This value has no effect on players, they will always use their real
//     * name.
//     *
//     * @return name of the mob/block or null
//     */
//    @Override
//    public String getCustomName() {
//        return null;
//    }
//
//    /**
//     * Sets a custom name on a mob or block. This name will be used in death
//     * messages and can be sent to the client as a nameplate over the mob.
//     * <p>
//     * Setting the name to null or an empty string will clear it.
//     * <p>
//     * This value has no effect on players, they will always use their real
//     * name.
//     *
//     * @param name the name to set
//     */
//    @Override
//    public void setCustomName(String name) {
//
//    }
//
//    /**
//     * Sets a metadata value in the implementing object's metadata store.
//     *
//     * @param metadataKey      A unique key to identify this metadata.
//     * @param newMetadataValue The metadata value to apply.
//     * @throws IllegalArgumentException If value is null, or the owning plugin
//     *                                  is null
//     */
//    @Override
//    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
//
//    }
//
//    /**
//     * Returns a list of previously set metadata values from the implementing
//     * object's metadata store.
//     *
//     * @param metadataKey the unique metadata key being sought.
//     * @return A list of values, one for each plugin that has set the
//     * requested value.
//     */
//    @Override
//    public List<MetadataValue> getMetadata(String metadataKey) {
//        return null;
//    }
//
//    /**
//     * Tests to see whether the implementing object contains the given
//     * metadata value in its metadata store.
//     *
//     * @param metadataKey the unique metadata key being queried.
//     * @return the existence of the metadataKey within subject.
//     */
//    @Override
//    public boolean hasMetadata(String metadataKey) {
//        return false;
//    }
//
//    /**
//     * Removes the given metadata value from the implementing object's
//     * metadata store.
//     *
//     * @param metadataKey  the unique metadata key identifying the metadata to
//     *                     remove.
//     * @param owningPlugin This plugin's metadata value will be removed. All
//     *                     other values will be left untouched.
//     * @throws IllegalArgumentException If plugin is null
//     */
//    @Override
//    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
//
//    }
//
//    /**
//     * Checks if this object contains an override for the specified
//     * permission, by fully qualified name
//     *
//     * @param name Name of the permission
//     * @return true if the permission is set, otherwise false
//     */
//    @Override
//    public boolean isPermissionSet(String name) {
//        return false;
//    }
//
//    /**
//     * Checks if this object contains an override for the specified {@link
//     * Permission}
//     *
//     * @param perm Permission to check
//     * @return true if the permission is set, otherwise false
//     */
//    @Override
//    public boolean isPermissionSet(Permission perm) {
//        return false;
//    }
//
//    /**
//     * Gets the value of the specified permission, if set.
//     * <p>
//     * If a permission override is not set on this object, the default value
//     * of the permission will be returned.
//     *
//     * @param name Name of the permission
//     * @return Value of the permission
//     */
//    @Override
//    public boolean hasPermission(String name) {
//        return false;
//    }
//
//    /**
//     * Gets the value of the specified permission, if set.
//     * <p>
//     * If a permission override is not set on this object, the default value
//     * of the permission will be returned
//     *
//     * @param perm Permission to get
//     * @return Value of the permission
//     */
//    @Override
//    public boolean hasPermission(Permission perm) {
//        return false;
//    }
//
//    /**
//     * Adds a new {@link PermissionAttachment} with a single permission by
//     * name and value
//     *
//     * @param plugin Plugin responsible for this attachment, may not be null
//     *               or disabled
//     * @param name   Name of the permission to attach
//     * @param value  Value of the permission
//     * @return The PermissionAttachment that was just created
//     */
//    @Override
//    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
//        return null;
//    }
//
//    /**
//     * Adds a new empty {@link PermissionAttachment} to this object
//     *
//     * @param plugin Plugin responsible for this attachment, may not be null
//     *               or disabled
//     * @return The PermissionAttachment that was just created
//     */
//    @Override
//    public PermissionAttachment addAttachment(Plugin plugin) {
//        return null;
//    }
//
//    /**
//     * Temporarily adds a new {@link PermissionAttachment} with a single
//     * permission by name and value
//     *
//     * @param plugin Plugin responsible for this attachment, may not be null
//     *               or disabled
//     * @param name   Name of the permission to attach
//     * @param value  Value of the permission
//     * @param ticks  Amount of ticks to automatically remove this attachment
//     *               after
//     * @return The PermissionAttachment that was just created
//     */
//    @Override
//    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
//        return null;
//    }
//
//    /**
//     * Temporarily adds a new empty {@link PermissionAttachment} to this
//     * object
//     *
//     * @param plugin Plugin responsible for this attachment, may not be null
//     *               or disabled
//     * @param ticks  Amount of ticks to automatically remove this attachment
//     *               after
//     * @return The PermissionAttachment that was just created
//     */
//    @Override
//    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
//        return null;
//    }
//
//    /**
//     * Removes the given {@link PermissionAttachment} from this object
//     *
//     * @param attachment Attachment to remove
//     * @throws IllegalArgumentException Thrown when the specified attachment
//     *                                  isn't part of this object
//     */
//    @Override
//    public void removeAttachment(PermissionAttachment attachment) {
//
//    }
//
//    /**
//     * Recalculates the permissions for this object, if the attachments have
//     * changed values.
//     * <p>
//     * This should very rarely need to be called from a plugin.
//     */
//    @Override
//    public void recalculatePermissions() {
//
//    }
//
//    /**
//     * Gets a set containing all of the permissions currently in effect by
//     * this object
//     *
//     * @return Set of currently effective permissions
//     */
//    @Override
//    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
//        return null;
//    }
//
//    /**
//     * Checks if this object is a server operator
//     *
//     * @return true if this is an operator, otherwise false
//     */
//    @Override
//    public boolean isOp() {
//        return false;
//    }
//
//    /**
//     * Sets the operator status of this object
//     *
//     * @param value New operator value
//     */
//    @Override
//    public void setOp(boolean value) {
//
//    }
//
//    /**
//     * Returns a custom tag container capable of storing tags on the object.
//     * <p>
//     * Note that the tags stored on this container are all stored under their
//     * own custom namespace therefore modifying default tags using this
//     * is impossible.
//     *
//     * @return the persistent metadata container
//     */
//    @Override
//    public PersistentDataContainer getPersistentDataContainer() {
//        return null;
//    }
//}
