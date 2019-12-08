using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using WeatherApp.Services;
using Xamarin.Forms;

namespace WeatherApp.Models
{
    public class CityInfo
    {
        public string Name { get; set; }
        public WeatherForecast WeatherForecast { get; set; }

        public CityInfo(string name)
        {
            this.Name = name;
            WeatherForecast = new WeatherForecast();
        }

        public async Task OnLoadWeatherForecast()
        {
            WeatherForecast = await WeatherApi.GetWeatherForecast(Name);
            OnCalculateWeatherByDays();
        }

        private void OnCalculateWeatherByDays()
        {
            var weatherByDays = new Dictionary<int, List<WeatherData>>();
            
            var previousHour = 0;
            var day = 0;
            var weatherByHours = new List<WeatherData>();
            
            foreach (var weather in WeatherForecast.Weather)
            {
                var date = DateTime.ParseExact(weather.DtTxt, "yyyy-MM-dd HH:mm:ss",
                    System.Globalization.CultureInfo.InvariantCulture);

                var hour = date.Hour;
                if (hour < previousHour)
                {
                    weatherByDays[day] = weatherByHours;
                    day++;
                    if (day > 1)
                    {
                        break;
                    }
                    weatherByHours = new List<WeatherData>();
                }

                previousHour = hour;
                weatherByHours.Add(weather);
            }

            WeatherForecast.WeatherByDays = weatherByDays;
        }
    }
}