package uz.gita.mynotesapplication.ui.screen

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import jp.wasabeef.richeditor.RichEditor
import uz.gita.mynotesapplication.R
import uz.gita.mynotesapplication.data.entity.NoteEntity
import uz.gita.mynotesapplication.databinding.ScreenInfoNoteBinding
import uz.gita.mynotesapplication.ui.viewmodel.InfoNoteViewModel

class InfoEditNoteScreen : Fragment() {
    private var _binding: ScreenInfoNoteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InfoNoteViewModel by viewModels()
    private var isPinned = false
    private var dataID = 0
    private var isBold = false
    private var isItalic = false
    private var isUnderline = false
    private var isStrikeThrough = false
    private lateinit var mEditor: RichEditor
    private var lastBackgroundColor = R.color.white
    private var lastTextColor = R.color.black

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenInfoNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mEditor = binding.editor
        binding.editor.setPadding(16, 10, 16, 10)
        binding.editor.setPlaceholder("Insert note text here...")
        mEditor.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.bottonLine.visibility = View.VISIBLE
            } else {
                binding.bottonLine.visibility = View.GONE
            }
        }
        binding.editor.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorBackground
            )
        )

        arguments?.let {
            val data = it.getSerializable("data") as NoteEntity
            dataID = data.id
            isPinned = if (data.isPinned) {
                binding.buttonPin.setImageResource(R.drawable.ic_pin_red)
                true
            } else {
                binding.buttonPin.setImageResource(R.drawable.ic_pin)
                false
            }

            binding.buttonPin.setOnClickListener {
                data.isPinned = !data.isPinned
            }

            if (data.title == "No title") {
                binding.editTitle.hint = data.title
            } else binding.editTitle.setText(data.title)
            binding.editor.html = data.message

            binding.buttonDelete.setOnClickListener {
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setMessage("This note will be deleted!")
                    .setPositiveButton("OK") { _: DialogInterface?, _: Int ->
                        viewModel.deleteNote(data)
                        findNavController().popBackStack()
                    }
                    .setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }
                    .create()
                alertDialog.show()
            }

            binding.buttonSave.setOnClickListener {
                viewModel.editNote(
                    NoteEntity(
                        data.id,
                        binding.editTitle.text.toString(),
                        binding.editor.html.toString(),
                        System.currentTimeMillis(),
                        data.isPinned
                    )
                )
                findNavController().popBackStack()
            }

            binding.buttonPin.setOnClickListener {
                if (!isPinned) {
                    binding.buttonPin.setImageResource(R.drawable.ic_pin_red)
                } else {
                    binding.buttonPin.setImageResource(R.drawable.ic_pin)
                }
                isPinned = !isPinned
                data.isPinned = !data.isPinned
            }

            binding.buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }

        }




        binding.buttonTextStyleBold.setOnClickListener {
            isBold = !isBold
            binding.editor.setBold()
        }

        binding.buttonTextStyleItalic.setOnClickListener {
            isItalic = !isItalic
            binding.editor.setItalic()
        }

        binding.buttonTextStyleunderline.setOnClickListener {
            isUnderline = !isUnderline
            binding.editor.setUnderline()
        }

        binding.buttonStrikeThrough.setOnClickListener {
            isStrikeThrough = !isStrikeThrough
            mEditor.setStrikeThrough()
        }


        binding.buttonUndo.setOnClickListener { mEditor.undo() }
        binding.buttonRedo.setOnClickListener { mEditor.redo() }

        binding.buttonColorPicker.setOnClickListener {
            binding.editor.setTextColor(lastTextColor)
            MaterialColorPickerDialog
                .Builder(requireActivity()) // Pass Activity Instance
                .setColorShape(ColorShape.SQAURE) // Or ColorShape.CIRCLE
                .setColorSwatch(ColorSwatch._300) // Default ColorSwatch._500
                .setDefaultColor(lastTextColor)// Pass Default Color
                .setNegativeButton("default color")
                .setColorListener { color, colorHex ->
                    lastTextColor = color
                    binding.editor.setTextColor(color)
                }
                .show()

            binding.editor.setTextColor(R.color.black)
        }

        binding.buttonBackgroundText.setOnClickListener {
            MaterialColorPickerDialog
                .Builder(requireContext())              // Pass Activity Instance
                .setTitle("Highlight Color")                // Default "Choose Color"
                .setColorShape(ColorShape.SQAURE)    // Default ColorShape.CIRCLE
                .setColorSwatch(ColorSwatch._300)    // Default ColorSwatch._500
                .setDefaultColor(lastBackgroundColor)
                .setNegativeButton("default color")
                .setColorListener { color, colorHex ->
                    lastBackgroundColor = color
                    binding.editor.setTextBackgroundColor(color)
                }
                .show()

            binding.editor.setTextBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
        }


    }

    override fun onPause() {
        super.onPause()
        viewModel.editNote(
            NoteEntity(
                dataID,
                binding.editTitle.text.toString(),
                binding.editor.html.toString(),
                System.currentTimeMillis(),
                isPinned
            )
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}