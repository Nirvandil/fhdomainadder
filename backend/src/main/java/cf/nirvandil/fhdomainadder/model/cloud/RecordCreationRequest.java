package cf.nirvandil.fhdomainadder.model.cloud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents request for creating DNS record on Cloudflare DNS.
 * @see <a href=https://api.cloudflare.com/#dns-records-for-a-zone-create-dns-record>Cloudflare API</a>
 */
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
