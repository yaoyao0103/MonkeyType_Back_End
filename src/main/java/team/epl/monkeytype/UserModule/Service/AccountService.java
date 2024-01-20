package team.epl.monkeytype.UserModule.Service;

import team.epl.monkeytype.UserModule.Exception.AccountCollectionException;
import team.epl.monkeytype.UserModule.Model.Account;

import java.util.List;

public interface AccountService {

    List<Account> getAllAccounts();
    Account getAccountByUsername(String username) throws AccountCollectionException;
    void createAccount(Account account) throws AccountCollectionException;
    void verifyRegisterCode(String username, String code) throws AccountCollectionException;
    Boolean checkAdmin(String username) throws AccountCollectionException;
    void deleteAccount(String username) throws AccountCollectionException;
    void resetPassword(String username, String oldPassword, String password) throws AccountCollectionException;
    Boolean checkPasswordIsValid(String username, String password) throws AccountCollectionException;
    Boolean checkPasswordFormat(String password);
    Boolean checkUsernameExists(String username) throws AccountCollectionException;
    Boolean checkEmailExists(String email) throws AccountCollectionException;

    String generateCode();
}
