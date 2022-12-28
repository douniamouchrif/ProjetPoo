package fr.ubx.poo.ubomb.game;

public record Configuration(Position playerPosition, Position monsterPosition, int bombBagCapacity, int playerLives, int keys, int range, long playerInvisibilityTime,
                            int monsterVelocity, long monsterInvisibilityTime) {
}
