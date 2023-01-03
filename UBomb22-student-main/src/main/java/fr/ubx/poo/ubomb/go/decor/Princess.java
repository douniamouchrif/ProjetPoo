package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Princess extends Decor {

    public Princess(Position position) {
        super(position);
    }
    @Override
    public boolean walkableBy(Player player) {
        return true;
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
