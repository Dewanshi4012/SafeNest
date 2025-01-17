package com.example.safenest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safenest.databinding.ActivityMainBinding
import com.example.safenest.databinding.FragmentHomeBinding

class InviteAdapter(private val listContacts:List<ContactModel>):RecyclerView.Adapter<InviteAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val item = inflator.inflate(R.layout.item_invite,parent,false)
        return ViewHolder(item)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listContacts[position]
        holder.name.text = item.name
    }
    override fun getItemCount(): Int {
        return listContacts.size
    }

    class ViewHolder(private val item: View):RecyclerView.ViewHolder(item) {
        val name = item.findViewById<TextView>(R.id.name)
    }
}