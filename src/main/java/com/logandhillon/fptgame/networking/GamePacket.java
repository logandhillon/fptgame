package com.logandhillon.fptgame.networking;

import com.google.protobuf.Message;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.nio.charset.StandardCharsets;

/**
 * A packet to send between the server or client that contains game-related networking data.
 *
 * @param type    the {@link Type} of packet to send
 * @param payload the raw content of the packet
 *
 * @author Logan Dhillon
 */
public record GamePacket(Type type, byte[] payload) {
    private static final Logger LOG   = LoggerContext.getContext().getLogger(GamePacket.class);
    private static final Type[] TYPES = Type.values();

    /**
     * Creates a packet with a null payload.
     *
     * @param type the type of packet to send.
     */
    public GamePacket(Type type) {
        this(type, new byte[0]);
    }

    public GamePacket(Type type, String payload) {
        this(type, payload.getBytes(StandardCharsets.UTF_8));
    }

    public GamePacket(Type type, Message proto) {
        this(type, proto.toByteArray());
    }

    /**
     * The type of packet to send. SRV are server packets, CLT are client packets.
     */
    public enum Type {
        // server-side types
        SRV_UPDATE_PLAYERLIST, SRV_DENY_CONN__FULL, SRV_DENY_CONN__USERNAME_TAKEN, // used for managing connections
        SRV_GAME_STARTING, // announces that the game is starting
        SRV_UNEXPECTED, // generic error for if the server wasn't expecting something (e.g. not ready for a request)
        SRV_SHUTDOWN,
        SRV_SYNC_MOVEMENT,

        // client-side types
        CLT_REQ_CONN, // used to request registration upon joining a server

        COM_JUMP,
        COM_MOVE_L,
        COM_MOVE_R,
        COM_STOP_MOVING
    }

    /**
     * Turns this packet into a length-prefixed byte array that can be sent.
     * <p>
     * Format: [4-byte payload length][1-byte type][payload bytes]
     *
     * @return the serialized byte array.
     */
    public byte[] serialize() {
        int totalLength = 4 + 1 + payload.length; // 4 bytes for length, 1 for type
        byte[] out = new byte[totalLength];

        // write length of the payload (1 byte type + payload)
        int payloadLength = 1 + payload.length;
        out[0] = (byte)((payloadLength >> 24) & 0xFF);
        out[1] = (byte)((payloadLength >> 16) & 0xFF);
        out[2] = (byte)((payloadLength >> 8) & 0xFF);
        out[3] = (byte)(payloadLength & 0xFF);

        // write type
        out[4] = (byte)type.ordinal();

        // write payload
        System.arraycopy(payload, 0, out, 5, payload.length);

        return out;
    }

    /**
     * Deserializes a length-prefixed byte array to a {@link GamePacket}.
     *
     * @param data the raw packet content as a byte array
     *
     * @return the deserialized game packet
     */
    public static GamePacket deserialize(byte[] data) {
        if (data.length < 5) {
            LOG.warn("Failed to deserialize packet: not enough data");
            return null;
        }

        // read payload length
        int payloadLength = ((data[0] & 0xFF) << 24) |
                            ((data[1] & 0xFF) << 16) |
                            ((data[2] & 0xFF) << 8) |
                            (data[3] & 0xFF);

        if (payloadLength != data.length - 4) {
            LOG.warn("Failed to deserialize packet: length mismatch");
            return null;
        }

        int typeIndex = data[4];
        if (typeIndex < 0 || typeIndex >= TYPES.length) {
            LOG.warn("Failed to deserialize packet, TYPE {} does not exist", typeIndex);
            return null;
        }

        byte[] payload = new byte[payloadLength - 1];
        System.arraycopy(data, 5, payload, 0, payload.length);
        return new GamePacket(TYPES[typeIndex], payload);
    }
}
