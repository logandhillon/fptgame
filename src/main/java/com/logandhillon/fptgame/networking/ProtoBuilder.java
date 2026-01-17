package com.logandhillon.fptgame.networking;

import com.logandhillon.fptgame.networking.proto.PlayerProto;

/**
 * Static helper methods for building protobuf types
 *
 * @author Logan Dhillon
 */
public class ProtoBuilder {
    /**
     * Builds a {@link com.logandhillon.fptgame.networking.proto.PlayerProto.PlayerData} protobuf
     *
     * @param name player name
     *
     * @return protobuf
     */
    public static PlayerProto.PlayerData player(String name) {
        return PlayerProto.PlayerData.newBuilder().setName(name).build();
    }
}
