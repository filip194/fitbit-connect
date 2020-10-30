package hr.fitbit.demo.fitbitconnect.fixture;

import hr.fitbit.demo.fitbitconnect.entity.RoleEntity;

public class RoleFixture {

    public static RoleEntity createRoleEntity(String name) {
        final RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        return roleEntity;
    }
}
