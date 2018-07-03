package cf.nirvandil.fhdomainadder.model.cloud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneCreationRequest {
    private String apiKey;
    private String email;
    private String name;
    private boolean jumpStart;
}
