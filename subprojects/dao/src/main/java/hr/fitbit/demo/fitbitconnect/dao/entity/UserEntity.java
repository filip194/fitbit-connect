package hr.fitbit.demo.fitbitconnect.dao.entity;

import hr.fitbit.demo.fitbitconnect.apisupport.apimodel.user.UserType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Entity(name = "users")
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "username"}, name = "user_id_and_username_unique")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 5313493413859894402L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_users_generator")
    @SequenceGenerator(name = "pk_users_generator", sequenceName = "pk_users_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private UserType type;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "age")
    private Integer age;

    @Column(name = "f_user_id")
    private String fUserId;

    @Column(name = "f_access_token")
    private String fAccessToken;

    @Column(name = "f_refresh_token")
    private String fRefreshToken;

    @Column(name = "created", nullable = false)
    @CreationTimestamp
    private Timestamp created;

    @Column(name = "updated", nullable = false)
    @UpdateTimestamp
    private Timestamp updated;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_entity_id", referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "user_entity_id_fk_user_id")),
            inverseJoinColumns = @JoinColumn(name = "role_entity_id", referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "role_entity_id_fk_role_id")))
    private Collection<RoleEntity> roles = new HashSet<>();

    @PrePersist
    public void createUserId() {
        this.userId = UUID.randomUUID();
    }

    public void addRole(RoleEntity role) {
        this.roles.add(role);
    }

    public void addAllRoles(Collection<RoleEntity> roles) {
        this.roles.addAll(roles);
    }

    public void removeRole(RoleEntity role) {
        this.roles.remove(role);
    }

    public void removeAllRoles(Collection<RoleEntity> roles) {
        this.roles.removeAll(roles);
    }

    public void retainAllRoles(Collection<RoleEntity> roles) {
        this.roles.retainAll(roles);
    }

    public void reassignRoles(Collection<RoleEntity> roles) {
        this.roles.stream().filter(roleEntity -> !roles.contains(roleEntity)).forEach(roleEntity -> roleEntity.removeUser(this));
        roles.stream().filter(roleEntity -> !this.roles.contains(roleEntity)).forEach(roleEntity -> roleEntity.addUser(this));
    }

}
