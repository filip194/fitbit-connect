package hr.fitbit.demo.fitbitconnect.repository;

import hr.fitbit.demo.fitbitconnect.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUserId(UUID userId);

    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT u FROM users u WHERE LOWER(:username) = null OR LOWER(u.username) LIKE LOWER(CONCAT('%',:username,'%'))")
    Page<UserEntity> findByUsernameContaining(@Param("username") String username, Pageable pageRequest);
}

