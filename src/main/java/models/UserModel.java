package models;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserModel {
    public String email;
    public String password;
    public String name;


    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

// добавлен setEmail чтобы убрать ошибку "Cannot resolve method 'setEmail' in 'UserModel'" В тесте testCreateUserWithoutEmailFailure
    public void setEmail(String email) {
        this.email = email;
    }

    public UserModel(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }


}





