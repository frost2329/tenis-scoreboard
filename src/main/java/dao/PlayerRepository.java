package dao;

import entity.Player;

public class PlayerRepository {
    public Player getByName(String name) {
        return Player.builder()
                .id(1L)
                .name(name)
                .build();
    }

    public Player create(String name) {
        return Player.builder()
                .id(1L)
                .name(name)
                .build();
    }
}
