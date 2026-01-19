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

    public static final LevelProto.LevelData LEVEL_9 = LevelProto.LevelData
            .newBuilder()
            .setLevelName("Mordor")
            .setLevelDifficulty("Hard")
            .setBackground(Textures.UNDERGROUND_BG.serialize())

            .setPlayer1SpawnX(1182).setPlayer1SpawnY(32)
            .setPlayer2SpawnX(116).setPlayer2SpawnY(608)

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 720 - 40, 1280, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 520, 400, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 120, 120, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 540, 580, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 80, 160, 40, 360).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 620, 360, 40, 320).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 440, 320, 400, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 440, 160, 40, 160).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 800, 160, 40, 160).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1080, 0, 40, 440).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 360, 560, 40, 120, LevelProto.Color.RED).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 320, 160, 120, 40, LevelProto.Color.RED).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 480, 160, 320, 40, LevelProto.Color.BLUE).serialize())

            .addObjects(new MovingPlatformEntity(Textures.UNDERGROUND_PIPE, 140, 460, 120, 40, 140, 120, 0.15f,
                                                 false, LevelProto.Color.NONE).serialize())
            .addObjects(new MovingPlatformEntity(Textures.UNDERGROUND_PIPE, 920, 620, 120, 40, 920, 160, 0.4f,
                                                 false, LevelProto.Color.NONE).serialize())
            .addObjects(new MovingPlatformEntity(Textures.UNDERGROUND_PIPE, 1140, 104, 120, 40, 1140, 620, 0.15f,
                                                 false, LevelProto.Color.NONE).serialize())

            .addObjects(new LevelButtonEntity(200, 660).serialize())
            .addObjects(new LevelButtonEntity(1080, 660).serialize())
            .addObjects(new LevelButtonEntity(80, 100).serialize())

            .addObjects(new PortalEntity(620, 220, true).serialize())
            .addObjects(new PortalEntity(20, 20, false).serialize())
            .build();

    // Level 8 Under Construction


    public static final LevelProto.LevelData LEVEL_7 = LevelProto.LevelData
            .newBuilder()
            .setLevelName("Maze")
            .setLevelDifficulty("Medium")
            .setNextLevel(LEVEL_9)
            .setBackground(Textures.UNDERGROUND_BG.serialize())

            .setPlayer1SpawnX(280).setPlayer1SpawnY(408)
            .setPlayer2SpawnX(120).setPlayer2SpawnY(88)

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 720 - 40, 1280, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 160, 1120, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1120, 160, 40, 320).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 120, 480, 1040, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 120, 320, 40, 160).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 160, 320, 640, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 940, 380, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 160, 1120, 40).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 580, 0, 40, 160, LevelProto.Color.BLUE).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 580, 360, 40, 120, LevelProto.Color.RED).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 580, 520, 40, 160, LevelProto.Color.RED).serialize())

            .addObjects(new MovingPlatformEntity(Textures.UNDERGROUND_PIPE, 20, 320, 80, 40, 20, 620, 0.15f,
                                                 false, LevelProto.Color.NONE).serialize())
            .addObjects(new MovingPlatformEntity(Textures.UNDERGROUND_PIPE, 1180, 620, 80, 40, 1180, 160, 0.15f,
                                                 false, LevelProto.Color.NONE).serialize())

            .addObjects(new LevelButtonEntity(410, 460).serialize())

            .addObjects(new PortalEntity(192, 380, true).serialize())
            .addObjects(new PortalEntity(32, 60, false).serialize())

            .build();

    public static final LevelProto.LevelData LEVEL_6 = LevelProto.LevelData
            .newBuilder()
            .setLevelName("Village")
            .setLevelDifficulty("Medium")
            .setNextLevel(LEVEL_7)
            .setBackground(Textures.UNDERGROUND_BG.serialize())

            .setPlayer1SpawnX(42).setPlayer1SpawnY(328)
            .setPlayer2SpawnX(122).setPlayer2SpawnY(328)

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 720 - 40, 1280, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 160, 280, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 400, 520, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 920, 400, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1080, 400, 200, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1040, 540, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1080, 240, 200, 40).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 240, 200, 40, 200, LevelProto.Color.RED).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 480, 440, 40, 240, LevelProto.Color.RED).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1080, 280, 40, 120, LevelProto.Color.RED).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 920, 440, 40, 240, LevelProto.Color.BLUE).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1000, 400, 80, 40, LevelProto.Color.BLUE).serialize())

            .addObjects(new LevelButtonEntity(164, 660).serialize())
            .addObjects(new LevelButtonEntity(1154, 380).serialize())

            .addObjects(new PortalEntity(1194, 580, true).serialize())
            .addObjects(new PortalEntity(1214, 300, false).serialize())

            .build();

    public static final LevelProto.LevelData LEVEL_5 = LevelProto.LevelData
            .newBuilder()
            .setLevelName("Follow The Leader")
            .setLevelDifficulty("Medium")
            .setNextLevel(LEVEL_6)
            .setBackground(Textures.UNDERGROUND_BG.serialize())

            .setPlayer1SpawnX(78).setPlayer1SpawnY(368)
            .setPlayer2SpawnX(78).setPlayer2SpawnY(128)

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 720 - 40, 1280, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 444, 320, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 626, 240, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 788, 160, 80, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1200, 560, 80, 40).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 440, 1160, 40).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 0, 200, 240, 40).serialize())

            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 200, 0, 40, 200, LevelProto.Color.BLUE).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 360, 480, 40, 200, LevelProto.Color.RED).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 920, 480, 40, 200, LevelProto.Color.BLUE).serialize())
            .addObjects(new PlatformEntity(Textures.UNDERGROUND_BRICKS, 1160, 440, 120, 40, LevelProto.Color.RED).serialize())

            .addObjects(new LevelButtonEntity(808, 140).serialize())
            .addObjects(new LevelButtonEntity(646, 660).serialize())

            .addObjects(new PortalEntity(95, 580, true).serialize())
            .addObjects(new PortalEntity(32, 580, false).serialize())

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
