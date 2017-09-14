package br.com.hrick.estoquepessoal.view.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.hrick.estoquepessoal.R;
import br.com.hrick.estoquepessoal.entity.Stock;
import br.com.hrick.estoquepessoal.view.main.fragments.dummy.DummyContent;
import br.com.hrick.estoquepessoal.view.main.fragments.dummy.DummyContent.DummyItem;

import java.util.List;


public class StockFragment extends Fragment {


    private OnListFragmentInteractionListener mListener;


    public StockFragment() {
    }

    @SuppressWarnings("unused")
    public static StockFragment newInstance(int columnCount) {
        return new StockFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_list, container, false);
        val view = inflater!!.inflate(R.layout.fragment_os_finalizadas_list, container, false)
        val context = view?.context
        view.rvOrdens.layoutManager = LinearLayoutManager(context)
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new StockRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Stock item);
    }
}
