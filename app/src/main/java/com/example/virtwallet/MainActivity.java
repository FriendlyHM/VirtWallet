package com.example.virtwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private VirtWallet wallet = new VirtWallet("test");

    private ListView cashList;
    private ListView coinsList;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        total = (TextView) findViewById(R.id.total);
        cashList = (ListView) findViewById(R.id.listview);
        coinsList = (ListView) findViewById(R.id.listview2);
        cashList.setAdapter(new myListAdapter(R.layout.list_item, wallet.getCash()));
        coinsList.setAdapter(new myListAdapter(R.layout.list_item, wallet.getCoins()));

        ImageButton addBtn = (ImageButton) findViewById(R.id.addButton);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.inflate(R.menu.popup_menu);
                popup.show();
            }
        });
    }

    /**
     * adds money into the wallet and displays to corresponding changes to the screen
     * @param a - the amount being added
     */
    public void add(double a) {
        //add money
        String type = "cash";
        if (Double.compare(a, 1.00) < 0) {
            type = "coins";
        }
        wallet.addMoney(a, type);

        //update changes
        if(type.equals("cash")) {
            cashList.setAdapter(new myListAdapter(R.layout.list_item, wallet.getCash()));
        } else {
            coinsList.setAdapter(new myListAdapter(R.layout.list_item, wallet.getCoins()));
        }
        total.setText("Total:    $" + wallet.getStringTotal());
    }

    /**
     * subtracts money from the wallet and displays to corresponding changes to the screen
     * @param a - the amount being subtracted
     */
    public void minus(double a) {
        String type = "cash";
        if (Double.compare(a, 1.00) < 0) {
            type = "coins";
        }
        wallet.removeMoney(a, type);
        if(type.equals("cash")) {
            cashList.setAdapter(new myListAdapter(R.layout.list_item, wallet.getCash()));
        } else {
            coinsList.setAdapter(new myListAdapter(R.layout.list_item, wallet.getCoins()));
        }
        total.setText("Total: $" + wallet.getStringTotal());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.penny:
                add(0.01);
                return true;
            case R.id.nickel:
                add(0.05);
                return true;
            case R.id.dime:
                add(0.10);
                return true;
            case R.id.quarter:
                add(0.25);
                return true;
            case R.id.one_dollar:
                add(1.00);
                return true;
            case R.id.two_dollar:
                add(2.00);
                return true;
            case R.id.five_dollar:
                add(5.00);
                return true;
            case R.id.ten_dollar:
                add(10.00);
                return true;
            case R.id.twenty_five_dollar:
                add(25.00);
                return true;
            case R.id.fifty_dollar:
                add(50.00);
                return true;
            case R.id.one_hundred_dollar:
                add(100.00);
                return true;
            default:
                return false;
        }
    }

    public class myListAdapter extends BaseAdapter {
        private final ArrayList mData;
        private int layout;
        private TextView textView1;
        private TextView textView2;
        private Button minusBtn;
        private Button plusBtn;

        public myListAdapter(int resource, HashMap<Double, Integer> map) {
            mData = new ArrayList();
            mData.addAll(map.entrySet());
            layout = resource;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public HashMap.Entry<Double, Integer> getItem(int position) {
            return (HashMap.Entry) mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View result;

            if (convertView == null) {
                result = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            } else {
                result = convertView;
            }

            HashMap.Entry<Double, Integer> item = getItem(position);
            textView1 = ((TextView) result.findViewById(R.id.list_item_text));
            textView1.setText(""+item.getKey());
            textView2 = ((TextView) result.findViewById(R.id.count_item_text));
            textView2.setText(""+item.getValue());

            //button click for minus
            minusBtn = result.findViewById(R.id.list_item_minusbtn);
            minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    minus(getItem(position).getKey());
                    Toast.makeText(getApplicationContext(), "Removed $" + getItem(position).getKey(), Toast.LENGTH_SHORT).show();
                }
            });

            //button click for plus
            plusBtn = result.findViewById(R.id.list_item_plusbtn);
            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add(getItem(position).getKey());
                    Toast.makeText(getApplicationContext(), "Added $" + getItem(position).getKey(), Toast.LENGTH_SHORT).show();
                }
            });

            return result;
        }
    }
}
