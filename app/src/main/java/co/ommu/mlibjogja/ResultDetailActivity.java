package co.ommu.mlibjogja;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import co.ommu.mlibjogja.components.AsynRestClient;
import co.ommu.mlibjogja.components.Utility;
import co.ommu.mlibjogja.models.ResultDetailModel;

public class ResultDetailActivity extends AppCompatActivity {

    ResultDetailModel item;
    ListView listResultDetail;
    TextView textEmpty;
    LinearLayout layoutDetail;
    ProgressDialog dialog;
    String url, location,
            cover, title, description, author, publisher, publish_year, paging, subject, isbn, call_number;
    static String id;
    //SettingFunction setting = new SettingFunction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);

        layoutDetail = (LinearLayout) findViewById(R.id.layoutDetail);
        layoutDetail.setVisibility(View.GONE);

        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getString("id");
            location = getIntent().getExtras().getString("loc");
            url = Utility.bookDetailPathURL + "/id/" + getIntent().getExtras().getString("id") + "/data/JSON";
            Log.i("url", url);
            getResult();
        }
    }

    private void getResult() {
        dialog = ProgressDialog.show(ResultDetailActivity.this, "", "Please wait...", true, true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                // TODO Auto-generated method stub
                AsynRestClient.cancelAllRequests(getApplicationContext());
                onBackPressed();
            }
        });
        AsynRestClient.get(getApplicationContext(), url, null, new JsonHttpResponseHandler() {
            //@Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                //super.onSuccess(response);
                try {
                    JSONObject jo = response.getJSONObject("detail");
                    item = new ResultDetailModel();
                    cover = item.cover = jo.getString("cover");
                    title = item.title = jo.getString("title");
                    description = item.description = jo.getString("description");
                    author = item.author = jo.getString("author");
                    publisher = item.publisher = jo.getString("publisher");
                    publish_year = item.publish_year = jo.getString("publish_year");
                    paging = item.paging = jo.getString("paging");
                    subject = item.subject = jo.getString("subject");
                    isbn = item.isbn = jo.getString("isbn");
                    call_number = item.call_number = jo.getString("call_number");
                    Log.i("publish_year", item.publish_year);
                    buildWidget();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Log.i("result", jo.toString());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    listResultDetail.setVisibility(View.GONE);
                    textEmpty.setVisibility(View.VISIBLE);
                }
            }

            //@Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
                // TODO Auto-generated method stub
                //super.onFailure(statusCode, headers, error, content);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void buildWidget() {
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        TextView tvDesc = (TextView) findViewById(R.id.tvDesc);
        TextView tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        TextView tvPublisher = (TextView) findViewById(R.id.tvPublisher);
        TextView tvYear = (TextView) findViewById(R.id.tvYear);
        TextView tvPage = (TextView) findViewById(R.id.tvPage);
        TextView tvSubject = (TextView) findViewById(R.id.tvSubject);
        TextView tvIsbn = (TextView) findViewById(R.id.tvIsbn);
        TextView tvCall = (TextView) findViewById(R.id.tvCall);
        tvTitle.setText(title);
        tvDesc.setText(description);
        tvAuthor.setText(author);
        tvPublisher.setText(publisher);
        tvYear.setText(publish_year);
        tvPage.setText(paging);
        tvSubject.setText(subject);
        tvIsbn.setText(isbn);
        tvCall.setText(call_number);
        layoutDetail.setVisibility(View.VISIBLE);
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
            startActivity(new Intent(getApplicationContext(), ResultDetailActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
