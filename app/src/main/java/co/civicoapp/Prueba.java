package co.civicoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class Prueba extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        recyclerView  = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        Double USER_LAT = 4.673731;
        Double USER_LNG = -74.058395;
        String TERM = "cines";

        MyViewModelFactory myViewModelFactory = new MyViewModelFactory(USER_LAT,USER_LNG,TERM);


        ItemViewModel itemViewModel = ViewModelProviders.of(this,myViewModelFactory).get(ItemViewModel.class);


        final ItemAdapter adapter = new ItemAdapter(this);

            itemViewModel.itemPagedList.observe(this, new Observer<PagedList<Result>>() {
                @Override
                public void onChanged(PagedList<Result> results) {
                    adapter.submitList(results);
                }
            });
            recyclerView.setAdapter(adapter);
    }
}
