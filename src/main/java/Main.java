import entity.PasswordEntity;
import entity.UserEntity;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String user, pass;
        while(true){
            System.out.println("Enter userName: ");
            user = scanner.nextLine();
            System.out.println("Enter password: ");
            pass = scanner.nextLine();
            Operations.login(user,pass);
        }
    }
}
