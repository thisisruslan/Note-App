package uz.gita.mynotesapplication.ui.screen

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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
import uz.gita.mynotesapplication.databinding.ScreenAddNoteBinding
import uz.gita.mynotesapplication.ui.viewmodel.AddNoteViewModel


class AddNoteScreen : Fragment() {
    private var _binding: ScreenAddNoteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddNoteViewModel by viewModels()
    private var isBtnSavePressed = false
    private var isPinned = false
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
    ): View? {
        _binding = ScreenAddNoteBinding.inflate(inflater, container, false)
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

        binding.buttonColorPicker.setOnClickListener {
            binding.editor.setTextColor(lastTextColor)
            MaterialColorPickerDialog
                .Builder(requireActivity()) // Pass Activity Instance
                .setColorShape(ColorShape.SQAURE) // Or ColorShape.CIRCLE
                .setColorSwatch(ColorSwatch._300) // Default ColorSwatch._500
                .setNegativeButton("default color")
                .setDefaultColor(lastTextColor)  // Pass Default Color
                .setColorListener { color, colorHex ->
                    lastTextColor = color
                    binding.editor.setTextColor(color)
                }
                .show()

            binding.editor.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
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

        binding.buttonPin.setOnClickListener {
            if (!isPinned) {
                binding.buttonPin.setImageResource(R.drawable.ic_pin_red)
            } else {
                binding.buttonPin.setImageResource(R.drawable.ic_pin)
            }
            isPinned = !isPinned
        }

        binding.buttonSave.setOnClickListener {
            isBtnSavePressed = true
            if (binding.editTitle.text.toString().isEmpty()) {
                binding.editTitle.setText("No title")
            }
            if (binding.editor.html.isNullOrEmpty()) {
                binding.editor.html = ""
            }
            viewModel.addNote(
                NoteEntity(
                    0,
                    binding.editTitle.text.toString(),
                    binding.editor.html.toString(),
                    System.currentTimeMillis(),
                    isPinned
                )
            )
            findNavController().popBackStack()
        }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isBtnSavePressed) return
        if (binding.editTitle.text.toString().isEmpty()) {
            binding.editTitle.setText("No title")
        }
        if (binding.editor.html.isNullOrEmpty()) {
            binding.editor.html = ""
        }
        viewModel.addNote(
            NoteEntity(
                0,
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