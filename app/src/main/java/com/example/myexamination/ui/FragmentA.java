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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myexamination.MyApplication;
import com.example.myexamination.R;
import com.example.myexamination.adapter.PlanetLightedAdapter;
import com.example.myexamination.entity.DaoSession;
import com.example.myexamination.entity.Planets;
import com.example.myexamination.entity.PlanetsDao;

import java.util.List;

public class FragmentA extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSRLayout;
    private PlanetLightedAdapter mAdapter;

    private final DaoSession daoSession = MyApplication.daoSession;
    private final PlanetsDao mPlanetDao = daoSession.getPlanetsDao();

    private List<Planets> mList = mPlanetDao.queryBuilder().where(PlanetsDao.Properties.IsLighted.eq("true")).list();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_a, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = requireView().findViewById(R.id.universe_rv);
        StaggeredGridLayoutManager sgManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sgManager);

        mSRLayout = requireActivity().findViewById(R.id.swipe_refresh_a);
        mSRLayout.setOnRefreshListener(this);
        mSRLayout.setColorSchemeResources(R.color.purple_200, R.color.purple_500, R.color.purple_700);

        mAdapter = new PlanetLightedAdapter(requireContext(), mList);
        mAdapter.setOnItemClickListener((position, dataSource) -> {
            long id = mPlanetDao.loadByRowId(dataSource.get(position).getId()).getId();
            Intent intent = new Intent(requireActivity(), LightedInfoActivity.class);
            intent.putExtra("planetId", id);
            startActivity(intent);
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

            List<Planets> mList1 = mPlanetDao.queryBuilder().where(PlanetsDao.Properties.IsLighted.eq("true")).list();
            mAdapter = new PlanetLightedAdapter(requireContext(), mList1);
            mAdapter.setOnItemClickListener((position, dataSource) -> {
                long id = mPlanetDao.loadByRowId(dataSource.get(position).getId()).getId();
                Intent intent = new Intent(requireActivity(), LightedInfoActivity.class);
                intent.putExtra("planetId", id);
                startActivity(intent);
            });
            mRecyclerView.setAdapter(mAdapter);

        }
    };
}
