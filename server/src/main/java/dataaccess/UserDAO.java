package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

public interface UserDAO extends DataAccessObject {

    void createUser(UserData data);

    UserData getUser(String username);

}
