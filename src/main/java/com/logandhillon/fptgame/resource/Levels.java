package com.logandhillon.fptgame.resource;

import com.logandhillon.fptgame.entity.game.MovingPlatformEntity;
import com.logandhillon.fptgame.entity.game.PlatformEntity;
import com.logandhillon.fptgame.entity.game.LevelButtonEntity;
import com.logandhillon.fptgame.entity.game.PortalEntity;
import com.logandhillon.fptgame.networking.proto.LevelProto;

/**
 * @author Logan Dhillon
 */
public class Levels {
    public static final LevelProto.LevelData DEBUG_LEVEL_2 = LevelProto.LevelData
            .newBuilder()
            .setBackground(Textures.UNDERGROUND_BG.serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 720 - 40, 1280, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 400, 560, 320, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_PIPE, 300, 200, 40, 320).serialize())
            .addObjects(new PortalEntity(900, 500, true).serialize())
            .build();

    public static final LevelProto.LevelData DEBUG_LEVEL = LevelProto.LevelData
            .newBuilder()
            .setBackground(Textures.UNDERGROUND_BG.serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 720 - 40, 1280, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 200, 600, 320, 40).serialize())
            .addObjects(
                    new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 500, 160, 40, LevelProto.Color.RED).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 500, 500, 40, 40,
                                           LevelProto.Color.BLUE).serialize())
            .addObjects(new MovingPlatformEntity(Textures.UNDERGROUND_PIPE, 300, 500, 120, 40, 800, 600, 0.15f,
                                                 false, LevelProto.Color.NONE).serialize())
            .addObjects(new PortalEntity(800, 500, true).serialize())
            .addObjects(new LevelButtonEntity(50, 660).serialize())
            .setNextLevel(DEBUG_LEVEL_2)
            .setPlayer1SpawnX(100).setPlayer1SpawnY(600)
            .build();
}
