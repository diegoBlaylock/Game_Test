package engine.physics;

/**
 * TRIGGER - No Collision resolution, sends collision events
 * PUSHOVER - Collision resolution with ICEBERGS, send collision events
 * ICEBERG - Immovable, cause resolution of PUSHOVERs, sends collision event
 * @author diego
 *
 */
public enum Collider {
	TRIGGER,
	ICEBERG,
	PUSHOVER
}
