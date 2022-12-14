package fr.ubx.poo.ubomb.go;

import fr.ubx.poo.ubomb.go.decor.bonus.*;

// Double dispatch visitor pattern
public interface TakeVisitor {
    // Key
    default void take(Key key) {}
    default void take(Heart heart) {}
    default void take(BombNumberInc bombnumberinc) {}
    default void take(BombNumberDec bombnumberdec) {}
    default void take(BombRangeInc bombrangeinc) {}
    default void take(BombRangeDec bombrangedec) {}
}
