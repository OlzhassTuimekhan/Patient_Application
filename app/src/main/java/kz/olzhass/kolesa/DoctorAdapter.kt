package kz.olzhass.kolesa

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DoctorAdapter(private var doctors: List<Doctor>) :
    RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    inner class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDoctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
        val tvSpecialty: TextView = itemView.findViewById(R.id.tvSpecialty)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        val tvServices: TextView = itemView.findViewById(R.id.tvServices)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctors[position]
        holder.tvDoctorName.text = doctor.nameSurname ?: "Неизвестно"
        holder.tvSpecialty.text = doctor.specialties ?: "Не указана"
        holder.tvPhone.text = doctor.phone ?: "Нет телефона"
        holder.tvServices.text = doctor.services ?: "Нет услуг"
    }

    override fun getItemCount(): Int = doctors.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateDoctors(newDoctors: List<Doctor>) {
        Log.d("DoctorAdapter", "Updating doctors list: $newDoctors")
        doctors = newDoctors
        notifyDataSetChanged()
    }

}
