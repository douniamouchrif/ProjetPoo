package fr.ubx.poo.ubomb.go;

import fr.ubx.poo.ubomb.go.character.Princess;
import fr.ubx.poo.ubomb.go.decor.bonus.*;
import fr.ubx.poo.ubomb.go.decor.*;

// Double dispatch visitor pattern
public interface WalkVisitor {

    default boolean walk(Princess princess) {return false;}
    default boolean walk(Box box) {return false;}

    //default boolean walk(Decor decor) {return false;}


}