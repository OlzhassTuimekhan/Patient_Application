package kz.olzhass.kolesa.ui.profile

import LoadingDialogFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.olzhass.kolesa.GlobalData
import kz.olzhass.kolesa.R
import kz.olzhass.kolesa.databinding.FragmentProfileBinding
import kz.olzhass.kolesa.ui.calendar.CalendarFragment
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONObject

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    val client = OkHttpClient()
    private var loadingDialog: LoadingDialogFragment? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        // Получаем userId из SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)  // Получаем userId, -1 - значение по умолчанию, если его нет

//        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
//            val result = loadDataFromDatabase() // Долгая операция
//            withContext(Dispatchers.Main) {
//                updateUI(result)
//            }
//        }
        if (userId != -1) {

            fetchProfile(userId)
        } else {
            // Ошибка: userId не найден, нужно попросить пользователя войти заново
            binding.tvErrorMessage.text = "User ID not found"
            binding.tvErrorMessage.visibility = View.VISIBLE
        }
        // 1. Читаем сохранённый индекс изображения
        val savedIndex = sharedPreferences.getInt("profile_image_index_$userId", -1)

        // 2. Если индекс уже есть, используем его. Если нет — создаём новый.
        val imageIndex = if (savedIndex != -1) {
            savedIndex
        } else {
            val newIndex = (0..7).random()
            sharedPreferences.edit().putInt("profile_image_index_$userId", newIndex).apply()
            newIndex
        }

        // 3. Сохраняем в глобальные данные
        GlobalData.randomImageIndex = imageIndex

        // 4. Устанавливаем изображение
        setProfileImage(imageIndex)

//        with(binding) {
//            tvMyAppointments.setOnClickListener {
//                navView.selectedItemId = R.id.navigation_calendar
//            }
//        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchProfile(userId: Int) {
        showLoading()
        if (userId == -1) {
            binding.tvErrorMessage.text = "User ID is not available"
            binding.tvErrorMessage.visibility = View.VISIBLE
            return
        }

        val url = "http://10.0.2.2:3000/profile/$userId"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requireActivity().runOnUiThread {
                    hideLoading()
                    binding.tvErrorMessage.text = "Profile fetch failed: ${e.message}"
                    binding.tvErrorMessage.visibility = View.VISIBLE
                }
            }

            override fun onResponse(call: Call, response: Response) {

                if (!isAdded || activity == null) return


                requireActivity().runOnUiThread {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val jsonResponse = JSONObject(responseBody)
                        if (jsonResponse.getBoolean("success")) {
                            hideLoading()
                            val profile = jsonResponse.getJSONObject("profile")
                            binding.tvName.text = profile.getString("name")
                            binding.tvNumber.text = profile.getString("phonenumber")
                            binding.tvLocation.text = profile.getString("location")
                        } else {
                            hideLoading()
                            binding.tvErrorMessage.text = "Profile fetch failed: ${jsonResponse.getString("message")}"
                            binding.tvErrorMessage.visibility = View.VISIBLE
                        }
                    } else {
                        hideLoading()
                        binding.tvErrorMessage.text = "Profile fetch failed: ${response.message}"
                        binding.tvErrorMessage.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
    private fun setProfileImage(index: Int) {
        val imageResources = listOf(
            R.drawable.image_1,
            R.drawable.image_2,
            R.drawable.image_3,
            R.drawable.image_4,
            R.drawable.image_5,
            R.drawable.image_6,
            R.drawable.image_7,
            R.drawable.image_8,
        )

        binding.imageProfile.setImageResource(imageResources[index])
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


}