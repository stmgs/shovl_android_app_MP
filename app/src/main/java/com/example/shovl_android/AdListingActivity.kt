package com.example.shovl_android

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
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


        binding.btnDoneAdListing.setOnClickListener {
            startActivity(Intent(this, ActivityDetails::class.java))
        }

        binding.btnCancelAdListing.setOnClickListener {
            finish()
        }


    }

    private fun showDatePickerDialog(view: View?, c: Calendar) {

    }

}

