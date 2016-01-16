package co.ommu.mlibjogja;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdvanceSearchActivity extends AppCompatActivity implements View.OnClickListener {

    EditText fieldTitle, fieldAuthor, fieldPublisher, fieldSubject;
    Button buttonAdvanceSearch;
    String title = "", author = "", publisher = "", subject = "";
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_search);

        fieldTitle = (EditText) findViewById(R.id.fieldTitle);
        fieldAuthor = (EditText) findViewById(R.id.fieldAuthor);
        fieldPublisher = (EditText) findViewById(R.id.fieldPublisher);
        fieldSubject = (EditText) findViewById(R.id.fieldSubject);
        buttonAdvanceSearch = (Button) findViewById(R.id.buttonAdvanceSearch);
        buttonAdvanceSearch.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.actionSimple) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else if (id == R.id.actionAdvance) {
            startActivity(new Intent(getApplicationContext(), AdvanceSearchActivity.class));
        } else if (id == R.id.actionSetting) {
            startActivity(new Intent(getApplicationContext(), SettingActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();

        if (id == R.id.buttonAdvanceSearch) {
            // TODO Auto-generated method stub
            getKeywords();
            Intent intent = new Intent(getBaseContext(), ResultActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("keyword", "Hasil Pencarian");
            startActivity(intent);
            fieldTitle.getText().clear();
            fieldAuthor.getText().clear();
            fieldPublisher.getText().clear();
            fieldSubject.getText().clear();
        }
    }

    private void getKeywords() {
        if (fieldTitle.getText().length() > 0) {
            title = fieldTitle.getText().toString();
        }
        if (fieldAuthor.getText().length() > 0) {
            author = fieldAuthor.getText().toString();
        }
        if (fieldPublisher.getText().length() > 0) {
            publisher = fieldPublisher.getText().toString();
        }
        if (fieldSubject.getText().length() > 0) {
            subject = fieldSubject.getText().toString();
        }

        url = "title/" + title + "/author/" + author + "/publisher/"
                + publisher + "/subject/" + subject;

    }
}
