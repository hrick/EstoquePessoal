package br.com.hrick.estoquepessoal.view.main.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.SQLException;
import java.util.ArrayList;

import br.com.hrick.estoquepessoal.R;
import br.com.hrick.estoquepessoal.entity.Stock;
import br.com.hrick.estoquepessoal.repository.StockRepository;
import br.com.hrick.estoquepessoal.view.main.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;


public class StockFragment extends Fragment {


    @BindView(R.id.rvStock)
    RecyclerView rvStock;
    StockRecyclerViewAdapter stockRecyclerViewAdapter;


    public StockFragment() {
    }

    public static StockFragment newInstance() {
        return new StockFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StockRepository.init(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_list, container, false);
        ButterKnife.bind(this, view);
        Context context = view.getContext();
        stockRecyclerViewAdapter = new StockRecyclerViewAdapter(new ArrayList<Stock>(), ((MainActivity) getActivity()), getActivity());
        rvStock.setLayoutManager(new LinearLayoutManager(context));
        rvStock.setHasFixedSize(true);
        rvStock.setAdapter(stockRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            stockRecyclerViewAdapter.updateListStocks(StockRepository.getInstance().getStocks());
        } catch (SQLException e) {
            ((MainActivity) getActivity()).showWarning(getString(R.string.erro_database));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    public void notifyList() {
        try {
            stockRecyclerViewAdapter.updateListStocks(StockRepository.getInstance().getStocks());
        } catch (SQLException e) {
            ((MainActivity) getActivity()).showWarning(getString(R.string.erro_database));
        }
        stockRecyclerViewAdapter.notifyDataSetChanged();
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Stock item);
        void onDeleteStockListener(Stock item);
        void onShareLocationStockListener(Stock item);

    }
}
