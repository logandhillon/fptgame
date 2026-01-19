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
    /**
     * Test levels used for debugging the game, contains every aspect of the game that may need to be debugged/tested.
     *
     * @author Logan Dhillon
     */
    public static class Debug {
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
                        new PlatformEntity(
                                Textures.UNDERGROUND_BRICKS, 0, 500, 160, 40, LevelProto.Color.RED).serialize())
                .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 500, 500, 40, 40,
                                               LevelProto.Color.BLUE).serialize())
                .addObjects(new MovingPlatformEntity(Textures.UNDERGROUND_PIPE, 300, 500, 120, 40, 300, 100, 0.15f,
                                                     false, LevelProto.Color.NONE).serialize())
                .addObjects(new PortalEntity(800, 500, true).serialize())
                .addObjects(new LevelButtonEntity(50, 660).serialize())

                .build();
    }

    public static final LevelProto.LevelData LEVEL_5 = LevelProto.LevelData
            .newBuilder()
            .setLevelName("Follow The Leader")
            .setLevelDifficulty("Medium")
            .setBackground(Textures.UNDERGROUND_BG.serialize())

            .setPlayer1SpawnX(78).setPlayer1SpawnY(368)
            .setPlayer2SpawnX(78).setPlayer2SpawnY(128)

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 720 - 40, 1280, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 444, 320, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 626, 240, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 788, 160, 80, 40).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 440, 1160, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 200, 240, 40).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 200, 0, 40, 200, LevelProto.Color.BLUE).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 360, 480, 40, 200, LevelProto.Color.RED).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 920, 480, 40, 200, LevelProto.Color.BLUE).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1160, 440, 120, 40, LevelProto.Color.RED).serialize())

            .addObjects(new LevelButtonEntity(808, 140).serialize())
            .addObjects(new LevelButtonEntity(646, 660).serialize())

            .addObjects(new PortalEntity(32, 580, false).serialize())
            .addObjects(new PortalEntity(95, 580, true).serialize())

            .build();


    public static final LevelProto.LevelData LEVEL_4 = LevelProto.LevelData
            .newBuilder()
            .setLevelName("Eye Of The Storm")
            .setLevelDifficulty("Easy")
            .setNextLevel(LEVEL_5)
            .setBackground(Textures.UNDERGROUND_BG.serialize())

            .setPlayer1SpawnX(698).setPlayer1SpawnY(368)
            .setPlayer2SpawnX(540).setPlayer2SpawnY(368)

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 720 - 40, 1280, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 555, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 200, 440, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 315, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 880, 315, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1200, 555, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1000, 440, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1200, 315, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1000, 200, 80, 40).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 280, 240, 40, 440).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 200, 200, 560, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 960, 0, 40, 480).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 520, 440, 440, 40).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 320, 440, 200, 40, LevelProto.Color.RED).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 760, 200, 200, 40, LevelProto.Color.BLUE).serialize())

            .addObjects(new LevelButtonEntity(620, 420).serialize())

            .addObjects(new PortalEntity(120, 580, true).serialize())
            .addObjects(new PortalEntity(1115, 0, false).serialize())

            .build();

    public static final LevelProto.LevelData LEVEL_3 = LevelProto.LevelData
            .newBuilder()
            .setLevelName("Locked Out")
            .setLevelDifficulty("Easy")
            .setNextLevel(LEVEL_4)
            .setBackground(Textures.UNDERGROUND_BG.serialize())

            .setPlayer1SpawnX(1158).setPlayer1SpawnY(128)
            .setPlayer2SpawnX(80).setPlayer2SpawnY(128)

            // floor
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 720 - 40, 1280, 40).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 480, 200, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_PIPE, 200, 480, 40, 200,
                                           LevelProto.Color.BLUE).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1080, 480, 1280, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_PIPE, 1040, 480, 40, 200,
                                           LevelProto.Color.RED).serialize())

            .addObjects(new LevelButtonEntity(620, 660).serialize())

            .addObjects(new PortalEntity(80, 580, true).serialize())
            .addObjects(new PortalEntity(1160, 580, false).serialize())

            .build();

    public static final LevelProto.LevelData LEVEL_2 = LevelProto.LevelData
            .newBuilder()
            .setLevelName("Hallway")
            .setLevelDifficulty("Easy")
            .setNextLevel(LEVEL_3)
            .setBackground(Textures.UNDERGROUND_BG.serialize())

            .setPlayer1SpawnX(80).setPlayer1SpawnY(128)
            .setPlayer2SpawnX(23).setPlayer2SpawnY(128)

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

            .setPlayer1SpawnX(1158).setPlayer1SpawnY(608)
            .setPlayer2SpawnX(80).setPlayer2SpawnY(608)

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
