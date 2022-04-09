package security.example.auth;

import java.util.Optional;

public interface UserDao {
    Optional<User> selectApplicationUserByUsername(String username);
}
