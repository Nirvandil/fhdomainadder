package cf.nirvandil.fhdomainadder.model.panel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckCgiRequest {
    ConnectionDetails connectionDetails;
    String user;
}
