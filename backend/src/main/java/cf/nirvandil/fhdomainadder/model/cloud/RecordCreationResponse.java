package cf.nirvandil.fhdomainadder.model.cloud;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents response for after sending {@link RecordCreationRequest}.
 */
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RecordCreationResponse extends CommonCreationResponse {
}
