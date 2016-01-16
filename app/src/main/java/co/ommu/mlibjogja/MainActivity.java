package co.ommu.mlibjogja;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //SettingFunction setting = new SettingFunction();
    Spinner spinnerSearch;
    EditText fieldSearch;
    Button buttonSearch;
    int selected;
    String[] searching = {"title", "publisher", "author", "subject"};
    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setting.onCreateOptionsMenu(Menu this);
        //setting.onOptionsItemSelected(MenuItem this);

        spinnerSearch = (Spinner) findViewById(R.id.spinnerSearch);
        fieldSearch = (EditText) findViewById(R.id.fieldSearch);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(this);

        spinnerSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                // TODO Auto-generated method stub
                selected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
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

        if (id == R.id.buttonSearch) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getBaseContext(), ResultActivity.class);
            keyword = fieldSearch.getText().toString();
            if (keyword != null && keyword.length() > 0) {
                intent.putExtra("url", searching[selected] + "/" + keyword);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
                fieldSearch.getText().clear();
            } else
                Toast.makeText(getBaseContext(), "Pencarian tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
    }
}
