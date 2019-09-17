package quest.outdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {

    String youWon;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        youWon = "Congrats! here is your prize";

        textView = findViewById(R.id.textView);

        textView.setText(youWon);
    }
}
