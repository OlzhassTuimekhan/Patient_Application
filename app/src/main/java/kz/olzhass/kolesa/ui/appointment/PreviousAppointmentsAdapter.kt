package kz.olzhass.kolesa.ui.appointment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.olzhass.kolesa.R

class PreviousAppointmentsAdapter(private val appointments: List<Appointment>) : RecyclerView.Adapter<PreviousAppointmentsAdapter.AppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_previous_appointment, parent, false) // новый layout для прошедших встреч
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]

        holder.doctorName.text = appointment.doctor_name
        holder.appointmentDate.text = appointment.appointment_date
        holder.appointmentTime.text = appointment.appointment_time
        holder.appointmentRemaining.text = appointment.appointment_status
        holder.appointmentPhone.text = appointment.appointment_phone
        holder.appointmentEmail.text = appointment.appointment_reason
        holder.appointmentCreatedAt.text = appointment.appointment_created_at
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    inner class AppointmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val doctorName: TextView = view.findViewById(R.id.tvDoctorName)
        val appointmentDate: TextView = view.findViewById(R.id.tvDate)
        val appointmentTime: TextView = view.findViewById(R.id.tvTime)
        val appointmentRemaining: TextView = view.findViewById(R.id.tvRemaining)
        val appointmentPhone: TextView = view.findViewById(R.id.tvPhone)
        val appointmentEmail: TextView = view.findViewById(R.id.tvEmail)
        val appointmentCreatedAt: TextView = view.findViewById(R.id.tvCreatedAt)
    }
}
