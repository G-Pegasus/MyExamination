package com.example.myexamination.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myexamination.AddPlanetActivity;
import com.example.myexamination.MyApplication;
import com.example.myexamination.R;
import com.example.myexamination.adapter.PlanetAdapter2;
import com.example.myexamination.adapter.PlanetLightedAdapter;
import com.example.myexamination.entity.DaoSession;
import com.example.myexamination.entity.Planets;
import com.example.myexamination.entity.PlanetsDao;

import java.util.List;

public class FragmentB extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSRLayout;
    private PlanetAdapter2 mAdapter;

    private final DaoSession daoSession = MyApplication.daoSession;
    private final PlanetsDao mPlanetDao = daoSession.getPlanetsDao();

    List<Planets> mList = mPlanetDao.queryBuilder().where(PlanetsDao.Properties.IsLighted.eq("false")).list();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_b, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = requireView().findViewById(R.id.recyclerView_planet);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyApplication.getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mSRLayout = requireActivity().findViewById(R.id.swipe_refresh_b);
        mSRLayout.setOnRefreshListener(this);
        mSRLayout.setColorSchemeResources(R.color.purple_200, R.color.purple_500, R.color.purple_700);

        mAdapter = new PlanetAdapter2(requireContext(), mList);
        mAdapter.setOnItemClickListener(new PlanetAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(int position, int arg, List<Planets> dataSource) {
                if (arg == 1) {
                    startActivity(new Intent(requireActivity(), AddPlanetActivity.class));
                } else {
                    Intent intent;
                    intent = new Intent(requireActivity(), PlanetInfoActivity_2.class);
                    intent.putExtra("planetId", dataSource.get(position).getId());
                    startActivity(intent);
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onRefresh() {
        mHandler.postDelayed(mRefresh, 1500);
    }

    private final Handler mHandler = new Handler();
    private final Runnable mRefresh = new Runnable() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            mSRLayout.setRefreshing(false);
            List<Planets> mList1 = mPlanetDao.queryBuilder().where(PlanetsDao.Properties.IsLighted.eq("false")).list();
            mAdapter = new PlanetAdapter2(requireContext(), mList1);
            mAdapter.setOnItemClickListener((position, arg, dataSource) -> {
                if (arg == 1) {
                    startActivity(new Intent(requireActivity(), AddPlanetActivity.class));
                } else {
                    Intent intent;
                    intent = new Intent(requireActivity(), PlanetInfoActivity_2.class);
                    intent.putExtra("planetId", dataSource.get(position).getId());
                    startActivity(intent);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    };
}
