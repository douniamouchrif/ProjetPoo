package fr.ubx.poo.ubomb.game;

public record Configuration(Position playerPosition, Position monsterPosition, int bombBagCapacity, int playerLives, long playerInvisibilityTime,
                            int monsterVelocity, long monsterInvisibilityTime) {
}
