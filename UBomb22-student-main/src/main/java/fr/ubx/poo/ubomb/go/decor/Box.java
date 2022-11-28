package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.WalkVisitor;
import fr.ubx.poo.ubomb.go.Walkable;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.bonus.Bonus;

public class Box extends Decor implements Movable {
    public Box(Position position) {
        super(position);
    }

    @Override
    public boolean canMove(Direction direction) {
        //this.walkableBy(Player player);
        return true; //faire appel a walkableby
    }

    @Override
    public void doMove(Direction direction) {

    }

    @Override
    public boolean walkableBy(Player player) {
        return player.walk(this);
    }
}
