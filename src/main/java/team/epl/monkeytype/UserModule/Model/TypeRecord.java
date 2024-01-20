package team.epl.monkeytype.UserModule.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TypeRecord {
    private String tenSec;
    private String thirtySec;
    private String SixtySec;
    private String OneHundredAndTwentySec;
}
