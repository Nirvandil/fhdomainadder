package cf.nirvandil.fhdomainadder.model.cloud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents request for creating DNS zone record.
 * @see <a href=https://api.cloudflare.com/#zone-create-zone>Cloudflare API</a>
 */
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
