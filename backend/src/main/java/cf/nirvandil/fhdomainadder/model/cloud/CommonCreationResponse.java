package cf.nirvandil.fhdomainadder.model.cloud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
abstract class CommonCreationResponse {
    private boolean success;
    private List<CloudError> errors;
    private List<String> messages;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CloudError {
    private int code;
    private String message;
}