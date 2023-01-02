package fr.ubx.poo.ubomb.launcher;

import fr.ubx.poo.ubomb.game.Configuration;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Level;
import fr.ubx.poo.ubomb.game.Position;
import javafx.beans.property.StringProperty;

import java.io.*;
import java.util.Properties;

public class GameLauncher implements MapRepo{

    public static Game load() {
        Position[] position = new Position[3];
        position[0] = new Position(10,10);
        position[1] = new Position(1,7);
        position[2] = new Position(8,2);
        Configuration configuration = new Configuration(new Position(0, 0), position, 3, 5, 0, 1, 4000, 10, 1000);

        return new Game(configuration, new Level(new MapLevelDefault()));
    }

    /*private static boolean booleanProperty(Properties config, String name, boolean defaultValue) {
        return Boolean.parseBoolean(config.getProperty(name, Boolean.toString(defaultValue)));
    }
    private static int intProperty(Properties config, String name, int defaultValue) {
        return Integer.parseInt(config.getProperty(name, Integer.toString(defaultValue)));
    }
    private static long longProperty(Properties config, String name, long defaultValue) {
        return Long.parseLong(config.getProperty(name, Long.toString(defaultValue)));
    }
    private MapLevel StringProperty(Properties config, String name, MapLevel defaultValue) {
        // String.parseString(config.getProperty(name, String.toString(defaultValue)));
        return load(config.getProperty(name,export(defaultValue)));
    }
    public Game load(File file) throws IOException {

        Properties config = new Properties();;
        Reader in = new FileReader(file);
        config.load(in);
        boolean compression = booleanProperty(config, "compression", false);
        int levels = intProperty(config, "levels", 1);
        MapLevel level1 = StringProperty(config, "level1", new MapLevelDefault());
        MapLevel level2 = StringProperty(config, "level2", new MapLevelDefault());
        MapLevel level3 = StringProperty(config, "level3", new MapLevelDefault());
        int playerLives = intProperty(config, "playerLives", 5);
        int monsterVelocity = intProperty(config, "monsterVelocity", 10);
        long playerInvisibilityTime = longProperty(config, "playerInvisibilityTime", 4000);
        long monsterInvisibilityTime = longProperty(config, "monsterInvisibilityTime", 1000);
        Position player = new Position(0,0);

        MapLevel[] mapLevels = new MapLevel[3];
        mapLevels[0] = level1;
        mapLevels[1] = level2;
        mapLevels[2] = level3;

        Position[] position = new Position[3];
        position[0] = new Position(10,10);
        position[1] = new Position(1,7);
        position[2] = new Position(8,2);
        Configuration configuration = new Configuration(player, position, 3, playerLives, 0, 1, playerInvisibilityTime, monsterVelocity, monsterInvisibilityTime);

        return new Game(configuration, new Level(mapLevels[0]));
    }*/

    static final char EOL = 'x';

    public MapLevel load(String string) {

        StringBuilder s = new StringBuilder();
        int len = string.length();

        for (int io = 0; io < len; io++) {
            if (string.charAt(io) == EOL) {
                s.append(EOL);
            } else if (!Character.isDigit(string.charAt(io)) && Character.isDigit(string.charAt(io + 1))) {
                for (int i = 0; i < string.charAt(io + 1) - 48; i++) {
                    s.append(string.charAt(io));
                }

            } else if (!Character.isDigit(string.charAt(io))) {
                s.append(string.charAt(io));
            }
        }

        String strad = s.toString();

        int cpt = 0;
        for (int i = 0; i < strad.length(); i++) {
            if (strad.charAt(i) == EOL) {
                cpt++;
            }
        }

        int index = strad.indexOf(EOL);

        if (index == -1) {
            throw new MapException("Missing eol character.");

        }
        MapLevel mapLevel = new MapLevel(index, cpt);
        Entity entity;
        int p = 0;
        for (int i = 0; i < mapLevel.height(); i++) {
            for (int j = 0; j < mapLevel.width(); j++) {
                if (strad.charAt(p) == EOL) {
                    p++;
                }
                entity = Entity.fromCode(strad.charAt(p));
                p++;
                mapLevel.set(j, i, entity);
            }
        }
        return mapLevel;
    }

    public String export(MapLevel mapLevel) {

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < mapLevel.height(); i++) {
            for (int j = 0; j < mapLevel.width(); j++) {
                s.append(mapLevel.get(j, i).getCode());
            }

            s.append(EOL);
        }

        System.out.println(s);
        return s.toString();
    }

    public MapLevel load(Reader in) throws IOException {
        BufferedReader br = new BufferedReader(in);
        StringBuilder ss = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            ss.append(line);
        }
        in.close();
        return load(ss.toString());
    }

}
