package nyc.c4q.syd.updateme;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by July on 7/19/15.
 */
public class StockFragment extends Fragment{
    private ArrayList<String> stocks;
    private ArrayAdapter<String> stockAdapter;
    private ListView lvStocks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stock_layout, container, false);
        lvStocks = (ListView) view.findViewById(R.id.stockList);
        stocks = new ArrayList<String>();
        readItems();
        stockAdapter = new ArrayAdapter<String>(getActivity(), R.layout.stock_textview, stocks);
        lvStocks.setAdapter(stockAdapter);
        //setListViewHeightBasedOnChildren(lvStocks);
        if (stocks.size() == 0)
            stocks.add("MSFT");

        ImageButton addStock = (ImageButton) view.findViewById(R.id.addStock);
        addStock.setOnClickListener(addStockListener);
        lvStocks.setOnItemClickListener(lvItemClickListener);
        lvStocks.setOnItemLongClickListener(lvItemLongClickListener);
        return view;

    }

    // save and load items from stock list
    private void readItems() {
        File filesDir = getActivity().getFilesDir();
        File todoFile = new File(filesDir, "stock.txt");
        try {
            stocks = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            stocks = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getActivity().getFilesDir();
        File todoFile = new File(filesDir, "stock.txt");
        try {
            FileUtils.writeLines(todoFile, stocks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // add item to stock list & adjust view size
    View.OnClickListener addStockListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            final EditText todoET = new EditText(getActivity());
            dialogBuilder.setTitle("Enter Stock")
                    .setMessage("Which stock would you like to add today?")
                    .setView(todoET)
                    .setPositiveButton("Add Stock", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String itemText = todoET.getText().toString().toUpperCase();
                            stocks.add(itemText);
                            //setListViewHeightBasedOnChildren(lvStocks);
                        }
                    });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            writeItems();
        }
    };


    AdapterView.OnItemClickListener lvItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapter, View item, final int pos, long id) {
            String stockSymbol = stocks.get(pos);
            Intent i = new Intent(getActivity(), StockInfoActivity.class);
            i.putExtra("stock", stockSymbol);
            getActivity().startActivity(i);
        }
    };

    // option to delete item from stock list
    AdapterView.OnItemLongClickListener lvItemLongClickListener = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            dialogBuilder.setTitle("Remove Stock?")
                    .setMessage("Are you sure?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int pos) {
                            stocks.remove(i);
                            stockAdapter.notifyDataSetChanged();
                            writeItems();
                            //setListViewHeightBasedOnChildren(lvStocks);
                            Toast.makeText(getActivity(), "Stock Removed!", Toast.LENGTH_LONG).show();
                        }
                    });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            return true;
        }
    };
}
