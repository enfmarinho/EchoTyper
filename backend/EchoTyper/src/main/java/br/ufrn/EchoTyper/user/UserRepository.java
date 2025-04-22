package br.ufrn.EchoTyper.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
   public Optional<User> findById(Long id);
   public Optional<User> findByUsername(String username);
   public Optional<User> findByEmail(String email);

   @Query("SELECT u.friends FROM User u WHERE u.id = :id")
   public Optional<List<User>> findAllFriends(@Param("id") Long id);
   
   @Query("SELECT u.friends FROM User u WHERE u.username = :username")
   public Optional<User> findFriendByUsername(@Param("username") String username);
}
