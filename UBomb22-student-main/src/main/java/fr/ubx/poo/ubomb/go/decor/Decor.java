package fr.ubx.poo.ubomb.go.decor;


import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.character.Player;

public abstract class Decor extends GameObject implements Movable{

    public Decor(Game game, Position position) {
        super(game, position);
    }

    public Decor(Position position) {
        super(position);
    }

    abstract public boolean canMove(Direction direction);
    abstract public void doMove(Direction direction);

    @Override
    public void explode() {
    }

}