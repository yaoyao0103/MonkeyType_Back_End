package team.epl.monkeytype.UserModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import team.epl.monkeytype.UserModule.Model.Account;
import team.epl.monkeytype.UserModule.Repository.AccountRepository;
import team.epl.monkeytype.UserModule.Service.AccountService;

import org.springframework.http.HttpHeaders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountTest {

    private HttpHeaders httpHeaders;

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    private final String testUsername = "bob";
    private final String testEmail = "bob@gmail.com";
    private final String testPassword = "1234";

    private final String testNewPassword = "12345";

    private String testCode = "";

    public void createAccount() throws Exception{
        Account account = new Account();
        account.setUsername(testUsername);
        account.setEmail(testEmail);
        account.setPassword(testPassword);
        accountService.createAccount(account);
    }

    @BeforeEach
    public void init() throws Exception {
        httpHeaders = new HttpHeaders();
        objectMapper = new ObjectMapper();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        accountRepository.deleteAll();
        createAccount();
    }

    @Test
    public void testCreateAccount() throws Exception {
        accountRepository.deleteAll();
        Account account = new Account();
        account.setUsername(testUsername);
        account.setEmail(testEmail);
        account.setPassword(testPassword);

        String accountJson = objectMapper.writeValueAsString(account);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user")
                .headers(httpHeaders)
                .content(accountJson);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateAccountWithDuplicateUsername() throws Exception {
        Account account = new Account();
        account.setUsername(testUsername);
        account.setEmail("alice@gmai.com");
        account.setPassword(testPassword);

        String accountJson = objectMapper.writeValueAsString(account);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user")
                .headers(httpHeaders)
                .content(accountJson);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict());
    }

    @Test
    public void testCreateAccountWithDuplicateEmail() throws Exception {
        Account account = new Account();
        account.setUsername("alice");
        account.setEmail(testEmail);
        account.setPassword(testPassword);

        String accountJson = objectMapper.writeValueAsString(account);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user")
                .headers(httpHeaders)
                .content(accountJson);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetAccountByUsername() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/" + testUsername)
                .headers(httpHeaders);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(testUsername))
                .andExpect(jsonPath("$.email").value(testEmail))
                .andExpect(jsonPath("$.isActivated").value(false))
                .andExpect(jsonPath("$.isAdmin").value(false))
                .andExpect(jsonPath("$.isAdmin").isNotEmpty());
    }

    @Test
    public void testGetAccountByNonexistentUsername() throws Exception {
        accountRepository.deleteAll();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/" + testUsername)
                .headers(httpHeaders);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllAccounts() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users" )
                .headers(httpHeaders);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(List.class)));
    }

    @Test
    public void testResetPassword() throws Exception {

        JSONObject request = new JSONObject()
                .put("oldPassword", testPassword)
                .put("newPassword", testNewPassword);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/resetPassword/" + testUsername)
                .headers(httpHeaders)
                .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

    }

    @Test
    public void testResetWithWrongPassword() throws Exception {

        JSONObject request = new JSONObject()
                .put("oldPassword", "54321")
                .put("newPassword", testNewPassword);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/resetPassword/" + testUsername)
                .headers(httpHeaders)
                .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testVerifyRegisterCode() throws Exception {

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/user/" + testUsername));
        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();
        Account responseObject = objectMapper.readValue(jsonResponse, Account.class);
        Assertions.assertNotNull(responseObject);
        testCode = responseObject.getVerificationCode();

        JSONObject request = new JSONObject()
                .put("username", testUsername)
                .put("code", testCode);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/verifyRegisterCode")
                .headers(httpHeaders)
                .content(request.toString());


        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    public void testVerifyDuplicateRegisterCode() throws Exception {

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/user/" + testUsername));
        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();
        Account responseObject = objectMapper.readValue(jsonResponse, Account.class);
        Assertions.assertNotNull(responseObject);
        testCode = responseObject.getVerificationCode();

        JSONObject request = new JSONObject()
                .put("username", testUsername)
                .put("code", testCode);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/verifyRegisterCode")
                .headers(httpHeaders)
                .content(request.toString());


        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
        mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict());
    }

    @Test
    public void testCheckAdmin() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/checkAdmin/" + testUsername)
                .headers(httpHeaders);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteUser() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/user/" + testUsername)
                .headers(httpHeaders);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteDuplicateUser() throws Exception {
        accountRepository.deleteAll();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/user/" + testUsername)
                .headers(httpHeaders);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }



}
