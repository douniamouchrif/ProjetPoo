package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;


public class Bomb extends Bonus{

    private int status;

    public Bomb(Position position) {
        super(position);
        this.status = 3;
    }

    @Override
    public void explode() {}

    public int getStatus(){
        return status;
    }

    public void setStatus(int i){
        this.status = i;
    }

}
