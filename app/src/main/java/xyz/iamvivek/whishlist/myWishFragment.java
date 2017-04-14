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
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class myWishFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private WishViewAdapter mAdapter;
    String id="";
    String url = "http://balumenon.net63.net/wishlist/wishlist/get_my_wish.php";
    ProgressDialog pDialog;
    String tag_json_obj = "json_obj_req";
    List<pojoclass2> list = new ArrayList<pojoclass2>();
    View view;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public myWishFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mywish_list, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences("user_details", MODE_PRIVATE);


        id= prefs.getString("id", "100");


        Log.e("id",id);
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
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("success")==2) {
                        list.clear();
                        // Toast.makeText(LoginActivity.this, "Sucess, user id:"+ obj.getString("user_id"), Toast.LENGTH_SHORT).show();
                        JSONArray jsonarray=obj.getJSONArray("wishes");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject c = jsonarray.getJSONObject(i);
                            pojoclass2 pc=new pojoclass2();
                            pc.setDate(c.getString("time"));
                            pc.setTitle(c.getString("title"));
                            pc.setWishId(c.getString("wish_id"));
                            pc.setHelp(c.getInt("helps"));
                            pc.setWishDesc(c.getString("desc"));
                            pc.setWishTag(c.getString("tags"));
                            pc.setHelp(c.getInt("likes"));
                            list.add(pc);
                            recyclerView = (RecyclerView) view.findViewById(R.id.list2);

                            mAdapter = new WishViewAdapter(list, new WishViewAdapter.OnListFragmentInteractionListener() {
                                @Override
                                public void onListFragmentInteraction(int item) {
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                    //add a fragment
                                    wishdetailsfragment myFragment = new wishdetailsfragment();

                                    Bundle args = new Bundle();

                                    args.putString("item", list.get(item).getWishId());
                                    myFragment.setArguments(args);
                                    fragmentTransaction.replace(R.id.main2, myFragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                }
                            }, true,getActivity());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
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

            params.put("id", id);


            return params;
        }};

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);




        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            recyclerView.setAdapter(new MyWishRecyclerViewAdapter(list, mListener));
//        }
        return view;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(int pos);
    }
}
