package com.example.quiz

import android.content.Intent
import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.quiz.databinding.FragmentSecondBinding
import java.io.IOException
import java.io.InputStream

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private var numberOfButtons= 2
    private  var selectedCountinent :String ="Europe"
    private var correctCountryName: String = ""
    private var userScore: Int = 0
    private var currentQuestion: Int = 1
    private var totalQuestions: Int = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshImageView()
        setButtonListeners()
        val args = arguments
        if (args != null) {
            numberOfButtons = args.getInt("selectedButtons", 2)
            selectedCountinent = args.getString("selectedContinent" , "Europe")
            refreshImageView()

        }
        when (numberOfButtons) {
            2 -> {
                binding.bAnswer3.visibility = View.GONE
                binding.bAnswer4.visibility = View.GONE
                binding.bAnswer5.visibility = View.GONE
                binding.bAnswer6.visibility = View.GONE
                binding.bAnswer7.visibility = View.GONE
                binding.bAnswer8.visibility = View.GONE
            }
            4 -> {
                binding.bAnswer5.visibility = View.GONE
                binding.bAnswer6.visibility = View.GONE
                binding.bAnswer7.visibility = View.GONE
                binding.bAnswer8.visibility = View.GONE
            }
            6 -> {
                binding.bAnswer7.visibility = View.GONE
                binding.bAnswer8.visibility = View.GONE
            }
        }

        setButtonListeners()
        val countryNames = getRandomCountryNames(selectedCountinent)
        setButtonTextsRandomly(countryNames)
        updateQuestionNumber()
    }

    private fun refreshImageView() {
        val (drawable, correctCountryName) = displayRandomImageFromAssets()
        binding.drapeauxImageView.setImageDrawable(drawable)
        this.correctCountryName = correctCountryName ?: ""
    }

    private fun displayRandomImageFromAssets(): Pair<Drawable?, String?> {
        try {
            val assetManager: AssetManager = requireContext().assets
            val files: Array<String>? = assetManager.list(selectedCountinent)
            val imageFiles = files?.filter { it.endsWith(".png") }
            val randomImageFileName = imageFiles?.random()
            val correctCountryName = randomImageFileName?.removeSuffix(".png") ?: ""
            val inputStream: InputStream? = randomImageFileName?.let { assetManager.open("$selectedCountinent/$it") }
            val drawable = Drawable.createFromStream(inputStream, null)
            return Pair(drawable, correctCountryName)
        } catch (e: IOException) {
            e.printStackTrace()
            return Pair(null, null)
        }
    }

    private fun getRandomCountryNames(selectedContinent: String): List<String> {
        try {
            val assetManager: AssetManager = requireContext().assets
            val regions: Array<String> = resources.getStringArray(R.array.regions_list)

            val filesCorrectAnswer: Array<String>? = assetManager.list(selectedContinent)
            val imageFilesCorrectAnswer = filesCorrectAnswer?.filter { it.endsWith(".png") }
            val countryNameCorrectAnswer = imageFilesCorrectAnswer?.random()?.removeSuffix(".png") ?: ""

            val otherRegions = regions.filter { it != selectedContinent }
            val countryNamesOptions = mutableListOf<String>()

            for (region in otherRegions) {
                val filesOptions: Array<String>? = assetManager.list(region)
                val imageFilesOptions = filesOptions?.filter { it.endsWith(".png") }
                val countryNamesInRegion = imageFilesOptions?.map { it.removeSuffix(".png") }?.shuffled() ?: emptyList()
                countryNamesOptions.addAll(countryNamesInRegion)
            }

            countryNamesOptions.add(countryNameCorrectAnswer)

            return countryNamesOptions.shuffled()
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        }
    }


    private fun setButtonTextsRandomly(countryNames: List<String>) {
        val shuffledCountryNames = countryNames.shuffled()
        val correctButtonIndex = (0 until numberOfButtons).random()
        val buttonList = listOf(
            binding.bAnswer1, binding.bAnswer2, binding.bAnswer3, binding.bAnswer4,
            binding.bAnswer5, binding.bAnswer6, binding.bAnswer7, binding.bAnswer8
        )

        for (i in 0 until numberOfButtons) {
            buttonList[i].text = shuffledCountryNames[i].substringAfterLast('-')
        }

        buttonList[correctButtonIndex].text = correctCountryName
    }

    private fun checkAnswer(selectedCountry: String) {
        if (selectedCountry == correctCountryName) {
            userScore++
            binding.reponseTextView.text = "Correct! "
            val correctColor = resources.getColor(R.color.reponse_correcte, requireContext().theme)
            binding.reponseTextView.setTextColor(correctColor)
        } else {
            binding.reponseTextView.text = "Incorrect!"
            val inCorrectColor = resources.getColor(R.color.reponse_incorrecte, requireContext().theme)
            binding.reponseTextView.setTextColor(inCorrectColor)
        }

        refreshImageView()

        currentQuestion++
        updateQuestionNumber()

        if (currentQuestion <= totalQuestions) {
            val countryNames = getRandomCountryNames(selectedCountinent)
            setButtonTextsRandomly(countryNames)
        } else {
            showFinalScore()
            navigateToMainActivity()
        }
    }


    private fun setButtonListeners() {
        binding.bAnswer1.setOnClickListener { checkAnswer(binding.bAnswer1.text.toString()) }
        binding.bAnswer2.setOnClickListener { checkAnswer(binding.bAnswer2.text.toString()) }
        binding.bAnswer3.setOnClickListener { checkAnswer(binding.bAnswer3.text.toString()) }
        binding.bAnswer4.setOnClickListener { checkAnswer(binding.bAnswer4.text.toString()) }
        binding.bAnswer5.setOnClickListener { checkAnswer(binding.bAnswer5.text.toString()) }
        binding.bAnswer6.setOnClickListener { checkAnswer(binding.bAnswer6.text.toString()) }
        binding.bAnswer7.setOnClickListener { checkAnswer(binding.bAnswer7.text.toString()) }
        binding.bAnswer8.setOnClickListener { checkAnswer(binding.bAnswer8.text.toString()) }
    }

    private fun updateQuestionNumber() {
        val questionText = getString(R.string.question, currentQuestion, totalQuestions)
        binding.numeroQuestionTextView.text = questionText
    }

    private fun showFinalScore() {
        val message = when {
            userScore == totalQuestions -> "Congratulations! "
            userScore >= totalQuestions / 2 -> "Good job! "
            else -> "Keep practicing! "
        }

        Toast.makeText(requireContext(), "Final Score: $userScore/$totalQuestions\n$message", Toast.LENGTH_LONG).show()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
