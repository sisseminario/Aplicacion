package com.example.jessica.laboratorio;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.example.jessica.laboratorio.ItemMenu.ItemMenuStructure;
import com.example.jessica.laboratorio.ItemMenu.MenuBaseAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.JsonStreamerEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

   private ListView LISTAS;
   private ArrayList<ItemMenuStructure> LISTASINFO;
   private MenuBaseAdapter ADAPTER;
   private Context root;

   @Override
    protected void onCreate(Bundle savedInstanceState) {

       StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       StrictMode.setThreadPolicy(policy);
        root = this;
        LISTASINFO = new ArrayList<ItemMenuStructure>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        loadComponests();
        //loadData();
    }

    private void loadComponests() {
       LISTAS = (ListView) this.findViewById(R.id.foodlist);
       //LISTASINFO.add(new ItemMenuStructure("prueba","http://181.114.179.122:7070/api/v1.0/foodimg/5b115eb0c65ae20473506328",10,"prueba"));
        EditText search = (EditText)this.findViewById(R.id.editText);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str =s.toString();
                loadData(str);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        LISTAS.setAdapter(ADAPTER);
    }

    private void loadData(String keystr) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://181.114.179.122:7070/api/v1.0/food/" + keystr + "5b115eb0c65ae20473506328";
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try {
                    JSONArray listData = response.getJSONArray("info");
                    for (int i = 0; i < listData.length(); i++){
                        JSONObject obj = listData.getJSONObject(i);
                        String name = obj.getString("name");
                        String cantidad = obj.getString("quantity");

                        ItemMenuStructure item = new ItemMenuStructure(name, cantidad);
                        LISTASINFO.add(item);

                    }
                    ADAPTER = new MenuBaseAdapter(root,LISTASINFO);

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
