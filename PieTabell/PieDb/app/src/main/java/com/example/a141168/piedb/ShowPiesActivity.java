package com.example.a141168.piedb;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by 141168 on 02.03.2017.
 */

public class ShowPiesActivity extends Activity {
    ListView listView;
    PieDbAdapter pieDbAdapter;
    Cursor cursor;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pies);

        listView = (ListView) findViewById(R.id.pieListView);

        // Bygg nytt databaseadapter og les alle data ved hjelp av cursor
        pieDbAdapter = new PieDbAdapter(this);
        pieDbAdapter.open();
        cursor = pieDbAdapter.getPies();


        // Bygg et SimpleCursorAdapter basert på cursoren
        // Versjon 1 fra læreboka
        /*
        adapter = new SimpleCursorAdapter(this,
                          android.R.layout.simple_list_item_1,
                          cursor, //Cursor
                          new String[] {PieDbAdapter.NAME},
                          new int[] { android.R.id.text1 }, 0);
         */

        // J.K. Versjon 2 med tre felt fra Pie-objektet
        adapter = new SimpleCursorAdapter(this,
                R.layout.pie_view_item,
                cursor, //Cursor
                new String[] {PieDbAdapter.NAME, PieDbAdapter.DESCRIPTION, PieDbAdapter.PRICE},
                new int[] { R.id.textViewName, R.id.textViewDescription, R.id.textViewPrice },
                0);
        // Koble SimpleCursorAdapter-objektet til ListView-objektet
        listView.setAdapter(adapter);
    }
    @Override
    public void onDestroy(){
        // Lukk cursoren og databaseadapteret når aktiviteten avsluttes
        if (cursor!=null){
            cursor.close();
        }
        if (pieDbAdapter!=null){
            pieDbAdapter.close();
        }
        super.onDestroy();
    }
}
