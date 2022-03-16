package com.example.shovl_android

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import com.example.shovl_android.databinding.ActivityAdListingBinding
import java.text.DateFormat
import java.util.*

class AdListingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAdListingBinding

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

        binding.btnDoneAdListing.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }

        binding.btnCancelAdListing.setOnClickListener {
            finish()
        }


    }

    private fun showDatePickerDialog(view: View?, c: Calendar) {

    }

}

