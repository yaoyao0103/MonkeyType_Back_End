package team.epl.monkeytype.UserModule.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.epl.monkeytype.UserModule.Exception.AccountCollectionException;
import team.epl.monkeytype.UserModule.Model.Account;
import team.epl.monkeytype.UserModule.Service.AccountService;

import java.util.List;
import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/users")
    public List<Account> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getAccountByUsername(@PathVariable("username") String username){
        try{
            Account account = accountService.getAccountByUsername(username);
            return ResponseEntity.status(HttpStatus.OK).body(account);
        }
        catch(AccountCollectionException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @PostMapping("/user")
    public ResponseEntity<?> createAccount(@RequestBody Account account){
        try{
            if(account.getUsername().isEmpty() || account.getEmail().isEmpty() || account.getPassword().isEmpty()) throw AccountCollectionException.invalidRequestFormat();
            accountService.createAccount(account);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        catch(AccountCollectionException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<?> deleteAccount(@PathVariable("username") String username){
        try{
            accountService.deleteAccount(username);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch(AccountCollectionException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @PutMapping("user/resetPassword/{username}")
    public ResponseEntity<?> resetPassword(@PathVariable("username") String username, @RequestBody Map<String, String> request){
        try{
            if(!request.containsKey("oldPassword") || !request.containsKey("newPassword")) throw AccountCollectionException.invalidRequestFormat();
            accountService.resetPassword(username, request.get("oldPassword"), request.get("newPassword"));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch(AccountCollectionException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @PostMapping("/user/verifyRegisterCode")
    public ResponseEntity<?> verifyRegisterCode(@RequestBody Map<String, String> request){
        try{
            if(!request.containsKey("username") || !request.containsKey("code")) throw AccountCollectionException.invalidRequestFormat();
            accountService.verifyRegisterCode(request.get("username"), request.get("code"));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch(AccountCollectionException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @GetMapping("/user/checkAdmin/{username}")
    public ResponseEntity<?> checkAdmin(@PathVariable("username") String username){
        try{
            if(accountService.checkAdmin(username)) return ResponseEntity.status(HttpStatus.OK).body("User has admin privileges.");
            else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have admin privileges.");
        }
        catch(AccountCollectionException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }


//    @PostMapping("/user/login")
//    public ResponseEntity<?> loggedIn(@RequestBody MultiValueMap<String, String> request){
//        try{
//            if(!request.containsKey("username") || !request.containsKey("code")) throw AccountCollectionException.invalidRequestFormat();
//            accountService.verifyRegisterCode(request.getFirst("username"), request.getFirst("code"));
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//        catch(AccountCollectionException e){
//            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
//        }
//    }

}
