package xyz.iamvivek.whishlist;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by balramp on 01/04/17.
 */

public class likefragment extends Fragment {
    View view;
    String url = "http://balumenon.net63.net/wishlist/wishlist/help_wish.php";
    String url1 = "http://balumenon.net63.net/wishlist/wishlist/like_wish.php";
    String url2= "http://balumenon.net63.net/wishlist/wishlist/expand_wish_by_ids.php";
    ProgressDialog pDialog;
    String tag_json_obj = "json_obj_req";
    String value;
    String id;
    TextView text,desc,tags,date,likes,helps;
    FloatingActionButton like,help;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.likefragment, container, false);
        text=(TextView) view.findViewById(R.id.wish_title);
        desc=(TextView) view.findViewById(R.id.wish_details);
        tags=(TextView) view.findViewById(R.id.tags);
        date=(TextView) view.findViewById(R.id.dates);
        likes=(TextView) view.findViewById(R.id.likes);
        helps=(TextView) view.findViewById(R.id.helps);
        value= getArguments().getString("item");
        SharedPreferences prefs = getActivity().getSharedPreferences("user_details", MODE_PRIVATE);
        id= prefs.getString("id", "100");
        pDialog= new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("like", response.toString()+value+"+"+id);



                pDialog.hide();
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONObject detail=obj.getJSONObject("details");
                    text.setText(detail.getString("title"));
                    desc.setText(detail.getString("desc"));
                    tags.setText(detail.getString("tags"));
                    date.setText(detail.getString("time"));
                    likes.setText(detail.getString("likes")+" Likes");
                    helps.setText(detail.getString("helps")+" Helps");

                }
                catch(Exception e){
                    Log.e("ex",e.toString());
                }
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


            params.put("wish_id", value+"");



            return params;
        }};

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

        like=(FloatingActionButton) view.findViewById(R.id.fab2) ;
        help=(FloatingActionButton) view.findViewById(R.id.fab1);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog= new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.show();
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        url1, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("sucess", response.toString());


                        pDialog.hide();
                        try {
                            Toast.makeText(getActivity(), "Like successful", Toast.LENGTH_SHORT).show();

                        }
                        catch(Exception e){
                            Log.e("ex",e.toString());
                        }
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
                    params.put("wish_id", value+"");



                    return params;
                }};

// Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

            }
        });
        help.setOnClickListener(new View.OnClickListener() {
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


                        pDialog.hide();
                        try {

                            Toast.makeText(getActivity(), "Great your frined will be notified", Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception e){
                            Log.e("ex",e.toString());
                        }
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
                    params.put("wish_id", value+"");



                    return params;
                }};

// Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
            }
        });




        return  view;
    }
}
