package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;

import java.util.LinkedList;
import java.util.List;

public class Game {

    private final Configuration configuration;
    private final Player player;
    private final Grid grid;
    private final Monster[] monster;

    public Game(Configuration configuration, Grid grid) {
        this.configuration = configuration;
        this.grid = grid;
        player = new Player(this, configuration.playerPosition());
        monster = new Monster[configuration.monsterPosition().length];
        for (int i=0; i < configuration.monsterPosition().length; i++){
            monster[i] = new Monster(this, configuration.monsterPosition()[i]);
        }
        //monster = new Monster(this, configuration.monsterPosition());
    }

    public Configuration configuration() {
        return configuration;
    }

    // Returns the player, monsters and bomb at a given position
    public List<GameObject> getGameObjects(Position position) {
        List<GameObject> gos = new LinkedList<>();
        if (player().getPosition().equals(position))
            gos.add(player);
        for(int i = 0; i< configuration.monsterPosition().length; i++){
            if (monster()[i].getPosition().equals(position))
                gos.add(monster[i]);
        }
        return gos;
    }

    public Grid grid() {
        return grid;
    }

    public Player player() {
        return this.player;
    }

    public Monster[] monster() {
        return this.monster;
    }

}
