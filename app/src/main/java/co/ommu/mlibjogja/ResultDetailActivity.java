package co.ommu.mlibjogja;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.ommu.mlibjogja.components.AsynRestClient;
import co.ommu.mlibjogja.components.Utility;
import co.ommu.mlibjogja.models.ResultDetailModel;
import co.ommu.mlibjogja.models.ResultLocationModel;
import co.ommu.mlibjogja.views.ResultLocationView;

public class ResultDetailActivity extends AppCompatActivity {

    public boolean loadingMore = false, firstTimeLoad = true;
    public ArrayList<ResultLocationModel> array = new ArrayList<ResultLocationModel>();
    ResultDetailModel item;
    ListView listResultDetail;
    TextView textEmpty;
    View footerView;
    LinearLayout layoutDetail;
    ProgressDialog dialog;
    ResultLocationView adap;
    String url, itemCount = "0", pageSize = "0", nextPage = "", location,
            cover, title, description, author, publisher, publish_year, paging, subject, isbn, call_number;
    static String id;
    int offset;
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

        footerView = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.content_footer, null, false);
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

                    JSONObject jo1 = response.getJSONObject("location");
                    JSONArray ja = jo1.getJSONArray("data");
                    for (int i = 0; i < ja.length(); i++) {
                        ResultLocationModel data = new ResultLocationModel();
                        data.name = ja.getJSONObject(i).getString("name");
                        data.address = ja.getJSONObject(i).getString("address");
                        data.book = ja.getJSONObject(i).getString("book");
                        data.point = ja.getJSONObject(i).getString("point");
                        array.add(data);
                    }

                    JSONObject jso = jo1.getJSONObject("pager");
                    itemCount = jso.getString("itemCount");
                    nextPage = jso.getString("nextPage");
                    pageSize = jso.getString("pageSize");
                    url = jo1.getString("nextpage");

                    Log.i("url2", url);
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
                    //listResultDetail.setVisibility(View.GONE);
                    //textEmpty.setVisibility(View.VISIBLE);
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
        textEmpty = (TextView) findViewById(R.id.textEmpty);
        listResultDetail = (ListView) findViewById(R.id.listResultDetail);

        if (firstTimeLoad) {
            if (Integer.parseInt(itemCount) > 20) {
                listResultDetail.addFooterView(footerView);
                loadMoreData();
            }
            adap = new ResultLocationView(array, getApplicationContext());
            listResultDetail.setAdapter(adap);

        } else {
            adap.notifyDataSetChanged();
        }

        firstTimeLoad = false;
        loadingMore = false;
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

    public void loadMoreData() {
        // TODO Auto-generated method stub

        listResultDetail.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        listResultDetail.removeFooterView(footerView);
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
