package team.epl.monkeytype.UserModule.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import team.epl.monkeytype.UserModule.Model.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    @Query("{'username': ?0}")
    Optional<Account> findByUsername(String username);

    @Query("{'email': ?0}")
    Optional<Account> findByEmail(String email);

    void deleteByUsername(String username);
}
