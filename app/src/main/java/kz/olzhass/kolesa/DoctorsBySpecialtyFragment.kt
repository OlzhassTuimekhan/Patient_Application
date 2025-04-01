package kz.olzhass.kolesa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import kz.olzhass.kolesa.databinding.FragmentDoctorsBySpecialtyBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class DoctorsBySpecialtyFragment : Fragment() {

    private var _binding: FragmentDoctorsBySpecialtyBinding? = null
    private val binding get() = _binding!!
    private lateinit var doctorsAdapter: DoctorAdapter

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage
    private val client = OkHttpClient()

    companion object {
        private const val ARG_SPECIALTY_ID = "specialty_id"

        fun newInstance(specialtyId: Int): DoctorsBySpecialtyFragment {
            val fragment = DoctorsBySpecialtyFragment()
            val args = Bundle()
            args.putInt(ARG_SPECIALTY_ID, specialtyId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorsBySpecialtyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val specialtyId = arguments?.getInt(ARG_SPECIALTY_ID) ?: 0
        doctorsAdapter = DoctorAdapter(emptyList())
        binding.recyclerViewDoctors.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = doctorsAdapter
        }

        fetchDoctors(specialtyId)
    }

    private fun fetchDoctors(specialtyId: Int) {
        val url = "http://${GlobalData.ip}:3000/doctors-by-specialty/$specialtyId"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                _errorMessage.postValue("Doctors fetch failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    try {
                        val jsonResponse = JSONObject(responseBody ?: "{}")
                        if (jsonResponse.getBoolean("success")) {
                            val doctorsArray = jsonResponse.getJSONArray("doctors")
                            val doctorsList = mutableListOf<Doctor>()
                            for (i in 0 until doctorsArray.length()) {
                                val doctorJson = doctorsArray.getJSONObject(i)
                                val doctor = Doctor(
                                    id = doctorJson.getInt("id"),
                                    nameSurname = doctorJson.getString("name"),
                                    phone = doctorJson.getString("phone"),
                                    specialties = doctorJson.getString("specialty"),
                                    services = doctorJson.getString("services") // Используем "services"
                                )
                                doctorsList.add(doctor)
                            }
                            doctorsAdapter.updateDoctors(doctorsList) // Обновляем данные в адаптере
                        } else {
                            _errorMessage.postValue("Doctors fetch failed: " + jsonResponse.getString("message"))
                        }
                    } catch (e: Exception) {
                        _errorMessage.postValue("Error parsing doctors data: ${e.message}")
                    }
                } else {
                    _errorMessage.postValue("Doctors fetch failed: ${response.message}")
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
