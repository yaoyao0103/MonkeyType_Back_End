package team.epl.monkeytype.UserModule.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import team.epl.monkeytype.UserModule.Exception.AccountCollectionException;
import team.epl.monkeytype.UserModule.Model.Account;
import team.epl.monkeytype.UserModule.Repository.AccountRepository;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteSource;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    public AccountRepository accountRepo;
    public BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<Account> getAllAccounts(){
        List<Account> accounts = accountRepo.findAll();
        if(!accounts.isEmpty()) return accounts;
        else return new ArrayList<Account>();
    }

    @Override
    public Account getAccountByUsername(String username) throws AccountCollectionException {
        Optional<Account> account = accountRepo.findByUsername(username);
        if(account.isPresent()) return account.get();
        else throw AccountCollectionException.userNotFoundException();
    }

    @Override
    public void createAccount(Account account) throws AccountCollectionException {
        if(checkUsernameExists(account.getUsername())) throw AccountCollectionException.usernameAlreadyExists();
        if(checkEmailExists(account.getEmail())) throw AccountCollectionException.emailAlreadyExists();
        if(!checkPasswordFormat(account.getPassword())) throw AccountCollectionException.invalidPasswordFormat();
        account.setCreatedDate(new Date(System.currentTimeMillis()));
        String hashedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(hashedPassword);
        account.setIsAdmin(false);
        account.setIsActivated(false);
        account.setVerificationCode(generateCode());
        accountRepo.save(account);
    }

    @Override
    public void verifyRegisterCode(String username, String code) throws AccountCollectionException{
        Account account = getAccountByUsername(username);
        if(account.getIsActivated()) throw AccountCollectionException.accountActivationConflict();
        if(account.getVerificationCode().equals(code)){
            account.setIsActivated(true);
            accountRepo.save(account);
        }
        else throw AccountCollectionException.invalidVerificationCode();
    }

    @Override
    public Boolean checkAdmin(String username) throws AccountCollectionException{
        Account account = getAccountByUsername(username);
        return account.getIsAdmin();
    }

    @Override
    public void deleteAccount(String username) throws AccountCollectionException {
        if(!checkUsernameExists(username)) throw AccountCollectionException.userNotFoundException();
        else accountRepo.deleteByUsername(username);
    }

    @Override
    public void resetPassword(String username, String oldPassword, String newPassword) throws AccountCollectionException {
        if(!checkPasswordFormat(newPassword)) throw AccountCollectionException.invalidPasswordFormat();
        if(!checkPasswordIsValid(username, oldPassword)) throw AccountCollectionException.invalidPassword();
        Account account = getAccountByUsername(username);
        String hashedPassword = passwordEncoder.encode(newPassword);
        account.setPassword(hashedPassword);
        accountRepo.save(account);
    }

    @Override
    public Boolean checkUsernameExists(String username) {
        Optional<Account> accountWithUsername = accountRepo.findByUsername(username);
        return accountWithUsername.isPresent();
    }

    @Override
    public Boolean checkEmailExists(String email) {
        Optional<Account> accountWithEmail = accountRepo.findByEmail(email);
        return accountWithEmail.isPresent();
    }

    @Override
    public Boolean checkPasswordIsValid(String username, String password) throws AccountCollectionException {
        Account account = getAccountByUsername(username);
        return passwordEncoder.matches(password, account.getPassword());
    }

    @Override
    public Boolean checkPasswordFormat(String password){
        return true;
    }

    @Override
    public String generateCode(){
        final int LENGTH = 20;
        SecureRandom secureRandom  = new SecureRandom();
        byte[] randomBytes = new byte[LENGTH];
        secureRandom.nextBytes(randomBytes);
        String base64Encoded = BaseEncoding.base64Url().encode(randomBytes);
        return base64Encoded.substring(0, LENGTH);
    }
}
