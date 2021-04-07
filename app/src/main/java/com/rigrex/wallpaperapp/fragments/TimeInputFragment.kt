package com.rigrex.wallpaperapp.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.databinding.FragmentTimeInputBinding

class TimeInputFragment : DialogFragment() {

    lateinit var binding: FragmentTimeInputBinding
    private var invoker: ((String) -> Unit)? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimeInputBinding.inflate(layoutInflater)
        return binding.root
    }

    fun setInv(invoker: ((String) -> Unit)) {
        this.invoker = invoker
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        this.invoker?.invoke(getSelected())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancel.setOnClickListener {
            var s = getSelected()
            if (s.isEmpty()) {
                binding.textInput.error = "Please Fill the field"
            } else {
                dismiss()
            }
        }
        binding.ok.setOnClickListener { dismiss() }
        init()
    }

    private fun init() {
        binding.hours.setOnClickListener {
            binding.hTemp.isSelected = true
            binding.mTemp.isSelected = false
        }
        binding.minutesSelector.setOnClickListener {
            binding.hTemp.isSelected = false
            binding.mTemp.isSelected = true
        }
    }

    private fun getSelected(): String {
        if (binding.textInput.text.isEmpty())
            return ""
        return "${binding.textInput.text} ${if (binding.hTemp.isSelected) "hrs" else "min"}"
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                TimeInputFragment().apply {
                    setStyle(STYLE_NORMAL, R.style.DialogTheme)
                    arguments = Bundle()
                }
    }
}