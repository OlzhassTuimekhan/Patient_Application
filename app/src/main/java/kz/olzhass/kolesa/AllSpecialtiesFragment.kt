package kz.olzhass.kolesa

import LoadingDialogFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import kz.olzhass.kolesa.databinding.FragmentSpecificDoctorsBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class AllSpecialtiesFragment : Fragment() {

    private var _binding: FragmentSpecificDoctorsBinding? = null
    private val binding get() = _binding!!
    private lateinit var specialtiesAdapter: SpecialtiesAdapter
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage
    private var loadingDialog: LoadingDialogFragment? = null
    private lateinit var specialtiesList: List<Specialty>


    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpecificDoctorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        specialtiesList = mutableListOf()

        specialtiesAdapter = SpecialtiesAdapter(specialtiesList) { specialty ->
            // Обработка нажатия, например, передаем id в другой фрагмент
            showDoctorsFragment(specialty.id)
        }

        binding.recyclerViewSpecialties.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = specialtiesAdapter
        }

        // Загружаем список специальностей
        fetchSpecialties()
    }

    private fun showDoctorsFragment(specialtyId: Int) {
        val fragment = DoctorsBySpecialtyFragment.newInstance(specialtyId)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun fetchSpecialties() {
        showLoading()

        val url = "http://${GlobalData.ip}:3000/specialties"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                hideLoading()
                // Обработка ошибок
                _errorMessage.postValue("Specialties fetch failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                hideLoading()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    try {
                        val jsonResponse = JSONObject(responseBody ?: "{}")
                        if (jsonResponse.getBoolean("success")) {
                            val specialtiesArray = jsonResponse.getJSONArray("specialties")
                            val specialtiesList = mutableListOf<Specialty>()
                            for (i in 0 until specialtiesArray.length()) {
                                val specialtyJson = specialtiesArray.getJSONObject(i)
                                val specialty = Specialty(
                                    id = specialtyJson.getInt("id"),
                                    name = specialtyJson.getString("name")
                                )
                                specialtiesList.add(specialty)
                            }

                            specialtiesAdapter.updateData(specialtiesList)
                        } else {
                            _errorMessage.postValue("Specialties fetch failed: " + jsonResponse.getString("message"))
                        }
                    } catch (e: Exception) {
                        _errorMessage.postValue("Error parsing specialties data: ${e.message}")
                    }
                } else {
                    _errorMessage.postValue("Specialties fetch failed: ${response.message}")
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