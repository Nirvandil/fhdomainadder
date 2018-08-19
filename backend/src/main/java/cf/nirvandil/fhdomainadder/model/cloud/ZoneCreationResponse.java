package cf.nirvandil.fhdomainadder.model.cloud;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Represents response after sending {@link ZoneCreationRequest}.
 */
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ZoneCreationResponse extends CommonCreationResponse {
    private ZoneCreationResult result;
}

/**
 * Describes name servers, assigned to created zone by Cloudflare.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ZoneCreationResult {
    private String id;
    @JsonProperty("name_servers")
    private List<String> nameServers;
}