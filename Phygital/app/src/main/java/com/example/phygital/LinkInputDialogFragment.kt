package com.example.phygital

import androidx.fragment.app.DialogFragment
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class LinkInputDialogFragment : DialogFragment() {

    private var listener: LinkInputListener? = null

    fun setListener(l: LinkInputListener) {
        listener = l
    }

    interface LinkInputListener {
        fun onLinkSubmitted(url: String, description: String, editMode: Boolean = false, position: Int? = null)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_link_input, null)

        val etUrl = view.findViewById<EditText>(R.id.etLinkUrl)
        val etDesc = view.findViewById<EditText>(R.id.etLinkDesc)
        val btnSave = view.findViewById<Button>(R.id.btnSaveLink)

        builder.setView(view)

        val dialog = builder.create()

        btnSave.setOnClickListener {
            val url = etUrl.text.toString().trim()
            val desc = etDesc.text.toString().trim()

            if (url.isBlank() || !url.startsWith("http")) {
                etUrl.error = "Enter a valid URL (http/https)"
                return@setOnClickListener
            }

            listener?.onLinkSubmitted(url, desc, false, null)
            dialog.dismiss()
        }

        return dialog
    }
}
