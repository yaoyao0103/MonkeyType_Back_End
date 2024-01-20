package team.epl.monkeytype.UserModule.Model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")

public class User {
    @Id
    private String id;
    @NotBlank
    private String username;
    private String biography;
    private String githubLink;
    private String twitterLink;
    private String websiteLink;
    private Integer unreadMessageCount;
    private TypeRecord record;
    private Message[] messages;
}
