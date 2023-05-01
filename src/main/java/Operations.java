import entity.AttemptsEntity;
import entity.PasswordEntity;
import entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.query.NativeQuery;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;

public class Operations {
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
    private static EntityManager manager = factory.createEntityManager();
    private static EntityTransaction transaction = manager.getTransaction();

    public static void insertUser(UserEntity u){
        try{
            transaction.begin();
            manager.persist(u);
            transaction.commit();
        }
        finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
            manager.close();
            factory.close();
        }
    }

    public static void insertPassword(PasswordEntity p){
        try{
            transaction.begin();
            manager.persist(p);
            transaction.commit();
        }
        finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
            manager.close();
            factory.close();
        }
    }

    public static void insertAttempt(AttemptsEntity a){
        try{
            transaction.begin();
            manager.persist(a);
            transaction.commit();
        }
        finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
        }
    }

    public static UserEntity getUserByName(String userName){
        try{
            transaction.begin();
            TypedQuery<UserEntity> getUser = manager.createNamedQuery("User.findByUserName",UserEntity.class);
            getUser.setParameter(1,userName);
            List<UserEntity> set = getUser.getResultList();
            transaction.commit();
            if(set.isEmpty()){
                System.out.println("User Name does not exist...");
                return null;
            }
            return set.get(0);

        }
        finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
        }
    }

    public static boolean isUserBlocked(UserEntity u){
        Timestamp current = new Timestamp(new Date().getTime());
        Timestamp usersLastBlock = u.getLastlyBlocked();
        if(usersLastBlock == null || current.getTime() - usersLastBlock.getTime() >= 15*60*1000){
            return false;
        }
        return true;
    }

    private static PasswordEntity getRecentPasswordOfUser(int id) {
        try{
            transaction.begin();
            TypedQuery<PasswordEntity> recent = manager.createNamedQuery("Password.mostRecent",PasswordEntity.class);
            recent.setParameter(1,id);
            PasswordEntity password = recent.getResultList().get(0);
            transaction.commit();
            return password;
        }
        finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
        }
    }

    public static void blockUser(int id){
        try{
            transaction.begin();
            UserEntity u = manager.find(UserEntity.class, id);
            u.setLastlyBlocked(new Timestamp(new Date().getTime()));
            manager.merge(u);
            transaction.commit();
        }
        finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
        }
    }
    public static void login(String userName, String password){
        UserEntity user = getUserByName(userName);
        if(user == null){
            return;
        }
        if(isUserBlocked(user)){
            System.out.println("User is blocked! try again later!");
            return;
        }

        AttemptsEntity attempt = new AttemptsEntity();
        attempt.setDate(new Timestamp(new Date().getTime()));
        attempt.setPasswordEntered(password);
        attempt.setUserId(user.getUserId());

        if(getRecentPasswordOfUser(user.getUserId()).getPasswordText().equals(password)){
            attempt.setAllowed(true);
            insertAttempt(attempt);
            System.out.println("login successfully");
            return;
        }
        else{
            System.out.println("Enterence was not allowed!");
            attempt.setAllowed(false);
            insertAttempt(attempt);
            TypedQuery<AttemptsEntity> attemptsEntityTypedQuery =
                    manager.createNamedQuery("threeLastEnteries",AttemptsEntity.class);
            attemptsEntityTypedQuery.setParameter(1,user.getUserId());
            attemptsEntityTypedQuery.setMaxResults(3);
            List<AttemptsEntity> attemptsEntityList = attemptsEntityTypedQuery.getResultList();
            if(attemptsEntityList.size()==3){
                Timestamp past = attemptsEntityList.get(2).getDate();
                Timestamp recent = attemptsEntityList.get(0).getDate();
                if(recent.getTime()-past.getTime() < 15*60*1000){
                    blockUser(user.getUserId());
                    System.out.println("User Blocked for 15 minutes");
                }
            }
        }
    }

}
