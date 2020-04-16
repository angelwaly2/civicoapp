package co.civicoapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EnOfertas extends Fragment {


    Double USER_LAT;
    Double USER_LNG;
    String TERM;

    private RecyclerView recyclerView;
    Context c;

    public EnOfertas(Double user_lat,Double user_lng,String term,Context c)
    {
        this.c = c;
        this.USER_LAT = user_lat;
        this.USER_LNG = user_lng;
        this.TERM = term;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.modelo_ofertas,container,false);
        recyclerView  = (RecyclerView) view.findViewById(R.id.recyclerView);
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(c));
            recyclerView.setHasFixedSize(true);
        }catch (Exception e){
            e.printStackTrace();
        }

        MyViewModelFactory myViewModelFactory = new MyViewModelFactory(USER_LAT,USER_LNG,TERM);
        ItemViewModel itemViewModel = ViewModelProviders.of(this,myViewModelFactory).get(ItemViewModel.class);
        final ItemAdapter adapter = new ItemAdapter(c);
        itemViewModel.itemPagedList.observe(this, new Observer<PagedList<Result>>() {
            @Override
            public void onChanged(PagedList<Result> results) {
                adapter.submitList(results);
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

}
