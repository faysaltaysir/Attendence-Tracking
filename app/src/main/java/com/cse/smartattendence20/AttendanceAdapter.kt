package com.cse.smarty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AttendanceAdapter(private val attendanceList: List<Pair<String, String>>) :
    RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    inner class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        private val tvStatus: TextView = itemView.findViewById(R.id.tv_status)

        fun bind(date: String, status: String) {
            tvDate.text = "Date: $date"
            tvStatus.text = "Status: $status"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_attendance, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val (date, status) = attendanceList[position]
        holder.bind(date, status)
    }

    override fun getItemCount(): Int {
        return attendanceList.size
    }
}