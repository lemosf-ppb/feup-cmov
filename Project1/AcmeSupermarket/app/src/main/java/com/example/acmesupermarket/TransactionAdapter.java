package com.example.acmesupermarket;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Transaction> transactions;
    private Resources res;

    public TransactionAdapter(Context context, ArrayList<Transaction> transactions, Resources res) {
        this.context = context;
        this.transactions = transactions;
        this.res = res;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Item> items = (transactions.get(groupPosition)).getItems();
        return items.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        Item item = (Item) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.child_items, null);
        }

        String item_title = res.getString(R.string.items_row, item.getTitle(), item.getQuantity());
        ((TextView) view.findViewById(R.id.cart_item_details)).setText(item_title.trim());

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Item> items = transactions.get(groupPosition).getItems();
        return items.size();
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


        TextView heading = (TextView) view.findViewById(R.id.transaction_id);
        heading.setText(transaction.getId().trim());

        String usedDiscounts = res.getString(R.string.used_discounts, transaction.hasUsedDiscounts());
        ((TextView) view.findViewById(R.id.used_discounts)).setText(usedDiscounts);

        String totalPrice = res.getString(R.string.total_payed_price, transaction.getPrice());
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
