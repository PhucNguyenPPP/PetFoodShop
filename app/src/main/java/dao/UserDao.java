package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petshopproject.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE userId = :userId")
    User getUserById(String userId);

    @Query("SELECT * FROM user WHERE userName = :userName")
    User getUserByUserName(String userName);

    @Insert
    void insert(User user);

    @Update
    void update (User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void deleteAllUsers();

    @Query("SELECT COUNT(*) > 0 FROM User WHERE userName = :username")
    boolean userExist (String username);

    @Query("SELECT COUNT(*) > 0 FROM User WHERE phone = :phone")
    boolean phoneExist (String phone);

    @Query("SELECT COUNT(*) > 0 FROM User WHERE email = :email")
    boolean emailExist (String email);

    @Query("SELECT COUNT(*) > 0 FROM User WHERE userName = :username AND password = :password")
    boolean checkLogin (String username, String password);
}
