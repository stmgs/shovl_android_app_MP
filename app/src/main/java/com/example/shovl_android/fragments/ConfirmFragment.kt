package com.example.shovl_android.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.shovl_android.HomeActivity
import com.example.shovl_android.PaymentActivity1
import com.example.shovl_android.adapters.ConfirmShovelerAdapter
import com.example.shovl_android.data.Bidders
import com.example.shovl_android.data.Post
import com.example.shovl_android.databinding.FragmentConfirmBinding
import com.example.shovl_android.utilities.PreferenceMangager
import com.example.shovl_android.utilities.ShovlConstants
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import org.json.JSONException
import org.json.JSONObject

class ConfirmFragment : Fragment() {

    private lateinit var binding : FragmentConfirmBinding
    private lateinit var preferenceMangager: PreferenceMangager
    var SECRET_KEY =
        "sk_test_51L4qAgKDCJiANE10hYGDVgZ6mluJl15oYjSdVSu6BM0nFW4QtCReu4YWSG73MZmsuovsblG21M22VpChGnRC7MQl003QlaG6C9"
    var PUBLISH_KEY =
        "pk_test_51L4qAgKDCJiANE10DbHvVSKuGjWs70OE5mLK6SVqsyMpkGYDAM54CM3SEVkT9WMtaloaz54UBTiWiO54UPNJj6Et00fKAWQWqh"

    var paymentSheet: PaymentSheet? = null

    var customerID: String? = null
    var EphericalKey: String? = null
    var ClientSecret: String? = null

    var postList = ArrayList<Post>()
    var postListWithoutRejected = ArrayList<Post>()
    var postModel: Post = Post()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceMangager = PreferenceMangager(requireContext())

        PaymentConfiguration.init(requireContext(), PUBLISH_KEY)

        paymentSheet = PaymentSheet(
            this
        ) { paymentSheetResult: PaymentSheetResult? ->
            onPaymentResult(
                paymentSheetResult!!
            )
        }

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST,
            "https://api.stripe.com/v1/customers",
            Response.Listener { response ->
                try {
                    val `object` = JSONObject(response)
                    customerID = `object`.getString("id")
                    Toast.makeText(requireContext(), customerID, Toast.LENGTH_SHORT).show()
                    getEphericalKey(customerID.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { }) {
            // Authoraization Tool
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val header: MutableMap<String, String> = java.util.HashMap()
                header["Authorization"] = "Bearer $SECRET_KEY"
                return header
            }
        }

        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)

        val db= FirebaseFirestore.getInstance()
        db.collection(ShovlConstants.KEY_COLLECTION_POSTS)
            .whereEqualTo(ShovlConstants.POSTED_BY
                ,preferenceMangager.getString(ShovlConstants.KEY_USER_ID))
            .get()
            .addOnSuccessListener { it ->

                if (it.isEmpty){
                    //if block is working fine
                    binding.tvNoDataPosts.visibility=View.VISIBLE
                } else{
                    binding.tvNoDataPosts.visibility=View.GONE
                    //postModel =it.toObject(Post::class.java)
                    //postModel.id = postDocumentFromFirestore.id

                    postList.clear()
                    for (postDocumentFromFirestore in it){
                        postModel =postDocumentFromFirestore.toObject(Post::class.java)
                        postModel.id = postDocumentFromFirestore.id
                        postList.add(postModel)
                    }

                    binding.rvConfirmShoveler.also {recyclerview->
                        recyclerview.layoutManager= LinearLayoutManager(requireContext()
                            , LinearLayoutManager.VERTICAL, false)
                        recyclerview.setHasFixedSize(true)
                        //println("bidder list:"+postModel.bidders)
                        recyclerview.adapter =
                            postList[0].bidders?.let { it1 ->
                                ConfirmShovelerAdapter(it1, object : ConfirmShovelerAdapter.ConfirmRVClickListener{
                                    override fun onConfirmClicked(bidder: Bidders) {
                                        //open a payment botton fragment here
                                        PaymentFlow()

                                        /*val intent = Intent(requireContext(), PaymentActivity1::class.java)
                                        //intent.putExtra("bidder_data", bidder)
                                        startActivity(intent)*/
                                    }

                                    override fun onDeleteClicked(bidder: Bidders) {
                                        it1.remove(bidder)

                                        val editedBidder = Bidders(bidder.user_id, bidder.name, bidder.price,bidder.time, true)
                                        it1.add(editedBidder)

                                        val postRef = db.collection(ShovlConstants.KEY_COLLECTION_POSTS)
                                        val docRef = postRef.document(postModel.id.toString())

                                        docRef.set(mutableMapOf(ShovlConstants.KEY_BIDDERS to it1),
                                            SetOptions.merge()).addOnCompleteListener {setTask->
                                            if (setTask.isSuccessful){
                                                Toast.makeText(requireContext(), "user rejected.", Toast.LENGTH_SHORT).show()
                                                val intent = Intent(requireContext(), HomeActivity::class.java)
                                                intent.flags =
                                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                startActivity(intent)
                                            }
                                        }

                                            //postRef.whereArrayContains(ShovlConstants.KEY_BIDDERS,bidder.user_id)
                                            /*docRef.update("${ShovlConstants.KEY_BIDDERS},${bidder.user_id}",FieldValue
                                                .arrayUnion(bidderMap))
                                            .addOnSuccessListener {
                                                Toast.makeText(requireContext(), "user rejected.", Toast.LENGTH_SHORT).show()

                                                val intent = Intent(requireContext(), HomeActivity::class.java)
                                                intent.flags =
                                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                startActivity(intent)

                                            }.addOnFailureListener {
                                                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
                                            }*/

                                        binding.rvConfirmShoveler.adapter?.notifyDataSetChanged()
                                    }

                                })
                            }
                    }


                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }


    }

    private fun onPaymentResult(paymentSheetResult: PaymentSheetResult) {
        if (paymentSheetResult is PaymentSheetResult.Completed) {
            Toast.makeText(requireContext(), "Payment Success", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getEphericalKey(customerID: String) {
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST,
            "https://api.stripe.com/v1/ephemeral_keys",
            Response.Listener { response ->
                try {
                    val `object` = JSONObject(response)
                    EphericalKey = `object`.getString("id")
                    Toast.makeText(requireContext(), EphericalKey, Toast.LENGTH_SHORT).show()
                    getClientSecret(customerID, EphericalKey.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { }) {
            // Authoraization Tool
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val header: MutableMap<String, String> = HashMap()
                header["Authorization"] = "Bearer $SECRET_KEY"
                header["Stripe-Version"] = "2020-08-27"
                return header
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["customer"] = customerID
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    private fun getClientSecret(customerID: String, ephericalKey: String) {
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST,
            "https://api.stripe.com/v1/payment_intents",
            Response.Listener { response ->
                try {
                    val `object` = JSONObject(response)
                    ClientSecret = `object`.getString("client_secret")
                    Toast.makeText(requireContext(), ClientSecret, Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { }) {
            // Authoraization Tool
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val header: MutableMap<String, String> = HashMap()
                header["Authorization"] = "Bearer $SECRET_KEY"
                return header
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["customer"] = customerID
                params["amount"] = "1000" + "00"
                params["currency"] = "usd"
                params["automatic_payment_methods[enabled]"] = "true"
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    private fun PaymentFlow() {
        paymentSheet!!.presentWithPaymentIntent(
            ClientSecret!!, PaymentSheet.Configuration(
                "Shovler",
                PaymentSheet.CustomerConfiguration(
                    customerID.toString(),
                    EphericalKey.toString()
                )
            )
        )
    }

}