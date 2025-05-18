package com.example.inventorymanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class InventoryAdapter(
    private var items: List<InventoryItem>,
    private val onEditClick: (InventoryItem) -> Unit,
    private val onDeleteClick: (InventoryItem) -> Unit
) : RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {

    inner class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDeleteRecord)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.inventory_item, parent, false)
        return InventoryViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val item = items[position]

        holder.tvName.text = item.name
        holder.tvQuantity.text = item.quantity.toString()
        holder.tvPrice.text = item.price.toString()

        holder.btnEdit.setOnClickListener {
            onEditClick(item)  // Pass whole item including id
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(item)  // Pass whole item including id
        }
    }

    fun updateList(newItems: List<InventoryItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
