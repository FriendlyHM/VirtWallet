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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private VirtWallet wallet;

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

        try {
            checkForJson();
            total.setText("Total: $" + wallet.getStringTotal());
        } catch (FileNotFoundException e) {
            wallet = new VirtWallet("test");
        }

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

        Button saveBtn = (Button) findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(wallet.getCash(), wallet.getCoins());
            }
        });
    }

    /**
     * Checks if there exists a json file and creates a wallet
     * @throws FileNotFoundException
     */
    public void checkForJson() throws FileNotFoundException {
        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path+"/wallet.json"));
        Gson gson = new Gson();
        Object json = gson.fromJson(bufferedReader, Object.class);
        String jsonString = json.toString();
        gson = new Gson();
        Type walletMapType = new TypeToken<HashMap<String,HashMap<Double,Integer>>>() {}.getType();
        HashMap<String,HashMap<Double,Integer>> walletHash = gson.fromJson(jsonString, walletMapType);

        HashMap<Double,Integer> cash = walletHash.get("cash");
        HashMap<Double,Integer> coins = walletHash.get("coins");

        wallet = new VirtWallet("test", cash, coins);
    }

    /**
     * Saves the current wallet most important information to a json file
     * @param cash - contains type of cash (Double) and how many of that type (Integer)
     * @param coins - contains type of coins (Double) and how many of that type (Integer)
     */
    public void save(Map<Double,Integer> cash, Map<Double,Integer> coins) {
        try {
            Map<String,Map<Double,Integer>> hm= new HashMap<>();
            hm.put("cash", cash);
            hm.put("coins", coins);
            Gson gson = new Gson();
            String json = gson.toJson(hm);
            Writer output = null;
            File file = new File(getApplicationContext().getFilesDir(), "wallet.json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(json);
            output.close();

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
        total.setText("Total: $" + wallet.getStringTotal());
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
            case R.id.twenty_dollar:
                add(20.00);
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
        private ImageView thumbnail;

        public myListAdapter(int resource, Map<Double, Integer> map) {
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
            thumbnail = (ImageView) result.findViewById(R.id.list_item_thumbnail);

            //set thumbnail
            switch (getItem(position).getKey().toString()) {
                case "1.0":
                    thumbnail.setImageResource(R.drawable.one);
                    break;
                case "2.0":
                    thumbnail.setImageResource(R.drawable.two);
                    break;
                case "5.0":
                    thumbnail.setImageResource(R.drawable.five);
                    break;
                case "10.0":
                    thumbnail.setImageResource(R.drawable.ten);
                    break;
                case "20.0":
                    thumbnail.setImageResource(R.drawable.twenty);
                    break;
                case "50.0":
                    thumbnail.setImageResource(R.drawable.fifty);
                    break;
                case "100.0":
                    thumbnail.setImageResource(R.drawable.hundred);
                    break;
                case "0.01":
                    thumbnail.setImageResource(R.drawable.penny);
                    break;
                case "0.05":
                    thumbnail.setImageResource(R.drawable.nickel);
                    break;
                case "0.1":
                    thumbnail.setImageResource(R.drawable.dime);
                    break;
                case "0.25":
                    thumbnail.setImageResource(R.drawable.quarter);
                    break;


            }

            //button click for minus
            minusBtn = result.findViewById(R.id.list_item_minusbtn);
            minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    minus(getItem(position).getKey());
                    //Toast.makeText(getApplicationContext(), "Removed $" + getItem(position).getKey(), Toast.LENGTH_SHORT).show();
                }
            });

            //button click for plus
            plusBtn = result.findViewById(R.id.list_item_plusbtn);
            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add(getItem(position).getKey());
                    //Toast.makeText(getApplicationContext(), "Added $" + getItem(position).getKey(), Toast.LENGTH_SHORT).show();
                }
            });

            return result;
        }

    }
}
