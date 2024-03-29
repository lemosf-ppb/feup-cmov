﻿using SkiaSharp;

namespace WeatherApp.Models
{
    public class GraphEntry
    {
        public GraphEntry(float temperature, string label, string temperatureLabel, SKBitmap icon)
        {
            Temperature = temperature;
            Label = label;
            TemperatureLabel = temperatureLabel;
            Icon = icon;
        }

        public string Label { get; set; }
        public string TemperatureLabel { get; set; }
        public float Temperature { get; set; }

        public SKBitmap Icon { get; set; }
    }
}