package br.com.hrick.estoquepessoal.view.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.com.hrick.estoquepessoal.R;
import br.com.hrick.estoquepessoal.entity.Product;
import br.com.hrick.estoquepessoal.view.OnListProductClickListener;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

    private  List<Product> mValues;
    private Context context;
    private final OnListProductClickListener mListener;

    public ProductRecyclerViewAdapter(List<Product> items, OnListProductClickListener listener, Context context) {
        this.mValues = items;
        this.mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_stock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Product mItem = mValues.get(position);
        holder.tvProductName.setText(mValues.get(position).getName());
        holder.tvQuantity.setText(context.getString(R.string.title_quantity, Integer.toString(mValues.get(position).getQuantity())));
        holder.llEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListProductClickListener(mItem);
                }
            }
        });
        holder.btCallToResponsible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListCallResponsibleClickListener(mItem);
                }
            }
        });
        holder.llEditProduct.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onDeleteProductListener(mItem);
                }
                return false;
            }
        });
    }

    public void updateListProducts(List<Product> products){
        this.mValues = products;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llEditProduct)
        LinearLayout llEditProduct;
        @BindView(R.id.btCallToResponsible)
        Button btCallToResponsible;
        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvQuantity)
        TextView tvQuantity;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
