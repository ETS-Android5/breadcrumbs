package bristol.ac.uk.breadcrumbsspe.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import bristol.ac.uk.breadcrumbsspe.api.RetrofitClient;
import bristol.ac.uk.breadcrumbsspe.api.UserService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Long id;
    private String firstName;

    private String lastName;

    private String email;

    private int score;

    public User(Long id,String firstName,String lastName,String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email; //can use regex to pattern match
        this.score= 0;
    }

    public boolean equals(User u) {
        return (id.equals(u.getId()) || (u.getEmail().toLowerCase().equals(email)));
    }

    @Override
    public String toString() {
        return "id =" + id + " "
                + "First name: " + firstName
                + " Last name: " + lastName
                + " email: " + email
                + " score: " + score;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addToScore(int pointsScored) {
        score += pointsScored;
        UserService userService = RetrofitClient.retrofit.create(UserService.class);
        Call<ResponseBody> userCall = userService.update(id,this);
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // you could add some log statements here
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
