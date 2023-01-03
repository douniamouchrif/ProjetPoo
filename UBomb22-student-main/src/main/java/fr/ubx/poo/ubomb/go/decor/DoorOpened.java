package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class DoorOpened extends Decor{

    public DoorOpened(Position position) {
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
    @Override
    public boolean walkableBy(Player player) { return true;}
}
