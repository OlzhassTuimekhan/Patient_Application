package kz.olzhass.kolesa.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kz.olzhass.kolesa.databinding.FragmentActivesBinding
import kz.olzhass.kolesa.ui.appointment.Appointment
import kz.olzhass.kolesa.ui.appointment.AppointmentsAdapter

class ActivesFragment : Fragment() {

    private var _binding: FragmentActivesBinding? = null
    private val binding get() = _binding!!

    // Инициализируем сразу пустым списком, чтобы избежать ошибки
    private var allAppointments: List<Appointment> = emptyList()

    private val calendarViewModel: CalendarViewModel by activityViewModels()
    private lateinit var adapter: AppointmentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActivesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Наблюдаем за активными встречами из ViewModel
        calendarViewModel.activeAppointments.observe(viewLifecycleOwner, Observer { appointmentsList ->
            if (appointmentsList != null) {
                // Обновляем мастер-лист
                allAppointments = appointmentsList.toMutableList()

                // Инициализируем адаптер с полным списком встреч
                adapter = AppointmentsAdapter(allAppointments.toMutableList())
                binding.recyclerViewActives.layoutManager = LinearLayoutManager(context)
                binding.recyclerViewActives.adapter = adapter
            }
        })

        // Реализация логики поиска
        binding.searchView1.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { filterAppointments(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterAppointments(it) }
                return true
            }
        })
    }

    // Функция фильтрации списка по имени врача, дате или телефону
    private fun filterAppointments(query: String) {
        val filteredList = allAppointments.filter { appointment ->
            appointment.doctor_name.contains(query, ignoreCase = true) ||
                    appointment.appointment_date.contains(query) ||
                    appointment.appointment_phone.contains(query)
        }
        adapter.updateList(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
