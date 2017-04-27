package no.hit.jon.pietabell;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewHolderActivity extends Activity {
    ListView pieListView;
    ArrayList<Pie> pies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pieListView = (ListView) findViewById(R.id.list_view);
        pies = makePies();
        PieAdapter adapter = new PieAdapter(this, pies);
        pieListView.setAdapter(adapter);

        pieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        pies.get(position).mName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static ArrayList<Pie> makePies(){
        ArrayList<Pie> pies = new ArrayList<Pie>();
        pies.add(new Pie("Apple","An old-fashioned favorite. ", 1.5));
        pies.add(new Pie("Blueberry","Made with fresh Maine blueberries.", 1.5));
        pies.add(new Pie("Cherry","Delicious and fresh made daily.", 2.0));
        pies.add(new Pie("Coconut Cream","A customer favorite.", 2.5));
        return pies;
    }
}
