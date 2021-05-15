package com.google.example.fridgefriend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.example.fridgefriend.dummy.DummyContent;
import com.google.example.fridgefriend.dummy.EdibleItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a list of Items.
 */
public class ShoppingList extends Fragment implements  RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<EdibleItem> ITEMS ;
    private MyItemRecyclerViewAdapter2 mAdapter;
    private ConstraintLayout mConstraintLayout;
    private static final int BARCODE_REQUEST = 10;
    static final int MANUAL_ENTRY = 2;
    boolean goingToFrigeList = false;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShoppingList() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ShoppingList newInstance(int columnCount) {
        ShoppingList fragment = new ShoppingList();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        ITEMS = new ArrayList<EdibleItem>();
        FirebaseData.firebaseData.setShoppingList(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list_list, container, false);
        RecyclerView recyclerView = ((ViewGroup)view).findViewById(R.id.list);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        mAdapter = new MyItemRecyclerViewAdapter2(requireContext(), ITEMS);
        mConstraintLayout = view.findViewById(R.id.constraint_fridgeList);
        fillListFromDatabase();
        // Set the adapter
        if (recyclerView != null) {
            Context context = view.getContext();

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(mAdapter);
        }
        FloatingActionButton foodFab= view.findViewById(R.id.add_food_fab);
        foodFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseData.firebaseData.getFridgeCode() !=null)
                    scanBarcode();
                else
                    Toast.makeText(getContext(), "Select a group from home", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void scanBarcode(){
        Intent intent = new Intent(getActivity(),CameraView.class );
        intent.putExtra("isQrScan", false);
        startActivityForResult(intent, BARCODE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == BARCODE_REQUEST && resultCode == RESULT_OK){
            Barcode barcode = data.getParcelableExtra("barcodeTag");
            int format = barcode.format;
            int valueType = barcode.valueFormat;
            if(format == Barcode.QR_CODE){

            }else//for all other formats, just try to compute it


                //productView.setText(barcode.rawValue);
                //String text ="rawValue = "  + barcode.rawValue+ " valueTpye = " + valueType;
                //textView.setText(text);
                Log.d("Testing stuff", barcode.displayValue);

            RequestQueue queue = Volley.newRequestQueue(requireContext());
            String url ="https://api.upcitemdb.com/prod/trial/lookup?upc=";
            url+=barcode.rawValue;
            Log.d("url", url);
// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //textView.setText("Response is: "+ response.substring(0,500));
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONArray jsonArray = (JSONArray)json.get("items");
                                JSONObject jsonObject1 =  jsonArray.getJSONObject(0);
                                //textView.setText(jsonObject1.get("title").toString());
                                String productName = jsonObject1.get("title").toString();
                                addProduct(productName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //textView.setText("That didn't work!");
                    Toast.makeText(requireContext(), "Unable to identify product. Enter manually please!", Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
        else if(requestCode == BARCODE_REQUEST && resultCode == MANUAL_ENTRY){
            Bundle bundle = data.getExtras();
            //String text = data.getStringExtra("com.example.testing.Product");
            //String text = (String)bundle.getString("Product");
            String local_text = data.getStringExtra("Product");
            addProduct(local_text);
            //textView.setText(text);
        }
        FirebaseData.firebaseData.getFridgeGroup().child("shoppinglist").setValue(ITEMS);
    }

    public void fillListFromDatabase(){
        //fillDatabase(); //for testing purposes
        DatabaseReference localRef = FirebaseData.firebaseData.getFridgeGroup();
        if(localRef!=null) {
            localRef = localRef.child("shoppinglist");
            localRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Object value = snapshot.getValue();
                    mAdapter.clear();
                    if (value instanceof ArrayList) {
                        for (Object o : (ArrayList) value) {
                            HashMap h = (HashMap) o;
                            addProduct((String) h.get("name"));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    /**
     * Takes a string and creates and EdibleItem to be added to the ITEM list
     * @param productName the name of the EdibleItem to be made
     *  TODO: check if the item already exists before adding it
     */
    private void addProduct(String productName){
        EdibleItem localEdibleItem = new EdibleItem(WordUtils.capitalize(productName.toLowerCase()));
        int count = mAdapter.getItemCount();
        mAdapter.addItem(localEdibleItem, count);
    }

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        String snackbarText = " removed from shopping list!";

        if (viewHolder instanceof MyItemRecyclerViewAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = ITEMS.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final EdibleItem deletedItem = ITEMS.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            if(direction == ItemTouchHelper.LEFT){
                //remove from fridge
                //Toast.makeText(getContext(), "left!!!", Toast.LENGTH_SHORT).show();
            }else if(direction == ItemTouchHelper.RIGHT){
                //send to shopping list
                //Toast.makeText(getContext(), "RIGHT!!!", Toast.LENGTH_SHORT).show();
                snackbarText = " sent to fridge list";
                goingToFrigeList = true;

            }

            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(mConstraintLayout, name + snackbarText, Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.addCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        // Snackbar closed on its own so we can upate the list
                        sendToFridgeList(deletedItem.getName());
                        FirebaseData.firebaseData.getFridgeGroup().child("shoppinglist").setValue(ITEMS);

                    }

                }

            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void fillDatabase(){
        FirebaseData.firebaseData.setFridgeGroup(FirebaseDatabase.getInstance().getReference());
        DatabaseReference localRef = FirebaseData.firebaseData.getFridgeGroup();
        for(int i = 0; i < 10;i++){

        }

        localRef = localRef.child("shoppinglist");
        localRef.setValue(ITEMS);
    }

    private void sendToFridgeList(String s){
        //FragmentManager f = getParentFragmentManager();
        if(goingToFrigeList){
            FridgeList f = FirebaseData.firebaseData.getFridgeList();
            f.addFromShoppingList(s);
            goingToFrigeList ^= goingToFrigeList;//i googled the coolest way to invert boolean
        }
    }

    /**
     * A public method to allow other classes to add to the
     * @param productName name of the product to be created
     */
    public void addFromFridgeList(String productName){
        addProduct(productName);
        FirebaseData.firebaseData.getFridgeGroup().child("shoppinglist").setValue(ITEMS);
    }
}