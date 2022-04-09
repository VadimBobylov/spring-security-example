package security.example.auth;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static security.example.security.UserRole.*;

@Repository("fake")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FakeUserDaoService implements UserDao {
    PasswordEncoder passwordEncoder;


    @Override
    public Optional<User> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst();
    }

    private List<User> getApplicationUsers() {
        return Lists.newArrayList(
                new User(passwordEncoder.encode("password"), "student", STUDENT.getGrantedAuthority(), true, true, true, true),
                new User(passwordEncoder.encode("password"), "admin", ADMIN.getGrantedAuthority(), true, true, true, true),
                new User(passwordEncoder.encode("password"), "admintrainee", ADMINTRAINEE.getGrantedAuthority(), true, true, true, true)
        );
    }
}
