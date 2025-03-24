package dao;

import entity.PlayerEntity;

public class PlayerRepository {
    public PlayerEntity getByName(String name) {
        return PlayerEntity.builder()
                .id(1)
                .name(name)
                .build();
    }

    public PlayerEntity create(String name) {
        return PlayerEntity.builder()
                .id(1)
                .name(name)
                .build();
    }
}
