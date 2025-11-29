package com.example.bookingappteam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingappteam.databinding.BookingItemBinding

class BookingAdapter(private val bookings: MutableList<Booking>) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = BookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(bookings[position])
    }

    override fun getItemCount() = bookings.size

    fun getSelectedBookings(): List<Booking> {
        return bookings.filter { it.isSelected }
    }

    inner class BookingViewHolder(private val binding: BookingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(booking: Booking) {
            binding.tvRoomName.text = booking.roomName
            binding.tvBookingDate.text = booking.date
            binding.tvBookingTime.text = booking.time
            binding.tvBookingPurpose.text = "Purpose: ${booking.purpose}"
            binding.tvNumberOfStudents.text = "Students: ${booking.numberOfStudents}"
            binding.tvBookedBy.text = "Booked By: ${booking.username}"
            itemView.setOnClickListener {
                booking.isSelected = !booking.isSelected
                itemView.setBackgroundResource(if (booking.isSelected) R.color.selected_booking else R.drawable.rounded_corner_background)
            }
        }
    }
}