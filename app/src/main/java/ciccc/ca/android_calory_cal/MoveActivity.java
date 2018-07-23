package ciccc.ca.android_calory_cal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MoveActivity extends AppCompatActivity {
    SearchView searchView;
    ListView listView;
    ArrayAdapter<Move> adapter;
    ArrayAdapter<Move> adapter2;
    TextView eat_item;
    InputStream inputStream;
    BufferedReader bufferedReader;
    ArrayList<String> MoveArray;
    ArrayList<Move> list;
    ArrayList<Move> list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat);

        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.listView);


        // 1. read text file
        inputStream = getResources().openRawResource(R.raw.food_calories);
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        MoveArray = new ArrayList<>();
        list = new ArrayList<>();
        list2 = new ArrayList<>();

        try {
            String line;
            while (true) {
                line = (bufferedReader.readLine());
                if (line == null)
                    break;
                MoveArray.add(line);

            }
            for (int i = 1; i < MoveArray.size()/4; i++) {
                list.add(new Move(MoveArray.get(4*i), MoveArray.get(2+4*i)));
                list2.add(new Move(MoveArray.get(4*i), MoveArray.get(2+4*i)));
            }

            bufferedReader.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // listView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list2);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(list.contains(query)){
                    adapter.getFilter().filter(query);

                }else{
                    Toast.makeText(MoveActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        //short click
        final Intent intent = new Intent(this, CalculateEatCalories.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // list - everything
                // after filter - refresh list or create another list with searched items.
                System.out.println("position : " + position);
                System.out.println("id : " + id);
                Move eat = list.get(position);

                intent.putExtra("food", eat.getExercises());
                intent.putExtra("calories", eat.getCalories());
                startActivity(intent);


            }
        });

        // tyep eat_gram -> calculate calories
}}