package com.logandhillon.fptgame.resource;

import com.logandhillon.fptgame.entity.game.LevelButtonEntity;
import com.logandhillon.fptgame.entity.game.MovingPlatformEntity;
import com.logandhillon.fptgame.entity.game.PlatformEntity;
import com.logandhillon.fptgame.entity.game.PortalEntity;
import com.logandhillon.fptgame.networking.proto.LevelProto;

/**
 * Contains programmatic {@link LevelProto.LevelData} used in the built-in levels.
 * <p>
 * Level design for levels 1-9 by Jack Ross.
 *
 * @author Logan Dhillon, Jack Ross
 */
public class Levels {
    public static final LevelProto.LevelData DEBUG_LEVEL_2 = LevelProto.LevelData
            .newBuilder()
            .setLevelName("DEBUG LEVEL 2")
            .setBackground(Textures.UNDERGROUND_BG.serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 720 - 40, 1280, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 400, 560, 320, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_PIPE, 300, 200, 40, 320).serialize())
            .addObjects(new PortalEntity(900, 500, true).serialize())

            .build();

    public static final LevelProto.LevelData DEBUG_LEVEL = LevelProto.LevelData
            .newBuilder()
            .setLevelName("DEBUG LEVEL 1")
            .setNextLevel(DEBUG_LEVEL_2)
            .setBackground(Textures.UNDERGROUND_BG.serialize())

            .setPlayer1SpawnX(100).setPlayer1SpawnY(600)

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

            .build();

    public static final LevelProto.LevelData LEVEL_2 = LevelProto.LevelData
            .newBuilder()
            .setLevelName("Hallway")
            .setLevelDifficulty("Easy")
//            .setNextLevel(DEBUG_LEVEL_2)
            .setBackground(Textures.UNDERGROUND_BG.serialize())

            .setPlayer1SpawnX(23).setPlayer1SpawnY(128)
            .setPlayer2SpawnX(80).setPlayer2SpawnY(128)

            // floor
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 720 - 40, 1280, 40).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 200, 200, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 200, 200, 880, 40,
                                           LevelProto.Color.RED).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1080, 200, 200, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1080, 0, 40, 200,
                                           LevelProto.Color.BLUE).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 390, 1280, 40).serialize())

            .addObjects(new PortalEntity(1208, 290, true).serialize())
            .addObjects(new PortalEntity(1208, 100, false).serialize())

            .build();

    public static final LevelProto.LevelData LEVEL_1 = LevelProto.LevelData
            .newBuilder()
            .setLevelName("Staircase")
            .setLevelDifficulty("Easy")
            .setNextLevel(LEVEL_2)
            .setBackground(Textures.UNDERGROUND_BG.serialize())

            .setPlayer1SpawnX(80).setPlayer1SpawnY(608)
            .setPlayer2SpawnX(1158).setPlayer2SpawnY(608)

            // floor
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 720 - 40, 1280, 40).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 270, 488, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 392, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 270, 296, 80, 40).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 540, 200, 200, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 540, 584, 200, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_PIPE, 620, 240, 40, 440).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 930, 488, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1200, 392, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 930, 296, 80, 40).serialize())

            .addObjects(new PortalEntity(550, 100, true).serialize())
            .addObjects(new PortalEntity(680, 100, false).serialize())

            .build();
}
