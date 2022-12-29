package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;

public class Monster extends GameObject implements Movable {

    private Direction direction;
    private long lastMove;

    Timer time;

    //private int lives;

    public Monster(Game game, Position position) {
        super(game, position);
        this.direction = Direction.random();
        //this.lives = game.configuration().playerLives();
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        if(!game.grid().inside(nextPos)){
            return false;
        }
        if (game.grid().get(nextPos) == null){
            return true;
        }
        return game.grid().get(nextPos).walkableBy(this);
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    /*public void update(long now) {
        if (canMove(direction)) {
            doMove(direction);
            lastMove = now + 10 * 1000000000L / game.configuration().monsterVelocity();
            if (now >= lastMove){
                requestMove(Direction.random());
                lastMove += (10 * 1000000000L / game.configuration().monsterVelocity());
            }
        } else {
            direction = Direction.random();
        }
    }*/

    public void update(long now) {
        lastMove = now + 10 * 1000000000L / game.configuration().monsterVelocity();
        if (now >= lastMove) {
            requestMove(Direction.random());
            lastMove += (10 * 1000000000L / game.configuration().monsterVelocity());
        }
        if (canMove(direction)) {
            doMove(direction);
        } else {
            direction = Direction.random();
        }
        time.update(now);
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
    }
}
