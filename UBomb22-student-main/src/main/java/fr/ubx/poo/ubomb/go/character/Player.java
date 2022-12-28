/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.*;
import fr.ubx.poo.ubomb.go.decor.Box;
import fr.ubx.poo.ubomb.go.decor.bonus.*;

public class Player extends GameObject implements Movable, TakeVisitor , WalkVisitor {

    private Direction direction;
    private boolean moveRequested = false;
    private int lives; //on a enlevé le private final
    private int availableBombs;
    private int keys;
    private int range;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.DOWN;
        this.lives = game.configuration().playerLives();
        this.availableBombs = game.configuration().bombBagCapacity();
        this.keys = game.configuration().keys();
        this.range = game.configuration().range();
    }

    @Override
    public void take(Key key) {
        System.out.println("Take the key ...");
    }
    @Override
    public void take(Heart heart) {
        System.out.println("Take the heart ...");
    }
    @Override
    public void take(BombNumberInc bombnumberinc) {
        System.out.println("Take the bombnumbinc ...");
    }
    @Override
    public void take(BombNumberDec bombnumberdec) {
        System.out.println("Take the bombnumbdec ...");
    }
    @Override
    public void take(BombRangeInc bombrangeinc) {
        System.out.println("Take the bombrangeinc ...");
    }
    @Override
    public void take(BombRangeDec bombrangedec) {
        System.out.println("Take the bombrangedec ...");
    }
    @Override
    public boolean walk(Princess princess) {return false;}
    @Override
    public boolean walk(Box box) {
        System.out.println("move the box ...");
        (new Box(game ,getPosition())).doMove(direction);
        //box.doMove(direction);
        return true;}

    public void doMove(Direction direction) {
        // This method is called only if the move is possible, do not check again
        Position nextPos = direction.nextPosition(getPosition());
        GameObject next = game.grid().get(nextPos);
        if (next instanceof Bonus bonus) {
                bonus.takenBy(this);
        }
        if (next instanceof Box box){
            if (!(new Box(game ,getPosition())).canMove(direction)){
                return;
            }
            box.walkableBy(this);
        }
        setPosition(nextPos);
    }


    public int getLives() {
        return lives;
    }

    public int getAvailableBombs() {
        return availableBombs;
    }

    public int getKeys() {
        return keys;
    }
    public int getRange() {
        return range;
    }

    public void setLives(int lives) {
        this.lives = lives;
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
        Position nextPos = direction.nextPosition(getPosition());
        if(!game.grid().inside(nextPos)){
            return false;
        }
        if (game.grid().get(nextPos) == null){
            return true;
        }
        return game.grid().get(nextPos).walkableBy(this);
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
