package kz.olzhass.kolesa

import LoadingDialogFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kz.olzhass.kolesa.databinding.FragmentAllDoctorsBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class AllDoctorsFragment : Fragment() {

    private var _binding: FragmentAllDoctorsBinding? = null
    private val binding get() = _binding!!
    private lateinit var doctorAdapter: DoctorAdapter

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _doctorsData = MutableLiveData<List<Doctor>>()
    val doctorsData: LiveData<List<Doctor>> get() = _doctorsData
    private var loadingDialog: LoadingDialogFragment? = null

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Важно: использовать правильный биндинг для нового XML
        _binding = FragmentAllDoctorsBinding.inflate(inflater, container, false)
        return binding.root // Это возвращает корневой элемент в разметке
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewDoctors.layoutManager = LinearLayoutManager(requireContext())
        doctorAdapter = DoctorAdapter(emptyList())
        binding.recyclerViewDoctors.adapter = doctorAdapter
        val searchView = binding.searchView1

        searchView.setOnClickListener {
            searchView.isIconified = false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterDoctors(newText)
                return true
            }
        })
        doctorsData.observe(viewLifecycleOwner) { doctorsList ->
            doctorAdapter.updateDoctors(doctorsList)
        }

        fetchDoctors()
    }

    fun filterDoctors(query: String?) {
        val filteredList = doctorsData.value?.filter { doctor ->
            val searchQuery = query?.toLowerCase() ?: ""
            doctor.nameSurname.toLowerCase().contains(searchQuery) ||
                    doctor.phone.contains(searchQuery)
        }
        doctorAdapter.updateDoctors(filteredList ?: emptyList())
    }

    fun fetchDoctors() {
        showLoading()

        // Формируем URL для запроса списка докторов
        val url = "http://${GlobalData.ip}:3000/doctors"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                hideLoading()
                _errorMessage.postValue("Doctors fetch failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                hideLoading()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    try {
                        // Преобразуем строку ответа в JSON-объект
                        val jsonResponse = JSONObject(responseBody ?: "{}")
                        if (jsonResponse.getBoolean("success")) {
                            val doctorsArray = jsonResponse.getJSONArray("doctors")
                            val doctorsList = mutableListOf<Doctor>()
                            // Парсим массив докторов
                            for (i in 0 until doctorsArray.length()) {
                                val doctorJson = doctorsArray.getJSONObject(i)
                                val doctor = Doctor(
                                    id = doctorJson.getInt("id"),
                                    nameSurname = doctorJson.getString("Name Surname"),
                                    phone = doctorJson.getString("phone"),
                                    specialties = doctorJson.getString("Specialities"),
                                    services = doctorJson.getString("Services")
                                )
                                doctorsList.add(doctor)
                            }

                            _doctorsData.postValue(doctorsList)
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

    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialogFragment()
            loadingDialog?.show(parentFragmentManager, "loading")
        }
    }

    fun hideLoading() {
        if (loadingDialog != null && loadingDialog?.isAdded == true) {
            loadingDialog?.dismissAllowingStateLoss()
            loadingDialog = null
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}