package kz.olzhass.kolesa

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2 // Две вкладки

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SpecificDoctorsFragment()
            else -> AllDoctorsFragment()
        }
    }
}
