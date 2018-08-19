package cf.nirvandil.fhdomainadder.model.cloud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents abstract Cloudflare response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
abstract class CommonCreationResponse {
    private boolean success;
    private List<CloudError> errors;
    private List<String> messages;
}

/**
 * Description of Cloudflare reported error.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class CloudError {
    private int code;
    private String message;
}