package com.logandhillon.fptgame.networking;

import com.logandhillon.fptgame.networking.proto.PlayerProto;

/**
 * @author Logan Dhillon
 */
public class ProtoBuilder {
    public static PlayerProto.PlayerData player(String name) {
        return PlayerProto.PlayerData.newBuilder().setName(name).build();
    }
}
