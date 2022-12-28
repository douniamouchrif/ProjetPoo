package fr.ubx.poo.ubomb.go;

import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.Box;

public interface Walkable {
    default boolean walkableBy(Player player) { return false;}
    //default boolean walkableBy(Box box) { return true;}
    default boolean walkableBy(Monster monster) { return true;}
}
