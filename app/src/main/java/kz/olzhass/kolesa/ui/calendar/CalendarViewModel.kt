package kz.olzhass.kolesa.ui.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.olzhass.kolesa.GlobalData
import kz.olzhass.kolesa.ui.appointment.Appointment
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarViewModel : ViewModel() {

    private val _activeAppointments = MutableLiveData<List<Appointment>>()
    val activeAppointments: LiveData<List<Appointment>> get() = _activeAppointments

    private val _previousAppointments = MutableLiveData<List<Appointment>>()
    val previousAppointments: LiveData<List<Appointment>> get() = _previousAppointments

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    private var userId: Int = -1

    private val client = OkHttpClient()

    fun fetchAppointments(userId: Int) {
        if (userId == -1) {
            _errorMessage.postValue("User ID is not available")
            return
        }

        val url = "http://${GlobalData.ip}:3000/appointments/$userId"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                _errorMessage.postValue("Appointments fetch failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    try {
                        val jsonResponse = JSONObject(responseBody ?: "{}")
                        Log.d("CalendarViewModel", "Response Body: $responseBody")
                        if (jsonResponse.getBoolean("success")) {
                            val appointmentsJsonArray = jsonResponse.getJSONArray("appointments")
                            val appointmentsList = mutableListOf<Appointment>()

                            Log.d("CalendarViewModel", "Appointments JSON Array: $appointmentsJsonArray")
                            for (i in 0 until appointmentsJsonArray.length()) {
                                val appJson = appointmentsJsonArray.getJSONObject(i)
                                Log.d("CalendarViewModel", "Appointment JSON Object: $appJson")
                                val appointment = Appointment(
                                    appointment_id = appJson.getInt("appointment_id"),
                                    user_id = appJson.getInt("user_id"),
                                    doctor_id = appJson.getInt("doctor_id"),
                                    appointment_date = appJson.getString("appointment_date"),
                                    appointment_time = appJson.getString("appointment_time"),
                                    appointment_phone = appJson.getString("appointment_phone"),
                                    appointment_reason = appJson.getString("appointment_reason"),
                                    appointment_status = appJson.getString("appointment_status"),
                                    appointment_created_at = appJson.getString("appointment_created_at"),
                                    doctor_name = appJson.getString("doctor_name"),
                                    doctor_phone = appJson.getString("doctor_phone"),
                                    doctor_email = appJson.getString("doctor_email"),
                                    doctor_created_at = appJson.getString("doctor_created_at")
                                )

                                appointmentsList.add(appointment)
                            }

                            // Разделяем встречи на активные и прошедшие
                            val activeList = mutableListOf<Appointment>()
                            val previousList = mutableListOf<Appointment>()

                            val currentDate = System.currentTimeMillis()

                            appointmentsList.forEach {
                                val appointmentDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(it.appointment_date)
                                val appointmentTime = appointmentDate?.time ?: 0

                                if (appointmentTime > currentDate) {
                                    activeList.add(it)
                                } else {
                                    previousList.add(it)
                                }
                            }

                            // Обновляем LiveData
                            _activeAppointments.postValue(activeList)
                            for (appointment in activeList) {
                                Log.d("ActivesFragment", "Appointment ID: ${appointment.appointment_id}, Doctor: ${appointment.doctor_name}, Time: ${appointment.appointment_time}")
                            }
                            _previousAppointments.postValue(previousList)
                            for (appointment in previousList) {
                                Log.d("ActivesFragment", "Appointment ID: ${appointment.appointment_id}, Doctor: ${appointment.doctor_name}, Time: ${appointment.appointment_time}")
                            }
                        } else {
                            _errorMessage.postValue("Appointments fetch failed: ${jsonResponse.getString("message")}")
                        }
                    } catch (e: Exception) {
                        _errorMessage.postValue("Error parsing appointments data: ${e.message}")
                    }
                } else {
                    _errorMessage.postValue("Appointments fetch failed: ${response.message}")
                }
            }
        })
    }
}
