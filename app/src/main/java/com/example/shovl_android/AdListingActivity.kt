package com.example.shovl_android

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shovl_android.adapters.ImagesRvAdapterAdList
import com.example.shovl_android.databinding.ActivityAdListingBinding
import com.example.shovl_android.utilities.ShovlConstants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AdListingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAdListingBinding
    //lateinit var imageUri : Uri
    var photosUrls = ArrayList<Uri>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAdListingBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            val dialog = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener{
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
            val dialog = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener{
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
                this,
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
                this,
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
            it.layoutManager= LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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

            val posts= hashMapOf<String, Any>(
                ShovlConstants.KEY_ADDRESS_POST to address,
                ShovlConstants.KEY_TITLE_POST to postTitle,
                ShovlConstants.KEY_DESCRIPTION to description,
                ShovlConstants.KEY_DATE_FROM to dateFrom,
                ShovlConstants.KEY_DATE_TO to dateTo,
                ShovlConstants.KEY_TIME_FROM to timeFrom,
                ShovlConstants.KEY_TIME_TO to timeTo,
            )


            val db= FirebaseFirestore.getInstance()
            db.collection(ShovlConstants.KEY_COLLECTION_POSTS)
                .add(posts)
                .addOnSuccessListener {
                    Toast.makeText(this, "Ad has been posted.", Toast.LENGTH_SHORT).show()
                    uploadImage()

                }
                .addOnFailureListener {
                    Toast.makeText(this, "There were some errors posting the ad. Please try again." +
                            "Sorry, for the inconvinience", Toast.LENGTH_SHORT).show()
                }

        }

        binding.btnCancelAdListing.setOnClickListener {
            finish()
        }


    }

    private fun uploadImage(){
        val pb = ProgressDialog(this)
        pb.setMessage("Uploading photos")
        pb.setCancelable(false)
        pb.show()

        val storageRef = FirebaseStorage.getInstance().getReference(ShovlConstants.IMAGES_REF_FIREBASE)

        var i = 0
        while (i < photosUrls.size) {
            val image: Uri = photosUrls[i]
            val imagename = storageRef.child(image.lastPathSegment.toString())
            imagename.putFile(photosUrls[i]).addOnSuccessListener {
                    val url = image.toString()
                    sendLink(url)

            }.addOnFailureListener {

            }
            i++
        }

        startActivity(Intent(this, PaymentActivity::class.java))


    }

    private fun sendLink(url: String) {
        val hashMap = HashMap<String, String>()
        hashMap["link"] = url

        val firestore = FirebaseFirestore.getInstance()
        val docId = firestore.collection(ShovlConstants.KEY_COLLECTION_POSTS).document().id


    }


    private fun selectImage(){
        val intent = Intent(Intent.ACTION_PICK
            , MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeType = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)


        startActivityForResult(intent, ShovlConstants.GALLERY_IMAGE_PICK)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ShovlConstants.GALLERY_IMAGE_PICK && resultCode== RESULT_OK){

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

