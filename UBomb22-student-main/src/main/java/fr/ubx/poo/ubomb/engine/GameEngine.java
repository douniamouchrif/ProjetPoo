/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.engine;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.bonus.Bomb;
import fr.ubx.poo.ubomb.view.*;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import java.util.Timer;
import java.util.TimerTask;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final Game game;
    private final Player player;
    private final Monster monster;
    private final List<Sprite> sprites = new LinkedList<>();
    private final Set<Sprite> cleanUpSprites = new HashSet<>();
    private final Stage stage;
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Bomb bomb;

    private long invTime;


    public GameEngine(Game game, final Stage stage) {
        this.stage = stage;
        this.game = game;
        this.player = game.player();
        this.monster = game.monster();
        initialize();
        buildAndSetGameLoop();
    }

    private void initialize() {
        Group root = new Group();
        layer = new Pane();

        int height = game.grid().height();
        int width = game.grid().width();
        int sceneWidth = width * ImageResource.size;
        int sceneHeight = height * ImageResource.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);

        // Create sprites
        for (var decor : game.grid().values()) {
            sprites.add(SpriteFactory.create(layer, decor));
            decor.setModified(true);
        }

        sprites.add(new SpritePlayer(layer, player));
        sprites.add(new SpriteMonster(layer, monster));
    }

    void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                update(now);
                //createNewBombs(now);
                checkCollision(now);
                checkExplosions();

                // Graphic update
                cleanupSprites();
                render();
                statusBar.update(game);
            }
        };
    }

    private void checkExplosions() {
        // Check explosions of bombs
    }

    private void animateExplosion(Position src, Position dst) {
        ImageView explosion = new ImageView(ImageResource.EXPLOSION.getImage());
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), explosion);
        tt.setFromX(src.x() * Sprite.size);
        tt.setFromY(src.y() * Sprite.size);
        tt.setToX(dst.x() * Sprite.size);
        tt.setToY(dst.y() * Sprite.size);
        tt.setOnFinished(e -> {
            layer.getChildren().remove(explosion);
        });
        layer.getChildren().add(explosion);
        tt.play();
    }

    private void createNewBombs(long now) {
        // Create a new Bomb is needed

        //if(input.isBomb()) {
            //layer.getChildren().add(explosion);
            int i = 3;
            long nextStatu = now + 10000000000L;
            Bomb b2 = new Bomb(player.getPosition());
            while (i >= 1) {
                System.out.println("Bomb ...");
                b2.setStatus(i);
                sprites.add(new SpriteBomb(layer, b2));
                if (nextStatu >= now) {
                    System.out.println("Bomb remove...");
                    i--;
                    nextStatu += 10000000000L;
                    game.grid().remove(b2.getPosition());
                    //b2.remove();
                    //System.out.println("Bomb ...");
                }
            }
            b2.setStatus(0);
            sprites.add(new SpriteBomb(layer, b2));
        //}
    }

    /*if (now >= nextStatu){
        nextStatu = now + 1000000000L;
        while (i >= 1) {
            System.out.println("Bomb ...");
            b2.setStatus(i);
            sprites.add(new SpriteBomb(layer, b2));
        }
        System.out.println("Bomb remove...");
        i--;
        nextStatu += 10000000000L;
        game.grid().remove(b2.getPosition());
    }*/


    private void checkCollision(long now) {
        if (game.player().getPosition().x() == game.monster().getPosition().x() && game.player().getPosition().y() == game.monster().getPosition().y()){
            if(now>=invTime){
                game.player().setLives(game.player().getLives()-1);
                System.out.println("Monster ...");
                invTime = now + game.configuration().playerInvisibilityTime()*1000000;
            }
        }
        // Check a collision between a monster and the player
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        } else if (input.isMoveDown()) {
            player.requestMove(Direction.DOWN);
        } else if (input.isMoveLeft()) {
            player.requestMove(Direction.LEFT);
        } else if (input.isMoveRight()) {
            player.requestMove(Direction.RIGHT);
        } else if (input.isMoveUp()) {
            player.requestMove(Direction.UP);
        } else if (input.isBomb()) {
            createNewBombs(now);
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }


    private void update(long now) {
        monster.update(now);
        player.update(now);

        if (player.getLives() == 0) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
    }

    public void cleanupSprites() {
        sprites.forEach(sprite -> {
            if (sprite.getGameObject().isDeleted()) {
                game.grid().remove(sprite.getPosition());
                cleanUpSprites.add(sprite);
            }
        });
        cleanUpSprites.forEach(Sprite::remove);
        sprites.removeAll(cleanUpSprites);
        cleanUpSprites.clear();
    }

    private void render() {
        sprites.forEach(Sprite::render);
    }

    public void start() {
        gameLoop.start();
    }
}