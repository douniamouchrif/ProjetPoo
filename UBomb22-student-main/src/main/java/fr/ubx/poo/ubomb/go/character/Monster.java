package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;

public class Monster extends GameObject implements Movable {

    private Direction direction;
    private long lastMove;

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

    public void update(long now) {
            if (now >= lastMove){
                lastMove = now + 10 * 1000000000L / game.configuration().monsterVelocity();
                requestMove(Direction.random());
                if (canMove(direction)) {
                    doMove(direction);
                    lastMove += (10 * 1000000000L / game.configuration().monsterVelocity());
                }
                else {
                    direction = Direction.random();
                }
            }
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
    }

    public void explode() {
        remove();
    }
}
