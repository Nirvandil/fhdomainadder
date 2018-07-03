package cf.nirvandil.fhdomainadder.model.cloud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordCreationRequest {
    private String type;
    private String name;
    private String content;
    private boolean proxied;
    private String apiKey;
    private String email;
}
