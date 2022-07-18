package com.shovl.shovl_android.fragments

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.shovl.shovl_android.adapters.ImagesRvAdapterAdList
import com.shovl.shovl_android.databinding.FragmentAddPostBinding
import com.shovl.shovl_android.utilities.PreferenceMangager
import com.shovl.shovl_android.utilities.ShovlConstants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddPostFragment : Fragment() {

    private lateinit var binding : FragmentAddPostBinding
    var photosUrls = ArrayList<Uri>()
    val imageUrlList = ArrayList<String>()
    private lateinit var pb : ProgressDialog
    private lateinit var preferenceMangager: PreferenceMangager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pb = ProgressDialog(requireContext())
        preferenceMangager = PreferenceMangager(requireContext())

        //Places.initialize(applicationContext, "")

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val currentDateString = DateFormat.getDateInstance().format(c.time)
        val curretTimeString = DateFormat.getDateTimeInstance().format(c.time.time)


        binding.etDateFromAdListing.text = currentDateString

        binding.etDateFromAdListing.setOnClickListener {
            //showDatePickerDialog(it, c)
            val dialog = DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(datePicker: DatePicker?, y: Int, m: Int, d: Int) {
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.YEAR, y)
                    cal.set(Calendar.MONTH,m)
                    cal.set(Calendar.DAY_OF_MONTH,d)

                    val newDateString = DateFormat.getDateInstance().format(cal.time)
                    binding.etDateFromAdListing.text = newDateString
                }
            }, year, month, day)
            dialog.show()
        }

        binding.etDateToAdListing.setOnClickListener {
            //showDatePickerDialog(it, c)
            val dialog = DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(datePicker: DatePicker?, y: Int, m: Int, d: Int) {
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.YEAR, y)
                    cal.set(Calendar.MONTH,m)
                    cal.set(Calendar.DAY_OF_MONTH,d)

                    val newDateString = DateFormat.getDateInstance().format(cal.time)
                    binding.etDateToAdListing.text = newDateString
                }
            }, year, month, day)
            dialog.show()
        }

        //binding.etTimeFromAdListing.text=curretTimeString
        binding.etTimeFromAdListing.setOnClickListener {
            val timePicker = TimePickerDialog(
                // pass the Context
                requireContext(),
                object : TimePickerDialog.OnTimeSetListener{
                    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {

                        val formattedTime: String = when {
                            hourOfDay == 0 -> {
                                if (minute < 10) {
                                    "${hourOfDay + 12}:0${minute} am"
                                } else {
                                    "${hourOfDay + 12}:${minute} am"
                                }
                            }
                            hourOfDay > 12 -> {
                                if (minute < 10) {
                                    "${hourOfDay - 12}:0${minute} pm"
                                } else {
                                    "${hourOfDay - 12}:${minute} pm"
                                }
                            }
                            hourOfDay == 12 -> {
                                if (minute < 10) {
                                    "${hourOfDay}:0${minute} pm"
                                } else {
                                    "${hourOfDay}:${minute} pm"
                                }
                            }
                            else -> {
                                if (minute < 10) {
                                    "${hourOfDay}:${minute} am"
                                } else {
                                    "${hourOfDay}:${minute} am"
                                }
                            }
                        }
                        binding.etTimeFromAdListing.text=formattedTime
                    } },
                hour,
                // default minute when the time picker
                // dialog is opened
                minute,
                // 24 hours time picker is
                // false (varies according to the region)
                false
            )

            // then after building the timepicker
            // dialog show the dialog to user
            timePicker.show()
        }

        // binding.etTimeToAdListing.text=curretTimeString
        binding.etTimeToAdListing.setOnClickListener {
            val timePicker = TimePickerDialog(
                // pass the Context
                requireContext(),
                object : TimePickerDialog.OnTimeSetListener{
                    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {

                        val formattedTime: String = when {
                            hourOfDay == 0 -> {
                                if (minute < 10) {
                                    "${hourOfDay + 12}:0${minute} am"
                                } else {
                                    "${hourOfDay + 12}:${minute} am"
                                }
                            }
                            hourOfDay > 12 -> {
                                if (minute < 10) {
                                    "${hourOfDay - 12}:0${minute} pm"
                                } else {
                                    "${hourOfDay - 12}:${minute} pm"
                                }
                            }
                            hourOfDay == 12 -> {
                                if (minute < 10) {
                                    "${hourOfDay}:0${minute} pm"
                                } else {
                                    "${hourOfDay}:${minute} pm"
                                }
                            }
                            else -> {
                                if (minute < 10) {
                                    "${hourOfDay}:${minute} am"
                                } else {
                                    "${hourOfDay}:${minute} am"
                                }
                            }
                        }
                        binding.etTimeToAdListing.text=formattedTime
                    } },
                hour,
                // default minute when the time picker
                // dialog is opened
                minute,
                // 24 hours time picker is
                // false (varies according to the region)
                false
            )

            // then after building the timepicker
            // dialog show the dialog to user
            timePicker.show()
        }

        binding.ivAddImageAdList.setOnClickListener {
            selectImage()
        }

        binding.rvAddImagePost.also {
            it.layoutManager= LinearLayoutManager(requireContext()
                , LinearLayoutManager.HORIZONTAL, false)
            it.setHasFixedSize(true)
            it.adapter =
                ImagesRvAdapterAdList(photosUrls)
        }

        binding.btnDoneAdListing.setOnClickListener {
            val address = binding.etAddressAdListing.text.toString()
            val postTitle = binding.etPostTitleAdListing.text.toString()
            val description = binding.etDescriptionAdListing.text.toString()
            val dateFrom = binding.etDateFromAdListing.text.toString()
            val dateTo = binding.etDateToAdListing.text.toString()
            val timeFrom = binding.etTimeFromAdListing.text.toString()
            val timeTo = binding.etTimeToAdListing.text.toString()

            if (address.isEmpty()||postTitle.isEmpty()||description.isEmpty()||dateFrom.isEmpty()
                ||dateTo.isEmpty()||timeFrom.isEmpty()||timeTo.isEmpty()){
                Toast.makeText(requireContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            } else{
                val posts= hashMapOf<String, Any>(
                    ShovlConstants.KEY_ADDRESS_POST to address,
                    ShovlConstants.KEY_TITLE_POST to postTitle,
                    ShovlConstants.KEY_DESCRIPTION to description,
                    ShovlConstants.KEY_DATE_FROM to dateFrom,
                    ShovlConstants.KEY_DATE_TO to dateTo,
                    ShovlConstants.KEY_TIME_FROM to timeFrom,
                    ShovlConstants.KEY_TIME_TO to timeTo,
                    ShovlConstants.POSTED_BY to preferenceMangager.getString(ShovlConstants.KEY_USER_ID)
                )


                val db= FirebaseFirestore.getInstance()
                db.collection(ShovlConstants.KEY_COLLECTION_POSTS)
                    .add(posts)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Ad has been posted.", Toast.LENGTH_SHORT).show()
                        uploadImage(it.id)

                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "There were some errors posting the ad. Please try again." +
                                "Sorry, for the inconvinience", Toast.LENGTH_SHORT).show()
                    }
            }

        }

        binding.btnCancelAdListing.setOnClickListener {
            clearAllFields()
        }

    }

    fun clearAllFields(){
        binding.etAddressAdListing.text=null
        binding.etPostTitleAdListing.text=null
        binding.etDescriptionAdListing.text=null
        binding.etTimeFromAdListing.text=null
        binding.etTimeToAdListing.text=null
        binding.etDateFromAdListing.text=null
        binding.etDateToAdListing.text=null
        photosUrls.clear()
        binding.rvAddImagePost.adapter?.notifyDataSetChanged()
    }

    private fun uploadImage(toString: String) {
        pb.setMessage("Uploading photos")
        pb.setCancelable(false)
        pb.show()

        val storageRef = FirebaseStorage.getInstance().getReference(ShovlConstants.IMAGES_REF_FIREBASE)

        var i = 0
        while (i < photosUrls.size) {
            val image: Uri = photosUrls[i]
            val imagename = storageRef.child(image.lastPathSegment.toString())
            imagename.putFile(photosUrls[i]).addOnSuccessListener {

                imagename.downloadUrl.addOnSuccessListener {
                    val url = it.toString()
                    sendLink(url, toString)
                }

            }.addOnFailureListener {

            }
            i++
        }
        clearAllFields()

    }

    private fun sendLink(url: String, toString: String) {
        imageUrlList.add(url)

        val firestore = FirebaseFirestore.getInstance()

        firestore.collection(ShovlConstants.KEY_COLLECTION_POSTS)
            .document(toString)
            .update("images", imageUrlList)
            .addOnSuccessListener {
                pb.dismiss()

            }.addOnFailureListener {

            }

    }

    private fun selectImage(){
        val intent = Intent(
            Intent.ACTION_PICK
            , MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeType = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)


        startActivityForResult(intent, ShovlConstants.GALLERY_IMAGE_PICK)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ShovlConstants.GALLERY_IMAGE_PICK && resultCode== AppCompatActivity.RESULT_OK){

            /* if (data?.clipData != null){
                 val count = data.clipData!!.itemCount
                 var curentIndex = 0

                 while (curentIndex<count){
                     var imageUri= data.clipData!!.getItemAt(curentIndex).uri

                 }

             }*/
            photosUrls.add(data?.data!!)
            binding.rvAddImagePost.adapter?.notifyDataSetChanged()
            //binding.iv1AddListing.setImageURI(imageUri)
            //set the recyclerview here


        }
    }


}