package hr.fitbit.demo.fitbitconnect.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Entity(name = "roles")
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"role_id", "name"}, name = "role_id_and_name_unique")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity implements Serializable {

    private static final long serialVersionUID = 5313493413859894495L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_roles_generator")
    @SequenceGenerator(name = "pk_roles_generator", sequenceName = "pk_roles_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Collection<UserEntity> users = new HashSet<>();

    @PrePersist
    public void createRoleId() {
        this.roleId = UUID.randomUUID();
    }

    public void addUser(UserEntity user) {
        this.users.add(user);
    }

    public void addAllUsers(Collection<UserEntity> users) {
        this.users.addAll(users);
    }

    public void removeUser(UserEntity user) {
        this.users.remove(user);
    }

    public void removeAllUsers(Collection<UserEntity> users) {
        this.users.removeAll(users);
    }

    public void retainAllUsers(Collection<UserEntity> users) {
        this.users.retainAll(users);
    }

}
