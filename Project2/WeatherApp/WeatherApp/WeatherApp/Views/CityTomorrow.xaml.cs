using System;
using System.Collections.Generic;
using System.Globalization;
using WeatherApp.Models;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

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
            
            foreach (var weather in WeatherByHours)
            {
                var name = "Temp" + i;
                if (this.FindByName(name) is Label tempLabel)
                {
                    tempLabel.Text = weather.Temp.ToString(CultureInfo.InvariantCulture) + "°C";
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