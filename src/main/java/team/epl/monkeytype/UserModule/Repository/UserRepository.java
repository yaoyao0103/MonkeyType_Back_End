package team.epl.monkeytype.UserModule.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import team.epl.monkeytype.UserModule.Model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
