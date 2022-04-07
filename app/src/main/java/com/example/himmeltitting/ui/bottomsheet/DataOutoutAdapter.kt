package com.example.himmeltitting.ui.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.himmeltitting.databinding.DataoutputItemBinding
import com.example.himmeltitting.utils.airQualityImageCalculator


class DataOutoutAdapter(private val dataSet: List<ForecastData>, private val context: Context) :
    RecyclerView.Adapter<DataOutoutAdapter.ViewHolder>()  {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(binding: DataoutputItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val headerText = binding.header
        val cloudText = binding.cloudText
        val cloudImage = binding.cloudIcon
        val temperatureText = binding.temperatureText
        val rainText = binding.rainText
        val windText = binding.windText
        val airText = binding.airText
        val airImage = binding.airIcon

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = DataoutputItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.headerText.text = dataSet[position].header
        viewHolder.cloudText.text = dataSet[position].cloudCover
        viewHolder.temperatureText.text = dataSet[position].temperature
        viewHolder.rainText.text = dataSet[position].precipitation6Hours
        viewHolder.windText.text = dataSet[position].wind_speed
        viewHolder.airText.text = dataSet[position].airQuality
        val airDrawable = airQualityImageCalculator(dataSet[position].airQuality)

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}