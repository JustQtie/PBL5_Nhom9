
package com.example.navigationbottom.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.AddBookActivity;
import com.example.navigationbottom.activity.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SellFragment extends Fragment {

    private RecyclerView postsRecyclerView;
//    private List<ModelPost> postList;
//    private AdapterPosts adapterPosts;
    private FloatingActionButton btn_fab;
    private ProgressDialog progressDialog;

    private View mView;
    public SellFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_sell, container, false);

        initUI();

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setFragmentToolbar(view);

        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddBookActivity.class));
            }
        });
    }

    private void initUI() {

        btn_fab = mView.findViewById(R.id.btn_fab_Sell);
        postsRecyclerView = mView.findViewById(R.id.rv_sell_fragment);

        progressDialog = new ProgressDialog(getActivity());

    }


    private void setFragmentToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_Sell);
        ((MainActivity)requireActivity()).setSupportActionBar(toolbar);
    }

}