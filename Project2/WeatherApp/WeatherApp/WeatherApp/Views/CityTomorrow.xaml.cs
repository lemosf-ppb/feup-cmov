using System;
using System.Collections.Generic;
using System.Globalization;
using Microcharts;
using SkiaSharp;
using WeatherApp.Models;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using Entry = Xamarin.Forms.Entry;

namespace WeatherApp.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CityTomorrow : ContentPage
    {
        private List<WeatherByHour> WeatherByHours { get; set; }
        public CityTomorrow(City city, DateTime tomorrow)
        {
            InitializeComponent();

            CityName.Text = city.Name;
            Date.Text = $"{tomorrow:dddd, MMMM d, yyyy}";
            
            WeatherByHours = city.WeatherList.WeatherByDays[1];
            
            Temperature.Text = WeatherByHours[0].Temp.ToString(CultureInfo.InvariantCulture);

            var i = 0;
            var minTemp = 100.0;
            var maxTemp = -100.0;
            
            var maxRain = 0;
            var minRain = 100;
            
            var maxHumidity = 0;
            var minHumidity = 100;
            
            var maxCloudiness = 0;
            var minCloudiness = 100;
            
            var maxPressure = 0.0;
            var minPressure = 10000.0;
            
            var maxWindSpeed = 0.0;
            var minWindSpeed = 10000.0;

            var entries = new ChartEntry[WeatherByHours.Count];
            
            foreach (var weather in WeatherByHours)
            {
                var name = "Temp" + i;
                if (FindByName(name) is Label tempLabel)
                {
                    var temp = weather.Temp;
                    var tempString = temp.ToString(CultureInfo.InvariantCulture) + "°C";
                    tempLabel.Text = tempString;

                    var date = weather.dt_txt;
                    var time = date.Substring(11, 2);
                    var entry = new ChartEntry((float) temp)
                    {
                        Label = time +"h",
                        ValueLabel = tempString,
                        Color = SKColors.White
                    };
                    entries[i] = entry;
                }

                //TEMP
                minTemp = UpdateMin(weather.Temp_min, minTemp);
                maxTemp = UpdateMax(weather.Temp_max, maxTemp);
                
                //WIND
                minWindSpeed = UpdateMin(weather.WindSpeed, minWindSpeed);
                maxWindSpeed = UpdateMax(weather.WindSpeed, maxWindSpeed);
                
                //PRESSURE
                minPressure = UpdateMin(weather.Pressure, minPressure);
                maxPressure = UpdateMax(weather.Pressure, maxPressure);
                
                //HUMIDITY
                minHumidity = UpdateMin(weather.Humidity, minHumidity);
                maxHumidity = UpdateMax(weather.Humidity, maxHumidity);
                
                //CLOUDINESS
                minCloudiness = UpdateMin(weather.Cloudiness, minCloudiness);
                maxCloudiness = UpdateMax(weather.Cloudiness, maxCloudiness);
                
                //PRECIPITATION
                minRain = UpdateMin(weather.Rain, minRain);
                maxRain = UpdateMax(weather.Rain, maxRain);

                i++;
            }

            MaxMinCloudiness.Text = minCloudiness + "/" + maxCloudiness + "%";
            
            MaxMinHumidity.Text = minHumidity + "/" + maxHumidity + "%";
            
            MaxMinPrecipitation.Text = minRain + "/" + maxRain + "%";

            MaxMinPressure.Text = minPressure + "/" + maxPressure + " hpa";

            MaxMinWind.Text = minWindSpeed + "/" + maxWindSpeed + " m/s";

            
            var chart = new LineChart()
            {
                Entries = entries,
                LineMode = LineMode.Straight,
                PointMode = PointMode.Circle,
                PointSize = 15,
                LineSize = 3,
                BackgroundColor = SKColors.Transparent,
                LabelTextSize = 50,
                LabelColor = SKColors.White,
                LabelOrientation = Orientation.Horizontal
            };

            ChartView.Chart = chart;

        }

        private static int UpdateMin(int value, int min)
        {
            if (value < min)
            {
                min = value;
            }

            return min;
        }
        
        private static double UpdateMin(double value, double min)
        {
            if (value < min)
            {
                min = value;
            }

            return min;
        }

        private static int UpdateMax(int value, int max)
        {
            if (value > max)
            {
                max = value;
            }

            return max;
        }
        
        private static double UpdateMax(double value, double max)
        {
            if (value > max)
            {
                max = value;
            }

            return max;
        }

        async void OnFollowBtnClicked(object sender, EventArgs args)
        {
            //Remove city from list
        }
    }
}