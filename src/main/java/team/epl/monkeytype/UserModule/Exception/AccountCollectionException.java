package team.epl.monkeytype.UserModule.Exception;

import lombok.Getter;

@Getter
public class AccountCollectionException extends Exception {

    private final int statusCode;

    public AccountCollectionException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public static AccountCollectionException userNotFoundException() {
        return new AccountCollectionException("Account is not found.", 404);
    }

    public static AccountCollectionException usernameAlreadyExists() {
        return new AccountCollectionException("Duplicated username.", 409);
    }

    public static AccountCollectionException emailAlreadyExists() {
        return new AccountCollectionException("i email.", 409);
    }

    public static AccountCollectionException invalidPassword() {
        return new AccountCollectionException("Invalid password.", 401);
    }

    public static AccountCollectionException invalidPasswordFormat() {
        return new AccountCollectionException("Invalid password format.", 400);
    }

    public static AccountCollectionException accountActivationConflict(){
        return new AccountCollectionException("The account has already been activated.", 409);
    }

    public static AccountCollectionException invalidRequestFormat(){
        return new AccountCollectionException("Invalid request format.", 400);
    }

    public static AccountCollectionException invalidVerificationCode(){
        return new AccountCollectionException("Invalid activation code.", 400);
    }
}
