package com.logandhillon.fptgame.resource;

import com.logandhillon.fptgame.entity.game.PlatformEntity;
import com.logandhillon.fptgame.networking.proto.LevelProto;

/**
 * @author Logan Dhillon
 */
public class Levels {
    public static final LevelProto.LevelData DEBUG_LEVEL = LevelProto.LevelData
            .newBuilder()
            .setBackground(Textures.UNDERGROUND_BG.serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 720-40, 1280, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 200, 600, 320, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_PIPE, 600, 300, 40, 320).serialize())
            .build();
}
