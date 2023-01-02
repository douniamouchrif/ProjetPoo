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
import fr.ubx.poo.ubomb.go.decor.bonus.Bonus;
import fr.ubx.poo.ubomb.view.*;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
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

//import java.util.Timer;
//import java.util.TimerTask;


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
    private long invTime;
    //private final Timer timer = new Timer(1000);


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
                //checkExplosions();

                // Graphic update
                cleanupSprites();
                render();
                statusBar.update(game);
            }
        };
    }

    private void checkExplosions() {
        // Check explosions of bombs
        for (int i = 0; i<game.grid().width(); i++){
            for (int j = 0; j<game.grid().height(); j++){
                if (game.grid().get(new Position(i,j)) instanceof Bomb bomb){
                    if (bomb.getStatus() == 0){
                        game.grid().remove(bomb.getPosition());
                        animateExplosion(bomb.getPosition(), new Position(bomb.getPosition().x()+player.getRange(),bomb.getPosition().y()));
                        animateExplosion(bomb.getPosition(), new Position(bomb.getPosition().x(),bomb.getPosition().y()+player.getRange()));
                        animateExplosion(bomb.getPosition(), new Position(bomb.getPosition().x()-player.getRange(),bomb.getPosition().y()));
                        animateExplosion(bomb.getPosition(), new Position(bomb.getPosition().x(),bomb.getPosition().y()-player.getRange()));
                        int x = 0;
                        while (x <= player.getRange()){
                            if (game.grid().get(new Position(i+x,j)) instanceof Bonus bonus){
                                bonus.explode();
                            }
                            if (game.grid().get(new Position(i,j+x)) instanceof Bonus bonus){
                                bonus.explode();
                            }
                            if (game.grid().get(new Position(i-x,j)) instanceof Bonus bonus){
                                bonus.explode();
                            }
                            if (game.grid().get(new Position(i,j-x)) instanceof Bonus bonus){
                                bonus.explode();
                            }
                            if (player.getPosition().x() == i+x && player.getPosition().y() == j){
                                player.explode();
                            }
                            if (player.getPosition().x() == i && player.getPosition().y() == j+x){
                                player.explode();
                            }
                            if (player.getPosition().x() == i-x && player.getPosition().y() == j){
                                player.explode();
                            }
                            if (player.getPosition().x() == i && player.getPosition().y() == j-x){
                                player.explode();
                            }
                            if (monster.getPosition().x() == i+x && monster.getPosition().y() == j){
                                monster.explode();
                            }
                            if (player.getPosition().x() == i && player.getPosition().y() == j+x){
                                player.explode();
                            }
                            if (player.getPosition().x() == i-x && player.getPosition().y() == j){
                                player.explode();
                            }
                            if (player.getPosition().x() == i && player.getPosition().y() == j-x){
                                player.explode();
                            }
                            x++;
                        }
                        bomb.remove();
                        player.setAvailableBombs(player.getAvailableBombs()+1);
                    }
                }
            }
        }
        //appel des explodes
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
        Bomb b = new Bomb(new Position(player.getPosition()));

        ImageView bomb3 = new ImageView(ImageResourceFactory.getBomb(3).getImage());
        TranslateTransition tt33 = new TranslateTransition(Duration.millis(0), bomb3);
        tt33.setToX(player.getPosition().x() * Sprite.size);
        tt33.setToY(player.getPosition().y() * Sprite.size);
        tt33.setOnFinished(e -> {
            layer.getChildren().add(bomb3);
        });
        TranslateTransition tt3 = new TranslateTransition(Duration.millis(1000), bomb3);
        tt3.setToX(player.getPosition().x() * Sprite.size);
        tt3.setToY(player.getPosition().y() * Sprite.size);
        tt3.setOnFinished(e -> {
            layer.getChildren().remove(bomb3);
        });

        ImageView bomb2 = new ImageView(ImageResourceFactory.getBomb(2).getImage());
        TranslateTransition tt22 = new TranslateTransition(Duration.millis(0), bomb2);
        tt22.setToX(player.getPosition().x() * Sprite.size);
        tt22.setToY(player.getPosition().y() * Sprite.size);
        tt22.setOnFinished(e -> {
            layer.getChildren().add(bomb2);
        });
        TranslateTransition tt2 = new TranslateTransition(Duration.millis(1000), bomb2);
        tt2.setToX(player.getPosition().x() * Sprite.size);
        tt2.setToY(player.getPosition().y() * Sprite.size);
        tt2.setOnFinished(e -> {
            layer.getChildren().remove(bomb2);
        });

        ImageView bomb1 = new ImageView(ImageResourceFactory.getBomb(1).getImage());
        TranslateTransition tt11 = new TranslateTransition(Duration.millis(0), bomb1);
        tt11.setToX(player.getPosition().x() * Sprite.size);
        tt11.setToY(player.getPosition().y() * Sprite.size);
        tt11.setOnFinished(e -> {
            layer.getChildren().add(bomb1);
        });
        TranslateTransition tt1 = new TranslateTransition(Duration.millis(1000), bomb1);
        tt1.setToX(player.getPosition().x() * Sprite.size);
        tt1.setToY(player.getPosition().y() * Sprite.size);
        tt1.setOnFinished(e -> {
            layer.getChildren().remove(bomb1);
        });

        ImageView bomb0 = new ImageView(ImageResourceFactory.getBomb(0).getImage());
        TranslateTransition tt00 = new TranslateTransition(Duration.millis(0), bomb0);
        tt00.setToX(player.getPosition().x() * Sprite.size);
        tt00.setToY(player.getPosition().y() * Sprite.size);
        tt00.setOnFinished(e -> {
            layer.getChildren().add(bomb0);
        });
        TranslateTransition tt0 = new TranslateTransition(Duration.millis(1000), bomb0);
        tt0.setToX(player.getPosition().x() * Sprite.size);
        tt0.setToY(player.getPosition().y() * Sprite.size);
        tt0.setOnFinished(e -> {
            layer.getChildren().remove(bomb0);
            b.setStatus(0);
            game.grid().set(b.getPosition(), b);
            checkExplosions();
        });

        SequentialTransition seq = new SequentialTransition(tt33, tt3, tt22, tt2, tt11, tt1, tt00, tt0);
        seq.play();
    }

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
            if (player.getAvailableBombs() > 0) {
                player.setAvailableBombs(player.getAvailableBombs() - 1);
                createNewBombs(now);
            }
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