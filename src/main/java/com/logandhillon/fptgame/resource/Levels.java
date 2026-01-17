package com.logandhillon.fptgame.resource;

import com.logandhillon.fptgame.entity.game.PlatformEntity;
import com.logandhillon.fptgame.networking.proto.LevelProto;

/**
 * @author Logan Dhillon
 */
public class Levels {
    public static final LevelProto.LevelData DEBUG_LEVEL = LevelProto.LevelData
            .newBuilder()
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 200, 200, 320, 40).serialize())
            .build();
}
