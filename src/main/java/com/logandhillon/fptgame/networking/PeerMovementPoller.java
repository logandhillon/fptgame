package com.logandhillon.fptgame.networking;

/**
 * Polls the movement queue of the connected peer.
 *
 * @author Logan Dhillon
 */
public interface PeerMovementPoller {
    /**
     * Retrieves and removes the head of this queue, or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    GamePacket.Type poll();
}