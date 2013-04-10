package ohtu.data_access;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.soap.SOAPBinding;
import ohtu.domain.User;

/**
 *
 * @author anttkari
 */
public class FileUserDao implements UserDao{
    
    File file;
    Scanner reader;
    
    public FileUserDao(String fileName) {
        file = new File(fileName);
    }
    
    @Override
    public List<User> listAll() {
        ArrayList<User> users = new ArrayList<User>();
        try {
            reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String row = reader.nextLine();
                String[] splittedLine = row.split(" ");
                users.add(new User(splittedLine[0], splittedLine[1]));
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }

    @Override
    public User findByName(String name) {
//        System.out.println(file);
        try {
            reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String row = reader.nextLine();
                String[] splittedLine = row.split(" ");
                if (splittedLine[0].equals(name)) {
                    return new User(splittedLine[0], splittedLine[1]);
                }
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void add(User user) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(user.getUsername() + " " + user.getPassword());
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(FileUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
