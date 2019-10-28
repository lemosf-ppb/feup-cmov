package ui.shop.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.acmesupermarket.R;

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

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_row, parent, false);
        }

        TransactionItem transactionItem = transactionItems.get(position);
        ((TextView) convertView.findViewById(R.id.itemTitle)).setText(transactionItem.getTitle());
        ((TextView) convertView.findViewById(R.id.priceUnit)).setText(transactionItem.getPrice() + "");
        ((TextView) convertView.findViewById(R.id.quantityItem)).setText(transactionItem.getQuantity() + "");

        convertView.findViewById(R.id.increase_btn).setOnClickListener(view -> {
            if (mViewModel.isCartFull()) {
                Toast.makeText(getContext(), "Your cart is already full!", Toast.LENGTH_LONG).show();
            } else {
                mViewModel.addTransactionItem(transactionItem);
            }
        });

        convertView.findViewById(R.id.decrease_btn).setOnClickListener(view -> {
            if (transactionItem.getQuantity() == 1) {
                Toast.makeText(getContext(), "You only have one transactionItem of that type left!", Toast.LENGTH_LONG).show();
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