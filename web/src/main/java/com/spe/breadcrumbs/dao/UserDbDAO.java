package com.spe.breadcrumbs.dao;

import com.spe.breadcrumbs.entity.Question;
import com.spe.breadcrumbs.entity.Quiz;
import com.spe.breadcrumbs.entity.User;
import com.spe.breadcrumbs.web.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.spe.breadcrumbs.web.DBConnection.getConnection;

public class UserDbDAO implements UserDAO {

    @Override
    public List<User> getAllUsers(){
      //  if(!userCache.isEmpty()) return userCache;
        List<User> users = new ArrayList<>();
        Connection con = getConnection();
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM User");
            while (rs.next()){
                User u = new User(rs.getLong("id"),rs.getString("firstName"),
                        rs.getString("lastName"),rs.getString("email"),
                        rs.getString("code"),rs.getInt("score"));
                users.add(u);
            }
            con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User getUser(Long id) {
        Connection con = getConnection();
        User u;
        try{
            String getUser = "SELECT * FROM User WHERE id = ?";
            PreparedStatement stmt = con.prepareStatement(getUser);
            stmt.setInt(1,Math.toIntExact(id));
            ResultSet rs = stmt.executeQuery();
           if(rs.next()) { //move it to the first row
               u = new User(rs.getLong("id"), rs.getString("firstName"),
                       rs.getString("lastName"), rs.getString("email"),
                       rs.getString("code"),rs.getInt("score"));
               con.close();
               return u;
           }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserWithQuiz(Long id) {
        User u = null;
        Quiz quiz;
        List<Question> questions = new ArrayList<>();
        Connection con = getConnection();
        try{
            String getUser = "SELECT User.id as userId," +
                    "User.firstName as firstName," +
                    "User.lastName as lastName," +
                    "User.email as email," +
                    "User.code as code," +
                    "User.score as score," +
                    "Quiz.quizId as quizId," +
                    "Quiz.title as title," +
                    "Question.id as questionId," +
                    "Question.question as question " +
                    "FROM User INNER JOIN Question " +
                    "INNER JOIN Quiz ON User.quizId = Question.quizId " +
                    "WHERE User.id = ?;";
            PreparedStatement stmt = con.prepareStatement(getUser);
            stmt.setLong(1,id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                u = new User(rs.getLong("userId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("code"),
                        rs.getInt("score"));
                quiz = new Quiz(rs.getInt("quizId"),rs.getString("title"));
                Question q = new Question(rs.getLong("questionId"),rs.getString("question"));
                questions.add(q);
                quiz.setQuestions(questions);
                u.setQuiz(quiz);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public Quiz getQuiz(Long id) {
        Quiz q = null;
        try{
            Connection con = getConnection();
            String getQuiz = "SELECT quizId FROM User WHERE id = ?";
            PreparedStatement stmt = con.prepareStatement(getQuiz);
            stmt.setLong(1,id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                int quizId = rs.getInt("quizId");
                QuizDAO quizDAO = new QuizDbDAO();
                q = quizDAO.getQuiz(quizId);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return q;
    }

    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public User getByCode(String code) {
        User u = null;
        try{
            Connection con = getConnection();
            String getUser = "SELECT * FROM User WHERE code = ?";
            PreparedStatement stmt = con.prepareStatement(getUser);
            stmt.setString(1,code);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                u = new User(rs.getLong("id"),rs.getString("firstName"),
                        rs.getString("lastName"),rs.getString("email"),
                        rs.getString("code"),rs.getInt("score"));
                con.close();
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public boolean addUser(User u) {
        try{
            Connection con = getConnection();
            String addUser = "INSERT INTO User(firstName,lastName,email) VALUES(?,?,?)";
            PreparedStatement stmt = con.prepareStatement(addUser);
            stmt.setString(1,u.getFirstName());
            stmt.setString(2,u.getLastName());
            stmt.setString(3,u.getEmail());
            stmt.executeUpdate();
            con.close();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Long id, User u) {
        return false;
    }

    @Override
    public boolean deleteUser(Long id) {
        try{
            Connection con = getConnection();
            String deleteUser = "DELETE FROM User Where id = ?";
            PreparedStatement stmt = con.prepareStatement(deleteUser);
            stmt.setLong(1,id);
            stmt.executeUpdate();
            con.close();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> search(String s) {
        return null;
    }
}
