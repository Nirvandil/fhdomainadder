package cf.nirvandil.fhdomainadder.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchUsersException.class)
    public ErrorMessage handleNoUsers() {
        return new ErrorMessage("Похоже, в панели не существует ни одного пользователя.");
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(PanelDoesNotExistException.class)
    public ErrorMessage handleNoPanel() {
        return new ErrorMessage("Работа возможна лишь с VESTA и ISPmanager 4/5.");
    }
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(HostNotAllowedException.class)
    public ErrorMessage handleAlienNet() {
        return new ErrorMessage("Работа возможна лишь с сетями компании FriendHosting!");
    }
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(CgiNotSupportedException.class)
    public ErrorMessage handleNoCgi() {
        return new ErrorMessage("Данному пользователю не разрешено использовать РНР в режиме CGI!");
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Throwable.class)
    public ErrorMessage handleOtherErrors(Exception e) {
        return new ErrorMessage(e.getMessage());
    }
}
