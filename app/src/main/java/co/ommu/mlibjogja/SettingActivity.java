package co.ommu.mlibjogja;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class SettingActivity extends AppCompatActivity {

    //SettingFunction setting = new SettingFunction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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
}
