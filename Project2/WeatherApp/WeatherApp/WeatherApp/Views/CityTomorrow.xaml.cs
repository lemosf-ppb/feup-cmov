using System;
using System.Collections.Generic;
using System.Globalization;
using Microcharts;
using SkiaSharp;
using WeatherApp.Models;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CityTomorrow : ContentPage
    {
        private List<WeatherData> WeatherByHours { get; set; }

        public CityTomorrow(CityInfo city, DateTime tomorrow)
        {
            InitializeComponent();
            Console.WriteLine("bamuuus");
            Console.WriteLine(city.Name);
            CityName.Text = city.Name;
            Date.Text = $"{tomorrow:dddd, MMMM d, yyyy}";

            WeatherByHours = city.WeatherForecast.WeatherByDays[1];

            Temperature.Text = WeatherByHours[0].Main.Temperature.ToString(CultureInfo.InvariantCulture);

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
                Console.WriteLine(name);
                if (FindByName(name) is Label tempLabel)
                {
                    var temp = weather.Main.Temperature;
                    var tempString = temp.ToString(CultureInfo.InvariantCulture) + "°C";
                    tempLabel.Text = tempString;

                    var date = weather.DtTxt;
                    var time = date.Substring(11, 2);
                    var entry = new ChartEntry((float) temp)
                    {
                        Label = time + "h",
                        ValueLabel = tempString,
                        Color = SKColors.White
                    };
                    entries[i] = entry;
                }

                minTemp = UpdateMin(weather.Main.TempMin, minTemp);
                maxTemp = UpdateMax(weather.Main.TempMax, maxTemp);

                //WIND
                minWindSpeed = UpdateMin(weather.Wind.Speed, minWindSpeed);
                maxWindSpeed = UpdateMax(weather.Wind.Speed, maxWindSpeed);

                //PRESSURE
                minPressure = UpdateMin(weather.Main.Pressure, minPressure);
                maxPressure = UpdateMax(weather.Main.Pressure, maxPressure);

                //HUMIDITY
                minHumidity = (int) UpdateMin(weather.Main.Humidity, minHumidity);
                maxHumidity = (int) UpdateMax(weather.Main.Humidity, maxHumidity);

                //CLOUDINESS
                minCloudiness = (int) UpdateMin(weather.Clouds.All, minCloudiness);
                maxCloudiness = (int) UpdateMax(weather.Clouds.All, maxCloudiness);

                //PRECIPITATION
                var rainValue = 0;
                if (weather.Rain != null)
                {
                    rainValue = (int) (weather.Rain.Value * 100);
                }

                minRain = UpdateMin(rainValue, minRain);
                maxRain = UpdateMax(rainValue, maxRain);

                i++;
            }

            MaxMinCloudiness.Text = minCloudiness + "/" + maxCloudiness + "%";

            MaxMinHumidity.Text = minHumidity + "/" + maxHumidity + "%";

            MaxMinPrecipitation.Text = minRain + "/" + maxRain + "%";

            MaxMinPressure.Text = minPressure + "/" + maxPressure + " hpa";

            MaxMinWind.Text = minWindSpeed + "/" + maxWindSpeed + " m/s";


            var chart = new LineChart
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

        private async void OnFollowBtnClicked(object sender, EventArgs args)
        {
            //Remove city from list
        }
    }
}