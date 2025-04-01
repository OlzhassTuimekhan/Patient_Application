package kz.olzhass.kolesa.ui.doctors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kz.olzhass.kolesa.R
import kz.olzhass.kolesa.TabsAdapter
import kz.olzhass.kolesa.databinding.FragmentDoctorsBinding
import kz.olzhass.kolesa.databinding.FragmentProfileBinding

class DoctorsFragment : Fragment() {

private var _binding: FragmentDoctorsBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
      return inflater.inflate(R.layout.fragment_doctors, container, false)
  }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        // Создаём адаптер, который будет возвращать вложенные фрагменты
        val adapter = TabsAdapter(this)
        viewPager.adapter = adapter

        // Привязываем TabLayout к ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Specific"
                1 -> tab.text = "All"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}