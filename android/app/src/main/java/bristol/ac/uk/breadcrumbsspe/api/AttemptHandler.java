package bristol.ac.uk.breadcrumbsspe.api;

import android.content.Intent;
import android.widget.Button;

import bristol.ac.uk.breadcrumbsspe.HomeActivity;
import bristol.ac.uk.breadcrumbsspe.QuestionActivity;
import bristol.ac.uk.breadcrumbsspe.R;
import bristol.ac.uk.breadcrumbsspe.entity.Question;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.rgb;

public class AttemptHandler implements Callback<ResponseBody> {
    private QuestionActivity questionActivity;
    private Button button;
    private Question question;

    public AttemptHandler(QuestionActivity questionActivity, Button button, Question question){
        this.questionActivity = questionActivity;
        this.button = button;
        this.question = question;
    }

    public void setQuestionActivity(QuestionActivity questionActivity) {
        this.questionActivity = questionActivity;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
       if(response.isSuccessful()){
           if (questionActivity.getButtons().indexOf(button) == questionActivity.getAnswer()) {
               question.correctAttemptMade(true);
               button.setBackgroundColor(rgb(0, 191, 0));
               Intent nextQ = new Intent(questionActivity, HomeActivity.class);
               nextQ.putExtra("CURRENT_QUESTION", question.getId().intValue());
               questionActivity.startActivity(nextQ);
               questionActivity.overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
               questionActivity.finish();
           } else {
               question.correctAttemptMade(false);
               button.setBackgroundColor(rgb(191, 0, 0));
               button.setEnabled(false);
           }
       }else{
           System.out.println(response.code());
       }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        t.printStackTrace();
    }

}
