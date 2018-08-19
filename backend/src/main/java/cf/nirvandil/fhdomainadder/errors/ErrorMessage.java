package cf.nirvandil.fhdomainadder.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response body with message for all exceptions, that may encounter while work with Cloudflare or panel API.
 */
@AllArgsConstructor
class ErrorMessage {
    @Getter
    private String message;
}
