package com.asterisk.tipper

import android.animation.ArgbEvaluator
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.asterisk.tipper.Constants.INITIAL_TIP_PERCENT
import com.asterisk.tipper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.seekBar.progress = INITIAL_TIP_PERCENT
        binding.tvPercentageLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvPercentageLabel.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                computeTipAndTotal()
            }
        })
    }

    private fun updateTipDescription(progress: Int) {
        val tipDesc = when(progress) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }

        binding.tipDescription.text = tipDesc

        val color = ArgbEvaluator().evaluate(
            progress.toFloat() / binding.seekBar.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int

        binding.tipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if (binding.etAmount.text.isEmpty()) {
            binding.tvTipAmount.text = ""
            binding.tvTotalAmount.text = ""
            return
        }

        // 1. Get the value of the amount and percentage
        val inputAmount = binding.etAmount.text.toString().toDouble()
        val tipPercent = binding.seekBar.progress
        // 2. Compute the tip and total
            val tipAmount = inputAmount * tipPercent / 100
            val totalAmount = inputAmount + tipAmount
        // 3. Update the UI
        binding.tvTipAmount.text = "%.2f".format(tipAmount)
        binding.tvTotalAmount.text ="%.2f".format(totalAmount)
    }
}