package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;

public class Monster extends GameObject implements Movable {

    private Direction direction;
    private boolean moveRequested = false;
    //private int lives;

    public Monster(Game game, Position position) {
        super(game, position);
        this.direction = Direction.DOWN;
        //this.lives = game.configuration().playerLives();
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean canMove(Direction direction) {
        return true;
    }

    @Override
    public void doMove(Direction direction) {



    }
}
