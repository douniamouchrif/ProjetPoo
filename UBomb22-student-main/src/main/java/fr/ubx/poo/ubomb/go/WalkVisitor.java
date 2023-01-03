package fr.ubx.poo.ubomb.go;

import fr.ubx.poo.ubomb.go.decor.*;

// Double dispatch visitor pattern
public interface WalkVisitor {
    default boolean walk(Box box) {return false;}

}