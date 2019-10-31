package ui.transactions;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.acmesupermarket.R;

import java.util.ArrayList;

import models.Transaction;
import models.TransactionItem;

public class TransactionAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Transaction> transactions;
    private Resources res;

    public TransactionAdapter(Context context, ArrayList<Transaction> transactions, Resources res) {
        this.context = context;
        this.transactions = transactions;
        this.res = res;
    }

    void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<TransactionItem> transactionItems = (transactions.get(groupPosition)).getTransactionItems();
        return transactionItems.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        TransactionItem transactionItem = (TransactionItem) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.child_items, null);
        }

        String item_title = res.getString(R.string.items_row, transactionItem.getId().toString().substring(0, 13), transactionItem.getQuantity());
        ((TextView) view.findViewById(R.id.cart_item_details)).setText(item_title.trim());

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<TransactionItem> transactionItems = transactions.get(groupPosition).getTransactionItems();
        return transactionItems.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return transactions.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return transactions.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        Transaction transaction = (Transaction) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.group_items, null);
        }


        TextView heading = view.findViewById(R.id.transaction_id);
        heading.setText(transaction.getId().trim());

        String usedDiscounts = res.getString(R.string.used_discounts, transaction.isUseDiscounts());
        ((TextView) view.findViewById(R.id.used_discounts)).setText(usedDiscounts);

        String totalPrice = res.getString(R.string.total_payed_price, transaction.getTotalPrice().toString());
        ((TextView) view.findViewById(R.id.transaction_price)).setText(totalPrice);

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
