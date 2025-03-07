package kz.olzhass.kolesa.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kz.olzhass.kolesa.R
import kz.olzhass.kolesa.databinding.FragmentCalendarBinding
import kz.olzhass.kolesa.databinding.FragmentProfileBinding

class CalendarFragment : Fragment() {

private var _binding: FragmentCalendarBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        return binding.root
    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}