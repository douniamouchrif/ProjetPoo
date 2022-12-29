/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.engine.Input;
import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.*;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.decor.Box;
import fr.ubx.poo.ubomb.go.decor.bonus.*;

public class Player extends GameObject implements Movable, TakeVisitor , WalkVisitor {

    private Direction direction;
    private boolean moveRequested = false;
    private int lives; //on a enlevé le private final
    private int availableBombs;
    private int keys;
    private int range;

    private Input input;

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
        key.remove();
        this.keys = this.keys+1;
    }
    @Override
    public void take(Heart heart) {
        System.out.println("Take the heart ...");
        heart.remove();
        this.lives = this.lives+1;
    }
    @Override
    public void take(BombNumberInc bombnumberinc) {
        System.out.println("Take the bombnumbinc ...");
        bombnumberinc.remove();
        this.availableBombs = this.availableBombs+1;
    }
    @Override
    public void take(BombNumberDec bombnumberdec) {
        System.out.println("Take the bombnumbdec ...");
        bombnumberdec.remove();
        this.availableBombs = this.availableBombs-1;
    }
    @Override
    public void take(BombRangeInc bombrangeinc) {
        System.out.println("Take the bombrangeinc ...");
        bombrangeinc.remove();
        this.range = this.range+1;
    }
    @Override
    public void take(BombRangeDec bombrangedec) {
        System.out.println("Take the bombrangedec ...");
        bombrangedec.remove();
        this.range = this.range-1;
    }
    @Override
    public boolean walk(Box box) {
        if((new Box(game ,box.getPosition())).canMove(direction)){
            (new Box(game ,box.getPosition())).doMove(direction);
            return true;
        }
        return false;
    }

    public void doMove(Direction direction) {
        // This method is called only if the move is possible, do not check again
        Position nextPos = direction.nextPosition(getPosition());
        GameObject next = game.grid().get(nextPos);
        if (next instanceof Bonus bonus) {
                bonus.takenBy(this);
        }
        if (next instanceof Box box){
            if ((new Box(game ,nextPos)).canMove(direction)){
                Position nextnextPos = direction.nextPosition(nextPos);
                game.grid().get(nextPos).doMove(direction);
                game.grid().set(nextnextPos, game.grid().get(nextPos));
                game.grid().remove(nextPos);
            }
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

    public boolean walkableBy(Monster monster) { return true;}

    @Override
    public void explode() {
        // TODO
    }
}
