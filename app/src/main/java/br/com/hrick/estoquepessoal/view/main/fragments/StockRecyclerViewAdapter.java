package br.com.hrick.estoquepessoal.view.main.fragments;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.hrick.estoquepessoal.R;
import br.com.hrick.estoquepessoal.entity.Stock;
import br.com.hrick.estoquepessoal.view.main.fragments.StockFragment.OnListFragmentInteractionListener;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.io.File;
import java.util.List;


public class StockRecyclerViewAdapter extends RecyclerView.Adapter<StockRecyclerViewAdapter.ViewHolder> {

    private List<Stock> mValues;
    private Context context;
    private final OnListFragmentInteractionListener mListener;

    public StockRecyclerViewAdapter(List<Stock> items, OnListFragmentInteractionListener listener, Context context) {
        this.mValues = items;
        this.mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_stock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Stock mItem = mValues.get(position);
        holder.tvStockName.setText(mValues.get(position).getName());
        if (mValues.get(position).getPathPicture() != null)
            Picasso.with(context).load(new File(mValues.get(position).getPathPicture()))
                    .fit().centerCrop()
                    .placeholder(R.drawable.stock_place_holder)
                    .error(R.drawable.stock_place_holder)
                    .into(holder.ivStock);

        holder.cardViewStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(mItem);
                }
            }
        });
        holder.cardViewStock.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onDeleteStockListener(mItem);
                }
                return false;
            }
        });
        holder.ibShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onShareLocationStockListener(mItem);
                }
            }
        });
    }

    public void updateListStocks(List<Stock> stocks) {
        this.mValues = stocks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivStock)
        ImageView ivStock;
        @BindView(R.id.tvStockName)
        TextView tvStockName;
        @BindView(R.id.ibShare)
        ImageButton ibShare;
        @BindView(R.id.cardViewStock)
        CardView cardViewStock;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
