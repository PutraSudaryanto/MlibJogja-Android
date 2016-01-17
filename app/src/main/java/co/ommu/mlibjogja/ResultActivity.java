package co.ommu.mlibjogja;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.ommu.mlibjogja.components.AsynRestClient;
import co.ommu.mlibjogja.components.Utility;
import co.ommu.mlibjogja.models.ResultModel;
import co.ommu.mlibjogja.views.ResultView;

public class ResultActivity extends AppCompatActivity {

    public ArrayList<ResultModel> array = new ArrayList<ResultModel>();
    public boolean loadingMore = false, firstTimeLoad = true;
    ListView listResult;
    TextView textEmpty;
    View footerView;
    ProgressDialog dialog;
    ResultView adap;
    String url, itemCount = "0", pageSize = "0", nextPage = "", keyword;
    int offset;
    //SettingFunction setting = new SettingFunction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if (getIntent().getExtras() != null) {
            keyword = getIntent().getExtras().getString("keyword");
            url = Utility.bookSearchURL + "/keyword/" + getIntent().getExtras().getString("url") + "/data/JSON";
            Log.i("url", url);
            getResult();
        }

        footerView = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.content_footer, null, false);
    }

    private void buildWidget() {
        textEmpty = (TextView) findViewById(R.id.textEmpty);
        listResult = (ListView) findViewById(R.id.listResult);

        if (firstTimeLoad) {
            if (Integer.parseInt(itemCount) > 20) {
                listResult.addFooterView(footerView);
                loadMoreData();
            }
            adap = new ResultView(array, getApplicationContext());
            listResult.setAdapter(adap);

        } else {
            adap.notifyDataSetChanged();
        }

        firstTimeLoad = false;

        /*
        listResult.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                startActivity(new Intent(getApplicationContext(),
                    DetailViewActivity.class).putExtra("id",
                    array.get(arg2).id).putExtra("loc",
                    array.get(arg2).location));
            }
        });
        */

        loadingMore = false;
    }

    public void loadMoreData() {
        // TODO Auto-generated method stub

        listResult.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen != 0 && totalItemCount != 0) && (lastInScreen == totalItemCount) && !(loadingMore)) {
                    offset += Integer.parseInt(pageSize);
                    if (offset < Integer.parseInt(itemCount)) {
                        loadingMore = true;
                        getResult();

                    } else {
                        if (offset > Integer.parseInt(itemCount) || offset == Integer.parseInt(itemCount))
                            removeFooter();
                    }
                }
            }
        });
    }

    public void removeFooter() {
        // TODO Auto-generated method stub
        loadingMore = true;
        listResult.removeFooterView(footerView);
    }

    private void getResult() {
        if (firstTimeLoad) {
            dialog = ProgressDialog.show(ResultActivity.this, "", "Please wait...", true, true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    // TODO Auto-generated method stub
                    onBackPressed();
                    AsynRestClient.cancelAllRequests(getApplicationContext());
                }
            });
        }

        AsynRestClient.get(getApplicationContext(), url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(response);
                try {
                    JSONArray ja = response.getJSONArray("data");
                    for (int i = 0; i < ja.length(); i++) {
                        ResultModel item = new ResultModel();
                        item.id = ja.getJSONObject(i).getString("id");
                        item.title = ja.getJSONObject(i).getString("title");
                        item.author = ja.getJSONObject(i).getString("author");
                        item.publisher = ja.getJSONObject(i).getString("publisher");
                        item.subject = ja.getJSONObject(i).getString("subject");
                        item.location = ja.getJSONObject(i).getString("location");
                        array.add(item);
                    }

                    JSONObject jso = response.getJSONObject("pager");
                    itemCount = jso.getString("itemCount");
                    nextPage = jso.getString("nextPage");
                    pageSize = jso.getString("pageSize");
                    url = "http://mlib.bpadjogja.info" + response.getString("nextpage");
                    Log.i("url2", url);
                    buildWidget();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Log.i("result", ja.toString());

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    buildWidget();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    listResult.setVisibility(View.GONE);
                    textEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, PreferenceActivity.Header[] headers, Throwable error, String content) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, error, content);
                if (firstTimeLoad) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loadingMore = false;
                }
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
}
