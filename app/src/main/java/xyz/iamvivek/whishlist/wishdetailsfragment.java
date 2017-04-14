package xyz.iamvivek.whishlist;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by balramp on 01/04/17.
 */

public class wishdetailsfragment extends Fragment {
    String id="";
    String url = "http://balumenon.net63.net/wishlist/wishlist/browse_wish_byid.php";
    ProgressDialog pDialog;
    String tag_json_obj = "json_obj_req";
    List<pojoclass2> list = new ArrayList<pojoclass2>();
    RecyclerView recyclerview;
    private listadapter mAdapter;
    TextView title,wish,tag,date,like,help;
    View view;
    String value;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {view = inflater.inflate(R.layout.wishdetailsfragment, container, false);
        value= getArguments().getString("item");
        Log.e("final",value+"");
        title=(TextView) view.findViewById(R.id.wish_title);
        wish=(TextView) view.findViewById(R.id.wish_details);
        date=(TextView) view.findViewById(R.id.dates);
        tag=(TextView) view.findViewById(R.id.tags);
        like=(TextView) view.findViewById(R.id.likes);
        help=(TextView) view.findViewById(R.id.helps);
        SharedPreferences prefs = getActivity().getSharedPreferences("user_details", MODE_PRIVATE);
        id= prefs.getString("id", "100");
        pDialog= new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("success", response.toString());


                pDialog.hide();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("success")==2) {
                        // Toast.makeText(LoginActivity.this, "Sucess, user id:"+ obj.getString("user_id"), Toast.LENGTH_SHORT).show();
                        JSONObject jobj=obj.getJSONObject("details");

                        title.setText(jobj.getString("title"));
                        wish.setText(jobj.getString("desc"));
                        tag.setText(jobj.getString("tags"));
                        date.setText(jobj.getString("time"));
                        like.setText(jobj.getString("likes")+" Likes");
                        help.setText(jobj.getString("helps")+" Helps");
                        JSONArray jsonarray=jobj.getJSONArray("helpers");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject c = jsonarray.getJSONObject(i);
                            pojoclass2 pc=new pojoclass2();
                            pc.setTitle(c.getString("name"));
                            pc.setDate(c.getString("mobile"));

                            list.add(pc);
                            recyclerview = (RecyclerView) view.findViewById(R.id.list2);

                            mAdapter = new listadapter(list, new myWishFragment.OnListFragmentInteractionListener() {
                                @Override
                                public void onListFragmentInteraction(int item) {
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                    //add a fragment
                                    wishdetailsfragment myFragment = new wishdetailsfragment();

                                    Bundle args = new Bundle();
                                    args.putInt("item", item);
                                    myFragment.setArguments(args);
                                    fragmentTransaction.replace(R.id.main2, myFragment);
                                    fragmentTransaction.commit();

                                }
                            });
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerview.setLayoutManager(mLayoutManager);
                            recyclerview.setItemAnimator(new DefaultItemAnimator());
                            recyclerview.setAdapter(mAdapter);
                        }
                        Log.e("inside","log");
                    }
                    else{

                    }

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
            params.put("wish_id", value);


            return params;
        }};

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);


        return view;
    }
}
