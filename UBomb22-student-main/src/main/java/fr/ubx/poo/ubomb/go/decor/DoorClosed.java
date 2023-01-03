package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Position;

public class DoorClosed extends Decor {

    public DoorClosed(Position position) {
        super(position);
    }
    @Override
    public boolean canMove(Direction direction){
        return false;
    }
    @Override
    public void doMove(Direction direction){
    }
    @Override
    public void explode() {

    }
}
