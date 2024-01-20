package team.epl.monkeytype.UserModule.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team.epl.monkeytype.UserModule.Model.Account;
import team.epl.monkeytype.UserModule.Repository.AccountRepository;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    AccountRepository userRepo;
}
