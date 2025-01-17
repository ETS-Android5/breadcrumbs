package com.spe.breadcrumbs.dao;

import com.spe.breadcrumbs.entity.Attempt;
import com.spe.breadcrumbs.entity.Choice;
import com.spe.breadcrumbs.entity.User;
import com.spe.breadcrumbs.web.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AttemptDbDAO implements AttemptDAO {
    private DBConnection dbConnection;

    public AttemptDbDAO(DBConnection dbConnection){
        this.dbConnection = dbConnection;
    }
    @Override
    public boolean addAttempt(Attempt a) {
        User u = a.getUser();
        Choice c = a.getChoice();
        try{
            Connection con = dbConnection.getConnection();
            String addAttempt = "INSERT INTO Attempt(userId,choiceId,attemptNo) VALUES (?,?,?)";
            PreparedStatement stmt = con.prepareStatement(addAttempt);
            stmt.setLong(1,u.getId());
            stmt.setLong(2,c.getChoiceId());
            int count = getCount(c.getQuestionId(),u.getId()) + 1;
            stmt.setInt(3,count);
            stmt.executeUpdate();
            return true;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //returns the no of choices in attempts array
    @Override
    public List<Choice> getAttempts(Long questionId, Long userId){
        List<Choice> choices = new ArrayList<>();
        try{
            Connection con = dbConnection.getConnection();
            String getAttempts = "SELECT * FROM Attempt INNER JOIN Choice ON Choice.id = Attempt.choiceId " +
                    "WHERE Choice.question = ? AND Attempt.userId = ?";
            PreparedStatement stmt = con.prepareStatement(getAttempts);
            stmt.setLong(1,questionId);
            stmt.setLong(2,userId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Choice c = new Choice(rs.getLong("question"),rs.getString("choiceText")
                        ,rs.getBoolean("answer"));
                choices.add(c);
            }
            return choices;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return choices;
    }

    private int getCount(Long questionId, Long userId){
        List<Choice> choices = getAttempts(questionId,userId);
        return choices.size();
    }
}
