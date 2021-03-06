package com.example.daisy_mobile.ui.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.daisy_mobile.R;
import com.example.daisy_mobile.adapter.Orderitemadapter;
import com.example.daisy_mobile.adapter.TopkitchenViewpagerAdapter;
import com.example.daisy_mobile.databinding.FragmentOrderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import dataclass.kitchen;
import dataclass.order;

public class OrderFragment extends Fragment {
    private TabLayout tab;
    private OrderViewModel orderViewModel;
    private FragmentOrderBinding binding; private FirebaseFirestore db;
    private Orderitemadapter adapter0,adapter1,adapter2;
    private ListView lv1,lv2,lv3; private ArrayList<order> newlist,presentlist,pastlist;
    private static final String TAG = "MyActivity";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        lv1=view.findViewById(R.id.lv_neworder);
        lv2=view.findViewById(R.id.lv_presentorder);
        lv3=view.findViewById(R.id.lv_pastorder);
        db=FirebaseFirestore.getInstance(); String id= FirebaseAuth.getInstance().getUid();
        newlist = new ArrayList<order>();presentlist = new ArrayList<order>();pastlist = new ArrayList<order>();
        initdata(0);
        tab=view.findViewById(R.id.tablayout);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TabLayout   tabLayout = (TabLayout) view.findViewById(R.id.tablayout); // get the reference of TabLayout
                int selectedTabPosition = tabLayout.getSelectedTabPosition(); // get the position for the current selected tab
                switch(selectedTabPosition)
                {
                    case 0: {
                        initdata(0);
                        lv1.setVisibility(View.VISIBLE);lv2.setVisibility(View.GONE);lv3.setVisibility(View.GONE);
                        break;
                    }
                    case 1: {
                        initdata(1);
                        lv2.setVisibility(View.VISIBLE);lv1.setVisibility(View.GONE);lv3.setVisibility(View.GONE);
                        break;
                    }
                    case 2: {
                        initdata(2);
                        lv3.setVisibility(View.VISIBLE);lv2.setVisibility(View.GONE);lv1.setVisibility(View.GONE);
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                TabLayout   tabLayout = (TabLayout) view.findViewById(R.id.tablayout); // get the reference of TabLayout
                int selectedTabPosition = tabLayout.getSelectedTabPosition(); // get the position for the current selected tab
                switch(selectedTabPosition)
                {
                    case 0: {
                        initdata(0);
                        lv1.setVisibility(View.VISIBLE);lv2.setVisibility(View.GONE);lv3.setVisibility(View.GONE);
                        break;
                    }
                    case 1: {
                        initdata(1);
                        lv2.setVisibility(View.VISIBLE);lv1.setVisibility(View.GONE);lv3.setVisibility(View.GONE);
                        break;
                    }
                    case 2: {
                        initdata(2);
                        lv3.setVisibility(View.VISIBLE);lv2.setVisibility(View.GONE);lv1.setVisibility(View.GONE);
                        break;
                    }
                }
            }
        });

        //goi data cho ba listview order
        return view;
    }

    private void initdata(int value)
    {
        newlist.clear();presentlist.clear();pastlist.clear();

        String id=FirebaseAuth.getInstance().getUid();
        db.collection("order")
                .whereEqualTo("user_id",id)

                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d(TAG, document.getId() + " => " + document.getData());
                                order abc=document.toObject(order.class);
                                if (abc.getStatus()==value)
                                {
                                    switch(value){
                                        case 0:newlist.add(abc); break;
                                        case 1:presentlist.add(abc); break;
                                        case 2:pastlist.add(abc); break;

                                    }
                                }

                            }

                        } else {
                              Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        switch(value){
                            case 0:
                            {
                                if (newlist!=null)
                                {

                                    adapter0=new Orderitemadapter(getContext(),newlist);
                                    lv1.setAdapter(adapter0); adapter0.notifyDataSetChanged();
                                }

                                break;

                            }
                            case 1:
                            {
                                if (presentlist!=null)
                                {
                                    adapter1=new Orderitemadapter(getContext(),presentlist);
                                    lv2.setAdapter(adapter1); adapter1.notifyDataSetChanged();
                                }
                                break;
                            }
                            case 2:
                            {
                                if (pastlist!=null)
                                {
                                    adapter2=new Orderitemadapter(getContext(),pastlist);
                                    lv3.setAdapter(adapter2); adapter2.notifyDataSetChanged();
                                }
                                break;
                            }
                        }
                    }

                });

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}