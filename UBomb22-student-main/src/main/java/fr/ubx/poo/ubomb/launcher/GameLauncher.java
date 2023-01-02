package fr.ubx.poo.ubomb.launcher;

import fr.ubx.poo.ubomb.game.Configuration;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Level;
import fr.ubx.poo.ubomb.game.Position;

public class GameLauncher {

    public static Game load() {
        Position[] position = new Position[3];
        position[0] = new Position(10,10);
        position[1] = new Position(1,7);
        position[2] = new Position(8,2);
        Configuration configuration = new Configuration(new Position(0, 0), position, 3, 5, 0, 1, 4000, 10, 1000);

        return new Game(configuration, new Level(new MapLevelDefault()));
    }

}
