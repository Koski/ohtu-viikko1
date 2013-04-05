package ohtu.services;

import ohtu.domain.User;
import java.util.ArrayList;
import java.util.List;
import ohtu.data_access.FileUserDao;
import ohtu.data_access.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class AuthenticationService {

    private UserDao userDao;
    private FileUserDao fileUserDao;
    @Autowired
    public AuthenticationService(FileUserDao fileUserDao) {
        this.fileUserDao = fileUserDao;
    }

    public boolean logIn(String username, String password) {
        for (User user : fileUserDao.listAll()) {
            if (user.getUsername().equals(username)
                    && user.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }

    public boolean createUser(String username, String password) {
        if (fileUserDao.findByName(username) != null) {
            return false;
        }

        if (invalid(username, password)) {
            return false;
        }

        fileUserDao.add(new User(username, password));

        return true;
    }

    private boolean invalid(String username, String password) {
        if (password.length() < 8 || username.length()<3 || !password.matches(".*\\d.*")) {
            return true;
        }
        return false;
    }
}
