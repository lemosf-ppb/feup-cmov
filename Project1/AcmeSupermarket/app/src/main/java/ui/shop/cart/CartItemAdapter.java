package ui.shop.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.acmesupermarket.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import models.TransactionItem;
import ui.shop.ShopViewModel;

public class CartItemAdapter extends ArrayAdapter<TransactionItem> {

    private Context context;
    private ArrayList<TransactionItem> transactionItems;
    private ShopViewModel mViewModel;

    public CartItemAdapter(Context context, ArrayList<TransactionItem> transactionItems, ShopViewModel mViewModel) {
        super(context, 0, transactionItems);
        this.context = context;
        this.transactionItems = transactionItems;
        this.mViewModel = mViewModel;
    }

    public void setTransactionItems(ArrayList<TransactionItem> transactionItems) {
        this.transactionItems = transactionItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_row, parent, false);
        }

        TransactionItem transactionItem = transactionItems.get(position);
        ((TextView) convertView.findViewById(R.id.itemTitle)).setText(transactionItem.getName());
        ((TextView) convertView.findViewById(R.id.priceUnit)).setText(String.format("%s€", transactionItem.getPrice()));
        ((TextView) convertView.findViewById(R.id.quantityItem)).setText(String.valueOf(transactionItem.getQuantity()));

        convertView.findViewById(R.id.increase_btn).setOnClickListener(view -> {
            if (mViewModel.isCartFull()) {
                Snackbar.make(view,
                        "Your cart is already full!",
                        Snackbar.LENGTH_SHORT
                ).show();
            } else {
                mViewModel.addTransactionItem(transactionItem);
            }
        });

        convertView.findViewById(R.id.decrease_btn).setOnClickListener(view -> {
            if (transactionItem.getQuantity() == 1) {
                Snackbar.make(view,
                        "You only have one item of that type left!",
                        Snackbar.LENGTH_SHORT
                ).show();
            } else {
                mViewModel.decreaseTransactionItem(transactionItem);
            }
        });

        convertView.findViewById(R.id.remove_btn).setOnClickListener(view -> {
            mViewModel.removeTransactionItem(transactionItem);
        });

        return convertView;
    }
}