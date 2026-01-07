package com.logandhillon.fptgame.engine;

import com.logandhillon.fptgame.entity.core.Entity;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import static com.logandhillon.fptgame.GameHandler.*;

/**
 * A GameScene is the lowest-level of the engine; controlling the game's lifecycle, rendering, and creating a game loop.
 * It represents a "scene" of the game, that being a section of the game that is related (e.g. a game level, the main
 * menu, etc.) GameScenes are rendered with {@link GameScene#build(Stage)}, which prepares the engine code for JavaFX,
 * allowing it to be executed and ran.
 *
 * @author Logan Dhillon
 */
public abstract class GameScene {
    private static final Logger LOG = LoggerContext.getContext().getLogger(GameScene.class);

    private final ArrayList<Entity>   entities = new ArrayList<>();
    private final List<HandlerRef<?>> handlers = new ArrayList<>();

    private AnimationTimer lifecycle;

    private record HandlerRef<T extends Event>(EventType<T> type, EventHandler<? super T> handler) {}

    /**
     * Do not instantiate this class.
     *
     * @see GameScene#build(Stage)
     */
    protected GameScene() {}

    /**
     * Called every tick for non-graphics-related updates (Entity lifecycle, etc.) This implementation updates all
     * entities.
     */
    protected void onUpdate(float dt) {
        for (Entity e: entities)
            e.onUpdate(dt);
    }

    /**
     * Called every tick to render the scene. This implementation renders all entities.
     *
     * @param g the graphical context to render to.
     */
    protected void render(GraphicsContext g) {
        for (Entity e: entities)
            e.render(g);
    }

    /**
     * Creates a new JavaFX Scene for this GameScene. This should only be called once, as this method creates a new
     * Scene every time.
     *
     * @return Scene containing the GameScene's GUI elements
     */
    public Scene build(Stage stage) {
        LOG.debug("Building game scene {} to stage", this);

        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext g = canvas.getGraphicsContext2D();

        // use one-element array as address cannot change once anonymously passed to lifecycle
        final long[] lastTime = { 0 };

        lifecycle = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime[0] == 0) lastTime[0] = now; // set initial value to now
                float dt = (now - lastTime[0]) / 1_000_000_000f; // nanoseconds to seconds
                lastTime[0] = now;

                onUpdate(dt);
                render(g);
            }
        };
        lifecycle.start();

        Group parent = new Group(canvas);
        StackPane root = new StackPane(parent);
        root.setBackground(Background.fill(Color.BLACK));
        Scene scene = new Scene(root, CANVAS_WIDTH, CANVAS_HEIGHT);

        // update scaling
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScale(scene, parent));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScale(scene, parent));

        // register all event handlers
        for (HandlerRef<?> h: handlers) {
            @SuppressWarnings("unchecked") EventType<Event> t = (EventType<Event>)h.type();
            @SuppressWarnings("unchecked") EventHandler<Event> eh = (EventHandler<Event>)h.handler();

            scene.addEventHandler(t, eh);
        }

        return scene;
    }

    /**
     * Called to discard this scene (i.e., stop its lifecycle, etc.)
     *
     * @param scene the JavaFX scene that is this scene is being removed from. use this for detaching events, etc.
     */
    public void discard(Scene scene) {
        LOG.debug("Discarding scene {}", this);

        // schedule all entities for destruction
        for (Entity e: entities) e.onDestroy();
        entities.clear();

        lifecycle.stop();

        // remove all stored event handlers
        for (HandlerRef<?> h: handlers) {
            @SuppressWarnings("unchecked") EventType<Event> t = (EventType<Event>)h.type();
            @SuppressWarnings("unchecked") EventHandler<Event> eh = (EventHandler<Event>)h.handler();

            scene.removeEventHandler(t, eh);
        }
        handlers.clear();
    }

    /**
     * Updates the scaling of the canvas wrapper (parent) based on the dimensions of the window (scene)
     *
     * @param scene  the {@link Scene} that contains the canvas from {@link GameScene#build(Stage)}
     * @param parent the parent of the canvas (not the canvas itself) that has the content
     */
    private void updateScale(Scene scene, Group parent) {
        float windowWidth = (float)scene.getWidth();
        float windowHeight = (float)scene.getHeight();
        float currentAspect = windowWidth / windowHeight;

        float scale;

        if (currentAspect > ASPECT_RATIO * (1 + SCALING_TOLERANCE)) {
            // slightly wider than target, allow cropping horizontally
            scale = windowHeight / CANVAS_HEIGHT;
        } else if (currentAspect < ASPECT_RATIO * (1 - SCALING_TOLERANCE)) {
            // slightly taller than target, allow cropping vertically
            scale = windowWidth / CANVAS_WIDTH;
        } else {
            // within tolerance; scale uniformly
            scale = Math.max(windowWidth / CANVAS_WIDTH, windowHeight / CANVAS_HEIGHT);
        }

        parent.setScaleX(scale);
        parent.setScaleY(scale);

        // center the canvas (cropped edges are hidden)
        parent.setLayoutX((windowWidth - CANVAS_WIDTH * scale) / 2);
        parent.setLayoutY((windowHeight - CANVAS_HEIGHT * scale) / 2);
    }

    /**
     * Adds a new entity to the scene, that will be rendered/updated every tick.
     *
     * @param e the entity to append.
     */
    public void addEntity(Entity e) {
        entities.add(e);
        e.onAttach(this);
    }

    /**
     * Removes all entities from this modal that match the predicate
     *
     * @param discard   if the entities should also be discarded (and trigger {@link Entity#onDestroy()}
     * @param predicate the predicate to only remove entities that match it.
     */
    public void clearEntities(boolean discard, Predicate<Entity> predicate) {
        int removed = 0;
        for (Iterator<Entity> it = entities.iterator(); it.hasNext(); ) {
            Entity e = it.next();
            if (predicate.test(e)) {
                it.remove(); // safe removal
                if (discard) e.onDestroy();
                removed++;
            }
        }
        LOG.info("Successfully removed {} entities from this modal", removed);
    }

    /**
     * Registers an event handler that will be attached to the scene when it is built.
     *
     * @param type    the type of event to fire on
     * @param handler the event handler itself (the method that will run)
     */
    public <T extends Event> void addHandler(EventType<T> type, EventHandler<? super T> handler) {
        handlers.add(new HandlerRef<>(type, handler));
    }
}
