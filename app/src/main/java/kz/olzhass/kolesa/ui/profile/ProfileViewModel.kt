package kz.olzhass.kolesa.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.olzhass.kolesa.GlobalData
import kz.olzhass.kolesa.ProfileData
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class ProfileViewModel : ViewModel() {

    private val _profileData = MutableLiveData<ProfileData?>()
    val profileData: LiveData<ProfileData?> get() = _profileData

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    private val client = OkHttpClient()


    fun fetchProfile(userId: Int) {
        if (userId == -1) {
            _errorMessage.postValue("User ID is not available")
            return
        }

        _isLoading.postValue(true)

        val url = "http://${GlobalData.ip}:3000/profile/$userId"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                _isLoading.postValue(false)
                _errorMessage.postValue("Profile fetch failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    try {
                        val jsonResponse = JSONObject(responseBody ?: "{}")
                        if (jsonResponse.getBoolean("success")) {
                            val profileJson = jsonResponse.getJSONObject("profile")
                            val profile = ProfileData(
                                name = profileJson.getString("name"),
                                phoneNumber = profileJson.getString("phonenumber"),
                                location = profileJson.getString("location")
                            )
                            _profileData.postValue(profile)
                        } else {
                            _errorMessage.postValue("Profile fetch failed: ${jsonResponse.getString("message")}")
                        }
                    } catch (e: Exception) {
                        _errorMessage.postValue("Error parsing profile data: ${e.message}")
                    }
                } else {
                    _errorMessage.postValue("Profile fetch failed: ${response.message}")
                }
            }
        })
    }
}
