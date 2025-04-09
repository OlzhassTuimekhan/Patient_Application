package kz.olzhass.kolesa.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kz.olzhass.kolesa.databinding.FragmentPreviousBinding
import kz.olzhass.kolesa.ui.appointment.Appointment
import kz.olzhass.kolesa.ui.appointment.AppointmentsAdapter


class PreviousFragment : Fragment() {

    private var _binding: FragmentPreviousBinding? = null
    private val binding get() = _binding!!
    private lateinit var appointments: MutableList<Appointment>
    private lateinit var adapter: AppointmentsAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPreviousBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appointments = getTestAppointments()
        Log.d("ActivesFragment", "Appointments size: ${appointments.size}")
        val adapter = AppointmentsAdapter(appointments)
        binding.recyclerViewPrevious.adapter = adapter
        binding.recyclerViewPrevious.layoutManager = LinearLayoutManager(context)
    }

    fun getTestAppointments(): MutableList<Appointment> {
        val appointments = mutableListOf<Appointment>()

        appointments.add(Appointment(
            appointment_id = 7,
            user_id = 34,
            doctor_id = 2,
            appointment_date = "2025-08-03T19:00:00.000Z",
            appointment_time = "18:46:00",
            appointment_phone = "87072793054",
            appointment_reason = "check-up",
            appointment_status = "Pending",
            appointment_created_at = "2025-04-08T13:46:38.242Z",
            doctor_name = "Сандыгаш Сайрамбек",
            doctor_phone = "87072793054",
            doctor_email = "a.alihan0106@gmail.com",
            doctor_created_at = "2025-03-24T05:49:40.963Z"
        ))

        appointments.add(Appointment(
            appointment_id = 9,
            user_id = 34,
            doctor_id = 3,
            appointment_date = "2025-04-08T19:00:00.000Z",
            appointment_time = "06:11:00",
            appointment_phone = "87070000001",
            appointment_reason = "teeth",
            appointment_status = "Pending",
            appointment_created_at = "2025-04-09T01:11:58.444Z",
            doctor_name = "Балдана Мараткызы",
            doctor_phone = "87070000001",
            doctor_email = "b.marat@example.com",
            doctor_created_at = "2025-03-24T05:49:40.963Z"
        ))

        appointments.add(Appointment(
            appointment_id = 10,
            user_id = 34,
            doctor_id = 3,
            appointment_date = "2025-04-08T19:00:00.000Z",
            appointment_time = "08:02:00",
            appointment_phone = "87070000001",
            appointment_reason = "ACTUALLY ITS WORKING BBBBB",
            appointment_status = "Pending",
            appointment_created_at = "2025-04-09T03:03:17.629Z",
            doctor_name = "Балдана Мараткызы",
            doctor_phone = "87070000001",
            doctor_email = "b.marat@example.com",
            doctor_created_at = "2025-03-24T05:49:40.963Z"
        ))

        return appointments
    }
}