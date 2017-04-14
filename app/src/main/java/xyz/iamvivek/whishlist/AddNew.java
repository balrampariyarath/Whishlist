package xyz.iamvivek.whishlist;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class AddNew extends Fragment {
    public AddNew() {
        // Required empty public constructor
    }
     View view;
    Button btn;
    String url = "http://balumenon.net63.net/wishlist/wishlist/add_wish.php";
    ProgressDialog pDialog;
    String tag_json_obj = "json_obj_req";
    public Button cancelButton;
    String  id="";
    EditText wish,desc,tag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences prefs = getActivity().getSharedPreferences("user_details", MODE_PRIVATE);
        id= prefs.getString("id", "100");
        view = inflater.inflate(R.layout.fragment_add_new, container, false);
        btn=(Button) view.findViewById(R.id.addWish);
        wish=(EditText) view.findViewById(R.id.wishTitle);
        desc=(EditText) view.findViewById(R.id.wishBody);
        tag=(EditText) view.findViewById(R.id.wishTags) ;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog= new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.show();
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("sucess", response.toString());
                        Toast.makeText(getActivity(), "Sucesss", Toast.LENGTH_SHORT).show();

                        pDialog.hide();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error", "Error: " + error.getMessage());
                        // hide the progress dialog
                        pDialog.hide();
                    }
                }){@Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("user_id", id);
                    params.put("title", wish.getText().toString());
                    params.put("desc", desc.getText().toString());
                    params.put("tags", tag.getText().toString());



                    return params;
                }};

// Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
            }
        });
        cancelButton = (Button) view.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new myWishFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                FloatingActionButton button = (FloatingActionButton) getActivity().findViewById(R.id.fab);
                View view1 = getActivity().findViewById(R.id.main2);
                button.setVisibility(view1.VISIBLE);
                //add a fragment
                myWishFragment myFragment = new myWishFragment();
                fragmentTransaction.replace(R.id.main2, myFragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}
