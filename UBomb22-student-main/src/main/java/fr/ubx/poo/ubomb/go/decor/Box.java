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

    public Box(Game game, Position position) {
        super(game, position);
    }

    @Override
    public boolean walkableBy(Player player){
        return player.walk(this);
    }
    @Override
    public boolean canMove(Direction direction) {
        //this.walkableBy(player);
        Position nextPos = direction.nextPosition(getPosition());
        if(!game.grid().inside(nextPos)){
            return false;
        }
        if ( game.grid().get(nextPos) == null){
            return true;
        }
        return game.grid().get(nextPos).walkableBy(this);
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        game.grid().remove(getPosition());
        (new Box(nextPos)).setPosition(nextPos);
        System.out.println("test");
    }

}
