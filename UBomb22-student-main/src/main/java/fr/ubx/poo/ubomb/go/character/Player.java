/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.*;
import fr.ubx.poo.ubomb.go.decor.Decor;
import fr.ubx.poo.ubomb.go.decor.Tree;
import fr.ubx.poo.ubomb.go.decor.Stone;
import fr.ubx.poo.ubomb.go.decor.Box;
import fr.ubx.poo.ubomb.go.decor.bonus.*;
import fr.ubx.poo.ubomb.launcher.Entity;

public class Player extends GameObject implements Movable, TakeVisitor , WalkVisitor {

    private Direction direction;
    private boolean moveRequested = false;
    private final int lives;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.DOWN;
        this.lives = game.configuration().playerLives();
    }


    @Override
    public void take(Key key) {
        System.out.println("Take the key ...");
    }

    public void doMove(Direction direction) {
        // This method is called only if the move is possible, do not check again
        Position nextPos = direction.nextPosition(getPosition());
        GameObject next = game.grid().get(nextPos);
        if (next instanceof Bonus bonus) {
                bonus.takenBy(this);
        }
        if (next instanceof Box box){
            if (box.canMove(direction)){
                box.doMove(direction);
            }
        }
        setPosition(nextPos);
    }


    public int getLives() {
        return lives;
    }

    public Direction getDirection() {
        return direction;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    public final boolean canMove(Direction direction) {
        // Need to be updated ;-)
        // peut pas marcher sur une case (caisse)
        Position nextPos = direction.nextPosition(getPosition());
        Player player = new Player(game , new Position(getPosition().x(),getPosition().y()) );
        if(direction == Direction.UP){
            if (getPosition().y() <= 0){
                return false;
            }
            if (game.grid().get(new Position(getPosition().x(),getPosition().y()-1)) == null){
                return true;
            }
        }
        if(direction == Direction.DOWN){
            if (getPosition().y() >= game.grid().height()-1){
                return false;
            }
            if (game.grid().get(new Position(getPosition().x(),getPosition().y()+1)) == null){
                return true;
            }
        }
        if(direction == Direction.LEFT){
            if (getPosition().x() <= 0){
                return false;
            }
            if (game.grid().get(new Position(getPosition().x()-1,getPosition().y()))== null){
                return true;
            }
        }
        if(direction == Direction.RIGHT){
            if (getPosition().x() >= game.grid().width()-1){
                return false;
            }
            if (game.grid().get(new Position(getPosition().x()+1,getPosition().y()))== null){
                return true;
            }
        }
        return game.grid().get(nextPos).walkableBy(player);
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    @Override
    public void explode() {
        // TODO
    }
}
