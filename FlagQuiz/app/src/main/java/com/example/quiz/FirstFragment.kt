package com.example.quiz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.quiz.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
   private val bundle = Bundle()
    private val bundle2 = Bundle()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.radioButton2.setOnClickListener {
            onRadioButtonClicked(it)
        }

        binding.radioButton4.setOnClickListener {
            onRadioButtonClicked(it)
        }

        binding.radioButton6.setOnClickListener {
            onRadioButtonClicked(it)
        }

        binding.radioButton8.setOnClickListener {
            onRadioButtonClicked(it)
        }

        binding.radioButtonAfrica.setOnClickListener {
            onRadioButtonClicked1(it)
        }
        binding.radioButtonAsia.setOnClickListener{
            onRadioButtonClicked1(it)
        }
        binding.radioButtonEurope.setOnClickListener{
            onRadioButtonClicked1(it)
        }
        binding.radioButtonNorthAmerica.setOnClickListener{
            onRadioButtonClicked1(it)
        }
        binding.radioButtonOceania.setOnClickListener{
            onRadioButtonClicked1(it)
        }
        binding.radioButtonSouthAmerica.setOnClickListener{
            onRadioButtonClicked1(it)
        }

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundleOf("selectedButtons" to bundle.getInt("selectedButtons"), "selectedContinent" to bundle2.getString("selectedContinent")))        }
    }
    fun onRadioButtonClicked1(view: View) {
        if (view is RadioButton) {
            val selectedContinent = when (view.id) {
                R.id.radioButtonAfrica -> "Africa"
                R.id.radioButtonAsia -> "Asia"
                R.id.radioButtonEurope -> "Europe"
                R.id.radioButtonNorthAmerica -> "North_America"
                R.id.radioButtonOceania -> "Oceania"
                R.id.radioButtonSouthAmerica -> "South_America"

                else -> "Africa"
            }

            bundle2.putString("selectedContinent", selectedContinent)




        }
    }
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val selectedButtons = when (view.id) {
                R.id.radioButton2 -> 2
                R.id.radioButton4 -> 4
                R.id.radioButton6 -> 6
                R.id.radioButton8 -> 8
                else -> 2
            }

            bundle.putInt("selectedButtons", selectedButtons)




        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
