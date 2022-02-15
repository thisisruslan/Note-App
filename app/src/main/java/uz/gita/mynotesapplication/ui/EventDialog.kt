package uz.gita.mynotesapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.gita.mynotesapplication.databinding.DialogEventBinding

class EventDialog : BottomSheetDialogFragment() {
    private var _binding: DialogEventBinding? = null
    private val binding get() = _binding!!
    private var pinListener: (() -> Unit)? = null
    private var shareListener: (() -> Unit)? = null
    private var deleteListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bottomSheetPin.setOnClickListener {
            pinListener?.invoke()
            dismiss()
        }

        binding.bottomSheetShare.setOnClickListener {
            shareListener?.invoke()
            dismiss()
        }

        binding.bottomSheetDelete.setOnClickListener {
            deleteListener?.invoke()
            dismiss()
        }
    }


    fun setPinListener(f: () -> Unit) {
        pinListener = f
    }

    fun setShareListener(f: () -> Unit) {
        shareListener = f
    }

    fun setDeleteListener(f: () -> Unit) {
        deleteListener = f
    }

}