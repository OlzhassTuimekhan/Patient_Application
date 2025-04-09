package kz.olzhass.kolesa.ui.calendar

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class CalendarTabsAdapter(
    fragment: Fragment,
    private val viewModel: CalendarViewModel // Получаем экземпляр ViewModel
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3 // Количество вкладок

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SearchFragment()
            1 -> ActivesFragment()
            2 -> PreviousFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
