//import android.content.Context
//import android.content.SharedPreferences
//import android.content.res.Configuration
//import androidx.preference.PreferenceManager
//import java.util.Locale
//
//object LocaleHelper {
//    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
//
//    // Сохраняем выбранный язык и обновляем конфигурацию
//    fun setLocale(context: Context, language: String): Context {
//        persist(context, language)
//        return updateResources(context, language)
//    }
//
//    // Получаем сохранённый язык или язык по умолчанию
//    fun getLanguage(context: Context): String {
//        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//        return prefs.getString(SELECTED_LANGUAGE, Locale.getDefault().language) ?: Locale.getDefault().language
//    }
//
//    // Обновляем context с новой локалью
//    fun updateResources(context: Context, language: String): Context {
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//        val config = Configuration(context.resources.configuration)
//        config.setLocale(locale)
//        return context.createConfigurationContext(config)
//    }
//
//    private fun persist(context: Context, language: String) {
//        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//        prefs.edit().putString(SELECTED_LANGUAGE, language).apply()
//    }
//}
