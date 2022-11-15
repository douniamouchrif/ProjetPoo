package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;

public class Box extends Decor implements Movable{
    public Box(Position position) {
        super(position);
    }

    @Override
    public boolean canMove(Direction direction) {
        if(direction == Direction.UP){
            if (getPosition().y() <= 0){
                return false;
            }
            if (game.grid().get(new Position(getPosition().x(),getPosition().y()-1)) instanceof Tree ){
                return false;
            }
            if (game.grid().get(new Position(getPosition().x(),getPosition().y()-1)) instanceof Stone ){
                return false;
            }
        }
        if(direction == Direction.DOWN){
            if (getPosition().y() >= game.grid().height()-1){
                return false;
            }
            if (game.grid().get(new Position(getPosition().x(),getPosition().y()+1)) instanceof Tree ){
                return false;
            }
            if (game.grid().get(new Position(getPosition().x(),getPosition().y()+1)) instanceof Stone ){
                return false;
            }
        }
        if(direction == Direction.LEFT){
            if (getPosition().x() <= 0){
                return false;
            }
            if (game.grid().get(new Position(getPosition().x()-1,getPosition().y())) instanceof Tree ){
                return false;
            }
            if (game.grid().get(new Position(getPosition().x()-1,getPosition().y())) instanceof Stone ){
                return false;
            }
        }
        if(direction == Direction.RIGHT){
            if (getPosition().x() >= game.grid().width()-1){
                return false;
            }
            if (game.grid().get(new Position(getPosition().x()+1,getPosition().y())) instanceof Tree ){
                return false;
            }
            if (game.grid().get(new Position(getPosition().x()+1,getPosition().y())) instanceof Stone ){
                return false;
            }
        }

        return true;
    }

    @Override
    public void doMove(Direction direction) {

    }
}
